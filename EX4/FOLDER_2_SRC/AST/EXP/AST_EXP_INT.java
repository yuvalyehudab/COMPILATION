package AST.EXP;

import TYPES.*;
import IR.REG.*;
import IR.COMMAND.*;
import IR.*;
import SYMBOL_TABLE.*;
import AST.*;


public class AST_EXP_INT extends AST_EXP
{
    public int value;
    public boolean is_const = true;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_INT(int value, boolean sign)
    {
	/******************************/
	/* SET A UNIQUE SERIAL NUMBER */
	/******************************/
	SerialNumber = AST_Node_Serial_Number.getFresh();

	this.t = TYPE_INT.instance;
	this.value = value;
	if (!sign)
	    this.value = value * (-1);
    }
    public AST_EXP_INT(int value){this(value, true);}
	
    public Reg irMe(Manager context)
    {
	Reg t = context.newRegister();
	context.command(new IRCommand_Const(t, value));
	return t;
    }
    public void semantPost(SYMBOL_TABLE st){	}
    Object get_const_value(){return value;}
}
