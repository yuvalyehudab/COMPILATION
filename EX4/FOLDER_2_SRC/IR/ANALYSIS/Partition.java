package IR.ANALYSIS;

import IR.COMMAND.*;
import IR.REG.*;
import IR.*;

import SYMBOL_TABLE.*;
import java.util.*;
import java.util.stream.*;


public class Partition {
    private List<IRCommand> commands;
    private HashMap<IRCommand_Label, IRBlock> blockMap = new HashMap<>();
    private List<IRBlock> blockList = new ArrayList<>();
    private IRBlock current = null;
    private IRBlock previous = null;
    private boolean continues = false;

    public Partition(List<IRCommand> commands) {
	this.commands = commands;
    }

    public List<IRBlock> juice() {
	this.commands
	    .forEach(cmd ->
		     {
			 if (cmd instanceof IRCommand_Label) {
			     labelPart((IRCommand_Label) cmd);
			 } else if (cmd instanceof IRCommand_Goto) {
			     flowPart(cmd, ((IRCommand_Goto) cmd).label);
			 } else if (cmd instanceof IRCommand_Jump_If_Eq_To_Zero) {
			     flowPart(cmd, ((IRCommand_Jump_If_Eq_To_Zero) cmd).label);
			 } else if (cmd instanceof IRCommand_Jump_If_Not_Eq_To_Zero) {
			     flowPart(cmd, ((IRCommand_Jump_If_Not_Eq_To_Zero) cmd).label);
			 } else {
			     noPart(cmd);
			 }
		     });
	if (current != null) {
	    blockList.add(current);
	}
	return blockList;
    }

    private void flowPart( IRCommand command, IRCommand_Label label) {
        if (current == null) {
            refresh(new IRBlock());
        }
	
        current.add(command);

        IRBlock another = blockMap.computeIfAbsent(label, IRBlock::new);
        current.followers.add(another);

        previous = current;
        continues = !(command instanceof IRCommand_Goto);
        refresh(null);
    }

    private void labelPart(IRCommand_Label label) {
	/// block initialized when label is first seen
        IRBlock succ = blockMap.computeIfAbsent(label, IRBlock::new);
        if (current != null) {
            previous = current;
            continues = true;
        }
        refresh(succ);
    }

    private void noPart(IRCommand command) {
        if (current == null) {
            refresh(new IRBlock());
        }

        current.add(command);

        if (command instanceof IRCommand_Return) {
            refresh(null);
        }
    }

    private void refresh(IRBlock fresh) {
        if (current != null) {
            blockList.add(current);
	}
	
        current = fresh;
	
	if (current != null) {
	    if (previous != null) {
		previous.succ = current;
		if (continues) {
		    previous.followers.add(current);
		}
	    }
	    previous = null;
	}
    }
}
