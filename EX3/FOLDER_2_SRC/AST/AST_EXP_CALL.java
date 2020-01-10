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
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);		
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
			    report_error("// Code bug -- type of var is not a class so it does not have methods");
			}
			funcType = ((TYPE_CLASS)varType).find(this.funcName);
		} else {
			funcType = sym_table.find(this.funcName);
		}

		// Make sure it is a function
		if (funcType == null || !funcType.isFunction()) {
		    report_error("// Code bug -- function name not in table or not a name of a function");
		}

		// Compute types of the arguments that were fed
		TYPE_LIST paramTypes;
		if (this.params != null) {
			paramTypes = this.params.SemantMe();
		} else {
			paramTypes = null;
		}

		// Then check that they have expected types
		TYPE_LIST expectedTypes = decls_replacer(((TYPE_FUNCTION)funcType).params);
		
		if (paramTypes == null && expectedTypes != null) {
		    report_error("// Code bug -- no arguments though expected");
		}
		if (paramTypes != null && expectedTypes == null) {
		    report_error("// Code bug -- arguments though unexpected");
		}
		if (paramTypes != null && expectedTypes != null && !expectedTypes.isAsExpected(paramTypes)) {
		    report_error("// Code bug -- incorrect argument types");
		}

		/* Return expected type */
		return ((TYPE_FUNCTION)funcType).returnType;
	}

    private TYPE_LIST decls_replacer (TYPE_LIST decl_list) {
	// Go from decl of types to the types declared
	// Initialize pointer to symbol table
	SYM_TABLE sym_table = SYM_TABLE.getInstance();
	
	if (decl_list != null) {
	    TYPE_VAR_DEC casted_head = (TYPE_VAR_DEC)(decl_list.head);
	    TYPE the_type = sym_table.find(casted_head.getTypeName());
	    return new TYPE_LIST(the_type, decls_find(decl_list.tail));
	}
	return null;
    }
}
