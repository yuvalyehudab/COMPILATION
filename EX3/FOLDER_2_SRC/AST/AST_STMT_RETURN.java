package AST;

import TYPES.*;
import SYM_TABLE.*;

public class AST_STMT_RETURN extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = line;

		this.exp = exp;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE STMT RETURN\n");

		/*****************************/
		/* RECURSIVELY PRINT exp ... */
		/*****************************/
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() {
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// Check that one of scopes is a function declaration
		if (!sym_table.isFunctionScope()) {
		    report_error("// Code bug -- return outside of a function scope");
		}

		TYPE expected = sym_table.getReturnType();

		if (exp == null) {
			if (expected != null) {
			    report_error("// Code bug -- expected a return value");
			} else {
			    report_error("// No return value for void function");
				return null;
			}
		} else {
			if (expected == null) {
			    report_error("// Code bug -- void cannot return a value");
			} else {
			    debug_print("expected: " + expected.name);
				// Check that types match
				TYPE expT = exp.SemantMe();

				// Check type is as expected
				if (!expected.isAsExpected(expT)) {
				    report_error("// Code bug -- type in return statement not as expected");
				}
				return null;
			}
		}
		return null;
	}
}
