package IR.ANALYSIS;

import IR.COMMAND.*;
import IR.REG.*;
import IR.*;

import SYMBOL_TABLE.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class IRBlock {
    // (live0,cmd0);(live1,cmd1)...;(liveN,cmdN);afterlife
    public List<LivenessCommand> commands;
    public Set<IRBlock> followers;
    public IRBlock succ;
    public IRCommand_Label label;
    public Set<Reg> afterlife;
    private boolean stasis;

    public IRBlock(IRCommand_Label label) {
        this.commands = new ArrayList<>();
        this.followers = new HashSet<>();
	this.succ = null;
        this.label = label;
	this.afterlife = new HashSet<>();
	this.stasis = false;
    }

    public IRBlock() {
        this(null);
    }
    
    public static Map<Reg, Integer> colorizor(Set<IRBlock> blocks) {
	// Liveness Analysis
	while (blocks.stream().anyMatch(b -> !b.isInStasis())) {
	    blocks.forEach(b -> b.round());
	}
	// Extract interference
	Set<Set<Reg>> interfSet = new HashSet<>();
	blocks.forEach(b -> interfSet.addAll(b.getAllLiveness()));
	// If a set is strictly contained in another it is superfluous
	Predicate<Set<Reg>> testContainment = i -> interfSet.stream().anyMatch(j -> j.containsAll(i) && !i.containsAll(j));
	interfSet.removeIf(testContainment);
	// Color using a graph
	return (new Graph(interfSet)).juice();
    }

    public void add(IRCommand command) {
	this.commands
	    .add(new LivenessCommand(command));
    }

    public IRCommand getFirstCommand() {
	return commands.get(0).command;
    }

    private void round() {
	Set<Reg> union = new HashSet<>();
	this.followers.forEach(block -> union.addAll(block.afterlife));
	this.analyze(union);
    }
    
    private void analyze(Set<Reg> afterlife) {
	if (this.afterlife.containsAll(afterlife)) {
	    this.stasis = true;
	} else {
	    this.stasis = false;
	    this.setAfterlife(afterlife);
	    this.bubble();
	}
    }
    
    private void bubble() {
	int length = this.commands.size();
	for (int i = 0; i < length ; i++) {
	    int j = length - (i + 1); // Going bottom-up
	    Set<Reg> below = i == 0 ? this.afterlife : this.commands.get(j).getPreliving(); // Bottommost: afterlife
	    LivenessCommand current = this.commands.get(j-1);
	    current.morphPreliving(below, current.command.reads, current.command.writes);
	}
    }
    
    private void setAfterlife(Set<Reg> afterlife) {
	this.afterlife = afterlife;
    }
    
    public Set<Reg> getPreliving() {
	if (this.commands.size() == 0) {
	    return this.afterlife;
	} else {
	    return this.commands.get(0).getPreliving();
	}
    }

    public boolean isStartingBlock() {
        return label != null && label.isStartingLabel();
    }

    public boolean isInStasis() {
	return this.stasis;
    }

    public Set<Set<Reg>> getAllLiveness() {
	Set<Set<Reg>> result = new HashSet<>();
	this.commands
	    .forEach(cmd -> result.add(cmd.getPrelivingTemps()));
	return result; // [afterlife] will be included anyway in one of the next blocks (if there are no next blocks it's just empty)
    }

    public Set<IRBlock> getAllReachables() {
        Set<IRBlock> reached = new HashSet<>();
        transitivelyClose(reached);
        return reached;
    }

    private void transitivelyClose(Set<IRBlock> reached) {
        if (!reached.contains(this)) {
	    reached.add(this);
	    followers.forEach(block -> block.transitivelyClose(reached));
	}
    }
    public class LivenessCommand {
	public IRCommand command;
	private Set<Reg> preliving;
	private Predicate<Reg> testNotTemp = r -> !r.isTemp();
	public LivenessCommand(IRCommand command) {
	    this.command = command;
	    this.preliving = new HashSet<>();
	}
	public Set<Reg> getPreliving() {
	    return this.preliving;
	}
	public Set<Reg> getPrelivingTemps() {
	    Set<Reg> prelivingTemps = this.preliving;
	    prelivingTemps.removeIf(testNotTemp);
	    return prelivingTemps;
	}
	public void morphPreliving(Set<Reg> orig, Set<Reg> rem, Set<Reg> add) {
	    this.preliving = new HashSet(orig);
	    this.preliving.removeAll(rem);
	    this.preliving.addAll(add);
	}
    }
}




