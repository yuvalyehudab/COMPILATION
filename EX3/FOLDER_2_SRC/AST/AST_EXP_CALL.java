package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_CALL extends AST_EXP
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String funcName;
	public AST_EXP_LIST params;
	public AST_EXP_VAR var;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_CALL(AST_EXP_VAR var, String funcName,AST_EXP_LIST params)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();

		this.var = var;
		this.funcName = funcName;
		this.params = params;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		/*System.out.format("CALL(%s)\nWITH:\n",funcName);*/

		/***************************************/
		/* RECURSIVELY PRINT params + body ... */
		/***************************************/
		if (params != null) params.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CALL(%s)\nWITH",funcName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);		
	}
	/***/
	/* SEMANTICS */
	/***/
	public TYPE SemantMe() {
		// Find type of this function
		TYPE funcType = SYMBOL_TABLE.getInstance().find(funcName);
		if (funcType == null) {
			return null; // TODO: Or crash in some other way?
		}

		// Compute types of the arguments that were fed
		TYPE_LIST paramTypes = params.SemantMe();

		// Then check that they have the expected type
		/*expectedTypes = funcType.params;
		if (!expectedTypes.equals(paramTypes)) {
			return null; // TODO: Or crash in some other way?
		}*/

		/* Return expected type */
		return funcType;
	}
}
