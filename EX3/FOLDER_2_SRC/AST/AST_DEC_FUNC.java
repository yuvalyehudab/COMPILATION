package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_FUNC extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String returnTypeName;
	public String name;
	public AST_TYPE_NAME_LIST params;
	public AST_STMT_LIST body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_FUNC(
		String returnTypeName,
		String name,
		AST_TYPE_NAME_LIST params,
		AST_STMT_LIST body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();

		this.returnTypeName = returnTypeName;
		this.name = name;
		this.params = params;
		this.body = body;
	}

	/************************************************************/
	/* The printing message for a function declaration AST node */
	/************************************************************/
	public void PrintMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		/*System.out.format("FUNC(%s):%s\n",name,returnTypeName);*/

		/***************************************/
		/* RECURSIVELY PRINT params + body ... */
		/***************************************/
		if (params != null) params.PrintMe();
		if (body   != null) body.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNC(%s)\n:%s\n",name,returnTypeName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);		
		if (body   != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);		
	}

	// Returns a type list from the parameters
	private TYPE_LIST params_processor() {
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// Initialize gathering list
		TYPE_LIST type_list;

		// Process parameter list
		for (AST_TYPE_NAME_LIST it = params; it  != null; it = it.tail)
		{
			// Lookup the name given to the currently processed parameter
			t = sym_table.find(it.head.type);
			// Check it
			if (t == null || !t.isTypeName())
			{
				// TODO: Code bug -- type given to param does not exist in table or just is not a name a of a type
			}
			// Its fine so add to list
			type_list = new TYPE_LIST(t,type_list);
		}
		// Done
		return type_list;
	}

	public void SemantMe()
	{
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// TODO: Check that scope is global

		// Check return type
		TYPE returnType = sym_table.find(returnTypeName);
		if (t == null || (!t.isTypeName() && !t.isVoid()))
		{
			// TODO: Code bug -- type to return does not exist in table or just is not a name a of a type nor void
		}

		// Process parameters
		TYPE_LIST type_list = this.params_processor();

		// Type of the function
		TYPE t = new TYPE_FUNCTION(name, returnType, type_list);

		// Enter into symbol table
		sym_table.enter(t);

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope(new SYM_TABLE_SCOPE(FUNCTION, type_list), null);

		/*******************/
		/* [3] Semant Body */
		/*******************/
		System.out.format("enter body semant:\n");
		body.SemantMe();
		System.out.format("leave body semant:\n");

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/*********************************************************/
		/* [6] Return value is irrelevant for class declarations */
		/*********************************************************/
		System.out.println("out of ast_dec_fumc semant " + name);
		return null;		
	}
	
}
