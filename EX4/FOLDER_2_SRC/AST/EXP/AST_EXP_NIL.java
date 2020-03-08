package AST.EXP;

import AST.*;
import TYPES.*;
import IR.REG.*;
import IR.*;
import IR.COMMAND.*;
import SYMBOL_TABLE.*;


public class AST_EXP_NIL extends AST_EXP
{
    public boolean is_const = false;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_NIL()
    {
	/******************************/
	/* SET A UNIQUE SERIAL NUMBER */
	/******************************/
	SerialNumber = AST_Node_Serial_Number.getFresh();

	this.t = TYPE_NIL.instance;
		
    }
	
	
    public Reg irMe(Manager context)
    {
	Reg t = context.newRegister();
	context.command(new IRCommand_Const(t, Manager.NIL_VALUE));
	return t;
    }
    public void semantPost(SYMBOL_TABLE st){}
}
