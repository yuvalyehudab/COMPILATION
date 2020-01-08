package AST;

import TYPES.*;
import SYM_TABLE.*;

public class AST_EXP_VAR_SUBSCRIPT extends AST_EXP_VAR
{
	public AST_EXP_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_SUBSCRIPT(AST_EXP_VAR var,AST_EXP subscript, int line)
	{
		System.out.print("====================== var -> var [ exp ]\n");
		lineNumber = line;
		
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
	}

	public TYPE SemantMe() {
		// Semant the var
		TYPE arrayT = var.SemantMe();
		if (!arrayT.isArray()) {
			// Code bug -- accessing a non-array
		    report_error();
		}
		TYPE indexT = subscript.SemantMe();
		if (indexT != TYPE_INT.getInstance()) {
			// Code bug -- index is not an int
		    report_error();
		}

		return ((TYPE_ARRAY)arrayT).memberT;
	}
}
