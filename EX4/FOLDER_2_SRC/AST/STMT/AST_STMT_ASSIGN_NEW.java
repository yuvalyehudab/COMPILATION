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

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_NEW_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN_NEW(AST_VAR var,AST_NEW_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		//System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
	{
		
		if (var != null) var.semantPost(st);
		if (exp != null) exp.semantPost(st);
		
		if (!var.t.canAssign(exp.t))
		{
			throw new SYMBOL_TABLE_ERROR(this);
		}
	}
	public Reg irMe(Manager context)
	{
		var.ir_assign(context, () -> exp.irMe(context));
		return NonExistsRegister.instance;
	}

}
