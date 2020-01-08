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
	public AST_STMT_RETURN(AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();

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
		    // Code bug -- return outside of a function scope
		    report_error();
		}

		TYPE expected = sym_table.getReturnType();

		if (exp == null) {
			if (expected != null) {
				// Code bug -- expected a return value
			    report_error();
			} else {
				// No return value for void function
				return null;
			}
		} else {
			if (expected == null) {
				// Code bug -- void cannot return a value
			    report_error();
			} else {
				// Check that types match
				TYPE expT = exp.SemantMe();
				TYPE varT = expected;

				// Copied from AST_STMT_ASSIGN
				// TODO MAYBE: Abstract this to a function
				if (expT == TYPE_NIL.getInstance()) {
				    if (varT.isClass() || varT.isArray()) {
					// nil is an acceptable class / array
					return null;
				    } else {
					// Code bug -- nil inappropriate
					report_error();
				    }
				}

				// Class inheritance
				if (expT.isClass()) {
				    if (expT.isAncestor(varT.name)) {
					// legal inheritance or equal
					return null;
				    } else {
					// Code bug -- no inheritance or equality
					report_error();
				    }
				}
			
		
				if (varT.name != expT.name)
				    {
					// Code bug -- types mismatch
					report_error();
				    }
				return null;
			}
		}
		return null;
	}
}
