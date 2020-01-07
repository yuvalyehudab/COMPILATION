package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_ARRAY extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String type;
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_ARRAY(String name,String type)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();

		this.type = type;
		this.name = name;
	}

	/********************************************************/
	/* The printing message for a declaration list AST node */
	/********************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST DEC ARRAY */
		/*********************************/

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR\nDEC(%s)\n:%s",name,type));

			
	}

	public TYPE SemantMe()
	{
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// Check that name does not already exist in scope
		if (sym_table.find(name) != null) {
			// TODO: Code bug -- name not available
		}

		// The type of the array members
		TYPE t = sym_table.find(type);
		if (t == null || !t.isTypeName()) {
			// TODO: Code bug -- type of members is not in table or just not a name of a type
		}

		// Enter the new type into the table
		sym_table.enter(new TYPE_ARRAY(name, t));

		/*********************************************************/
		/* [4] Return value is irrelevant for array declarations */
		/*********************************************************/
		return null;		
	}
	
}
