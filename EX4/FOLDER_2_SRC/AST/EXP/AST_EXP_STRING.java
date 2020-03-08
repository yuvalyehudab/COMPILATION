package AST.EXP;

import TYPES.*;
import IR.REG.*;
import IR.*;
import AST.*;
import IR.COMMAND.*;
import SYMBOL_TABLE.*;

public class AST_EXP_STRING extends AST_EXP
{
    public String value;
    public boolean is_const = true;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_STRING(String value)
    {
	/******************************/
	/* SET A UNIQUE SERIAL NUMBER */
	/******************************/
	SerialNumber = AST_Node_Serial_Number.getFresh();

	//System.out.format("====================== exp -> STRING( %s )\n", value);
	this.t = TYPE_STRING.instance;
	this.value = value;
    }

	
    public Reg irMe(Manager context)
    {
	Reg t = context.newRegister();
	context.command(new IRCommand_Load_From_Label(t, context.labelForConstantString(value)));
	return t;
    }
	
    public void semantPost(SYMBOL_TABLE st){}
    Object get_const_value(){return value;}
}
