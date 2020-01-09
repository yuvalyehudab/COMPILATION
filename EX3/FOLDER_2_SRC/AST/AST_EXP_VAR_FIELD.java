package AST;

import TYPES.*;
import SYM_TABLE.*;

public class AST_EXP_VAR_FIELD extends AST_EXP_VAR
{
	public AST_EXP_VAR var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_FIELD(AST_EXP_VAR var,String fieldName, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = line;

		/*System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);*/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		/*System.out.format("FIELD\nNAME\n(___.%s)\n",fieldName);*/

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n___.%s",fieldName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);		
	}
	public TYPE SemantMe()
	{
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();
		
		TYPE t = null;
		TYPE tc = null;
		
		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null)
		{
			tc = var.SemantMe();
			
			/*********************************/
			/* [2] Make sure type is a class */
			/*********************************/
			if (!tc.isClass())
			{
				// Code bug -- not accessing a class
			    report_error();
			}
		}
		
		/************************************/
		/* [3] Look for fiedlName inside tc */
		/************************************/
		t = ((TYPE_CLASS)tc).find(fieldName);
		if (t == null) {
			// Code bug -- field does not exist in the class
		    report_error();
		}
		// The name has been declared, so return its type
		return sym_table.find(((TYPE_VAR_DEC)t).getTypeName());

	}
}
