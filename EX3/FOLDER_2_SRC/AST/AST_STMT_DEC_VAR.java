package AST;

import TYPES.*;
import SYM_TABLE.*;

public class AST_STMT_DEC_VAR extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_VAR var;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_DEC_VAR(AST_DEC_VAR var, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = line;

		this.var = var;
	}
	
	public void PrintMe()
	{
		var.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nDEC\nVAR"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);		
	}
	
	public TYPE SemantMe()
	{
	    debug_print("starting variable decl");
		return var.SemantMe();
	}
}
