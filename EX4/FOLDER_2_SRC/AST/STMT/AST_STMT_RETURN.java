/***********/
/* PACKAGE */
/***********/
package AST.STMT;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import AST.*;
import AST.DEC.*;
import AST.VAR.*;
import AST.EXP.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;
import SYMBOL_TABLE.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import java.util.*;


public class AST_STMT_RETURN extends AST_STMT
{
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_EXP exp;
    public SYMBOL ownerFunc;

    /*******************/
    /*  CONSTRUCTOR(S) */
    /*******************/
    public AST_STMT_RETURN(AST_EXP exp)
    {
	/******************************/
	/* SET A UNIQUE SERIAL NUMBER */
	/******************************/
	SerialNumber = AST_Node_Serial_Number.getFresh();

	this.exp = exp;
    }

    public AST_STMT_RETURN() {
	this(null);
    }
	
    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {
	if (exp != null) exp.semantPost(st);
		
	ownerFunc = st.ownerFunc;
	if (ownerFunc == null)
	    {
		throw new SYMBOL_TABLE_ERROR(this);
	    }
	TYPE_FUNCTION f = ownerFunc.getFunc();
	if (f.returnType == TYPE_VOID.instance){
	    if (exp != null)
		throw new SYMBOL_TABLE_ERROR(this);
	}else if (exp == null){
	    throw new SYMBOL_TABLE_ERROR(this);
	}
	else if (!f.returnType.canAssign(exp.t))
	    throw new SYMBOL_TABLE_ERROR(this);
    }
    public Reg irMe(Manager context)
    {
	if (exp != null) {
            Reg temp = exp.irMe(context);
            context.command(new IRCommand_Set(RetReg.instance, temp));
        }
        context.command(new IRCommand_Goto(context.returnLabelFor(ownerFunc)));

        return NonExistsRegister.instance;
    }
	


}
