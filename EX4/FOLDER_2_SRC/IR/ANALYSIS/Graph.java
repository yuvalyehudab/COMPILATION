package IR.ANALYSIS;

import IR.REG.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

// Input: `cliques` - set of sets of nodes
// Output: `Graph(cliques).run()` - set of sets of colored nodes

public class Graph {
    private Stream<Stream<Node>> graph;
    private Stack<Reg> cache = new Stack<>();
    private Map<Reg, Integer> colors = new HashMap<>();
    public Graph(Set<Set<Reg>> cliques) {
	this.graph = cliques.stream().map(c -> c.stream().map(Node::new));
    }
    public Map<Reg, Integer> juice() {
	this.cache();
	this.uncache();
	return this.colors;
    }
    private void push(Reg n) {
	// Record the push in the graph
	this.graph
	    .forEach(clique -> clique
		     .forEach(node -> {
			     if (node.isData(n)) {
				 // Remove all appearances of this node in cliques
				 node.cache();
			     }
			 })
		     );
	// Record the push in the cache
	this.cache.push(n);
    }
    private void cache() {
	this.graph
	    .forEach(clique -> clique
		     .forEach(node -> {
			     if (!node.isHasBeenCached()) { // Otherwise it was already pushed to the stack
				 // Push all appearances of this node in cliques
				 this.push(node.getData());
			     }
			 })
		     );
    }
    
    // Get all the colors that node must connect to
    private Set<Integer> getColors(Reg n) {
	Set<Integer> touching = new HashSet<>();
	this.graph
	    .filter(clique -> clique.anyMatch(node -> node.isData(n))) // Filter for cliques with this node
	    .forEach(clique -> clique.forEach(node -> {
			Integer c = this.colors.get(node);
			if (c != null) {
			    touching.add(c);
			}
		    })); // Get the colors from those cliques
	return touching;
    }
    private void pop() {
	// Record the pop in the cache and get the node
	Reg n = cache.pop();
	// Choose a color for the node
	Set<Integer> connected = this.getColors(n);
	int c = 0;
	while (connected.contains(c)) {
	    c++;
	}
	final int chosen = c;
	// Record the pop in the graph by coloring
	this.graph
	    .forEach(clique -> clique
		     .forEach(node -> {
			     if (node.isData(n)) {
				 colors.put(n,chosen);
			     }
			 })
		     );
    }
    private void uncache() {
	while (!cache.empty()) {
	    this.pop();
	}
    }
}

