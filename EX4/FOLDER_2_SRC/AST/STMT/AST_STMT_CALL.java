/***********/
/* PACKAGE */
/***********/
package AST.STMT;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import AST.*;
import AST.DEC.*;
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


public class AST_STMT_CALL extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP_CALL callExp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_CALL(AST_EXP_CALL callExp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.callExp = callExp;
	}
	
	public Reg irMe(Manager context)
	{
		return callExp.irMe(context);
	}
	public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR {
        callExp.semantPost(st);
    }
	public void PrintMe()
	{
		callExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nCALL"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,callExp.SerialNumber);		
	}
}
