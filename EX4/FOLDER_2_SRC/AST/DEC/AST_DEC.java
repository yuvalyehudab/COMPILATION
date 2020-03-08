package AST.DEC;

import TYPES.*;

import AST.*;
import SYMBOL_TABLE.*;



public abstract class AST_DEC extends AST_Node
{
    public String type;
    public String name;
	
    public AST_DEC(String type, String name)
    {
	this.type = type;
	this.name = name;
    }
    public abstract void semantPre(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR;

}
