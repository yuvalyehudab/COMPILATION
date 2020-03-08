package IR.ANALYSIS;

import IR.REG.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class Node {
    private Reg data;
    private boolean hasBeenCached;
    public Node(Reg data) {
	this.data = data;
	this.hasBeenCached = false;
    }
    public Reg getData() {
	return data;
    }
    public boolean isData(Reg data) {
	return data == this.data;
    }
    public boolean isHasBeenCached() {
	return this.hasBeenCached;
    }
    public void cache() {
	this.hasBeenCached = true;
    }
}
