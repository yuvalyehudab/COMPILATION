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
	public AST_DEC_VAR(String type,String name,AST_EXP initialValue, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = line;

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
		    report_error("// Code bug -- name not available");
		}

		// Check declared type
		TYPE t = sym_table.find(this.type);
		if (t == null || !t.isTypeName())
		{
		    report_error("// Code bug -- type of variable does not exist in table or just is not a name a of a type");
		}

		// Check that there is no shadowing different types
		TYPE_CLASS extending = sym_table.getExtending();
		if (extending != null) {
		    // Look if method/constant is already defined
		    TYPE t_ancestor = extending.find(this.name);
		    if (t_ancestor != null) {
			// Is defined, now check if shadowing is legit
			if (t_ancestor.isFunction()) {
			    report_error("// Code bug -- shadowing a function");
			}
			if (!t.equals(t_ancestor)) {
			    report_error("// Code bug -- shadowing of different type");
			}
			// Override case, no need to enter this symbol again
		    }
		}

		// Check initial value type
		if (this.initialValue != null) {
		    // t_init cannot be null because grammar says it is an expression
			TYPE t_init = this.initialValue.SemantMe();
			// Check that it is an expected type
			if (!t.isAsExpected(t_init)) {
				// Code bug -- type not as expected
				report_error();
			}
		}

		// Enter into symbol table
		sym_table.enter(new TYPE_VAR_DEC(t,this.name));

		return null;
	}

}
