package AST;

import TYPES.*;
import SYM_TABLE.*;

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
	public AST_EXP_CALL(AST_EXP_VAR var, String funcName,AST_EXP_LIST params, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = line;

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

		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		TYPE varType;
		TYPE funcType;

		// Set funcType
		if (this.var != null) {
			varType = this.var.SemantMe();
			if (varType.kind != KIND.CLASS) {
				// Code bug -- type of var is not a class so it does not have methods
			    report_error();
			}
			funcType = ((TYPE_CLASS)varType).find(this.funcName);
		} else {
			funcType = sym_table.find(this.funcName);
		}

		// Make sure it is a function
		if (funcType == null || funcType.isFunction()) {
			// Code bug -- function name not in table or not a name of a function
		    report_error();
		}

		// Compute types of the arguments that were fed
		TYPE_LIST paramTypes;
		if (this.params != null) {
			paramTypes = this.params.SemantMe();
		} else {
			paramTypes = null;
		}

		// Then check that they have expected types
		TYPE_LIST expectedTypes = ((TYPE_FUNCTION)funcType).params;
		if (paramTypes == null && expectedTypes != null) {
		    // Code bug -- no arguments though expected
		    report_error();
		}
		if (paramTypes != null && expectedTypes == null) {
		    // Code bug -- arguments though unexpected
		    report_error();
		}
		if (paramTypes != null && expectedTypes != null && !expectedTypes.isAsExpected(paramTypes)) {
			// Code bug -- incorrect argument types
		    report_error();
		}

		/* Return expected type */
		return ((TYPE_FUNCTION)funcType).returnType;
	}
}
