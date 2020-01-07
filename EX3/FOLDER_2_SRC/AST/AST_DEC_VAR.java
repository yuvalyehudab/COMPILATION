package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

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
		if (sym_table.getConstructedTypeList.find(name) != null) {
			// TODO: Code bug -- name not available
		}

		// Check return type
		TYPE t = sym_table.find(this.type);
		if (t == null || !t.isTypeName())
		{
			// TODO: Code bug -- type of variable does not exist in table or just is not a name a of a type
		}

		// Check initial value type
		if (this.initialValue != null) {
			TYPE t_init = this.initialValue.SemantMe();
			// Check that they match, or initial is decendent or nil
			if (!t.equals(t_init)) {
				if (t_init.kind == NIL) {
					if (t.kind != CLASS && t.kind != ARRAY) {
						// TODO: Code bug -- nil is only allowed for class or array
					}
				}
				if (t_init.kind == CLASS) {
					if (!t_init.isAncestor(t)) {
						// TODO: Code bug -- init is not decendent not nill
					}
				}
				// TODO: Code bug -- types do not strictly match and special cases all fail
			}
		}

		// Enter into symbol table
		sym_table.enter(t);

		return null;
	}

}
