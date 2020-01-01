package AST;

import TYPES.*;

public class AST_EXP_NIL extends AST_EXP
{
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_NIL()
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();

	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT NIL */
		/*******************************/

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NIL"));
	}
	public TYPE SemantMe()
	{
		return TYPE_INT.getInstance();
	}
}
