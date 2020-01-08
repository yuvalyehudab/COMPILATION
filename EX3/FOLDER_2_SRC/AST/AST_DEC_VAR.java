package AST;

import TYPES.*;
import SYM_TABLE.*;

public class AST_DEC_VAR extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String type;
	public String name;
	public AST_EXP initialValue;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_VAR(String type,String name,AST_EXP initialValue)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();

		this.type = type;
		this.name = name;
		this.initialValue = initialValue;
	}

	/********************************************************/
	/* The printing message for a declaration list AST node */
	/********************************************************/
	public void PrintMe()
	{
		/********************************/
		/* AST NODE TYPE = AST DEC LIST */
		/********************************/
		/*if (initialValue != null) System.out.format("VAR-DEC(%s):%s := initialValue\n",name,type);
		if (initialValue == null) System.out.format("VAR-DEC(%s):%s                \n",name,type);*/

		/**************************************/
		/* RECURSIVELY PRINT initialValue ... */
		/**************************************/
		if (initialValue != null) initialValue.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR\nDEC(%s)\n:%s",name,type));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (initialValue != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,initialValue.SerialNumber);		
			
	}

	public TYPE SemantMe()
	{
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// Check that name does not already exist in the innermost scope
		if (sym_table.getConstructedTypeList() != null && sym_table.getConstructedTypeList().find(name) != null) {
			// TODO: Code bug -- name not available
		}

		// Check declared type
		TYPE t = sym_table.find(this.type);
		if (t == null || !t.isTypeName())
		{
			// Code bug -- type of variable does not exist in table or just is not a name a of a type
		    report_error();
		}

		// Check initial value type
		if (this.initialValue != null) {
		    // t_init cannot be null because grammar says it is an expression
			TYPE t_init = this.initialValue.SemantMe();
			// Check that they match, or initial is decendent or nil
			if (!t.equals(t_init)) {
				if (t_init.kind == KIND.NIL) {
					if (t.kind != KIND.CLASS && t.kind != KIND.ARRAY) {
						// Code bug -- nil is only allowed for class or array
					    report_error();
					}
				}
				if (t_init.kind == KIND.CLASS) {
					if (!((TYPE_CLASS)t_init).isAncestor(t.name)) {
						// Code bug -- init is not decendent not nil
					    report_error();
					}
				}
				// Code bug -- types do not strictly match and special cases all fail
				report_error();
			}
		}

		// Enter into symbol table
		sym_table.enter(new TYPE_VAR_DEC(t,this.name));

		return null;
	}

}
