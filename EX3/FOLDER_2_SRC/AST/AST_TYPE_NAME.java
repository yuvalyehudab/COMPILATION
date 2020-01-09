/***********/
/* PACKAGE */
/***********/
package AST;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import SYM_TABLE.*;

public class AST_TYPE_NAME extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String type;
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_TYPE_NAME(String type,String name, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = line;

		this.type = type;
		this.name = name;
	}

	/*************************************************/
	/* The printing message for a type name AST node */
	/*************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST TYPE NAME NODE */
		/**************************************/
		System.out.format("NAME(%s):TYPE(%s)\n",name,type);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NAME:TYPE\n%s:%s",name,type));
	}

    public TYPE SemantMe() {
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();
		
		// Lookup the name
		TYPE t = sym_table.find(this.type);
		// Check it
    	System.out.println("debug: AST_TYPE_NAME.semant_me 54 54545454545454545454 before");
    	System.out.println("t, t.isTypeName");
    	System.out.println(this.name);
		if (t == null || !t.isTypeName())
		    {
			// Code bug -- type given does not exist in table or just is not a name a of a type
			report_error();
		    }
			// Its fine, declare it
		return new TYPE_VAR_DEC(t,this.name);
    }
}
