package SYMBOL_TABLE;

import AST.AST_Node;

public class SYMBOL_TABLE_ERROR extends Exception {
    private final AST_Node node;

    public SYMBOL_TABLE_ERROR(AST_Node node) {
        this.node = node;
    }

    public AST_Node getNode() {
        return node;
    }
}
