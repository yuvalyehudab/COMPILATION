package AST.EXP;

import AST.*;
import SYMBOL_TABLE.*;
import IR.*;
import IR.ANALYSIS.*;
import IR.COMMAND.*;
import IR.REG.*;
import TYPES.*;


public abstract class AST_EXP extends AST_Node
{
    public TYPE t;
	
    public boolean is_const = false;
	
    Object get_const_value(){return null;}
	
}
