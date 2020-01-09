package AST;

import TYPES.*;
import SYM_TABLE.*;

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
		AST_STMT_LIST body, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = line;

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
    /* OLD BROKEN CODE -- NOW USING AST_TYPE_NAME_LIST like a good semaritan
	// Returns a type list from the parameters
	private TYPE_LIST params_processor() {
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// Initialize gathering list
		// TOOD: Must not be null
		TYPE_LIST type_list = null;

		// Process parameter list
		for (AST_TYPE_NAME_LIST it = this.params; it  != null; it = it.tail)
		{
			// Lookup the name given to the currently processed parameter
			TYPE t = sym_table.find(it.head.name);
			// Check it
			if (t == null || !t.isTypeName())
			{
				// Code bug -- type given to param does not exist in table or just is not a name a of a type
			    report_error();
			}
			// Its fine so add to list
			type_list = new TYPE_LIST(t,type_list);
		}
		// The list was reveresed in the process, so reverse before returning the product
		return type_list.reversed();
	}
    */
	public TYPE SemantMe()
	{
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// Check that name does not already exist in scope
		// TODO MAYBE: Find out if allowed to define method with
		//   name of globally defined function - if so,
		//   change search to only current scope
		if (sym_table.find(this.name) != null) {
			// Code bug -- name not available
		    report_error();
		}

		// Check that the current scope is the global scope
		if (!(sym_table.isGlobal()) || !(sym_table.getKind() == SCOPE_KIND.CLASS_SCOPE)) {
			// Code bug -- declaring class in non-global scope
		    report_error();
		}

		// Check return type
		TYPE returnType = sym_table.find(returnTypeName);
		if (returnType == null || (!returnType.isTypeName() && !returnType.isVoid()))
		{
			// Code bug -- type to return does not exist in table or just is not a name a of a type nor void
		    report_error();
		}

		// Process parameters
		TYPE_LIST type_list = null;
		if (this.params != null) {
		    type_list = this.params.SemantMe();
		}

		// Type of the function
		TYPE t = new TYPE_FUNCTION(name, returnType, type_list);

		// Check that there is no shadowing different types
		TYPE_CLASS extending = sym_table.getExtending();
		if (extending != null) {
		    // Look if method/constant is already defined
		    TYPE t_ancestor = extending.find(t.name);
		    if (t_ancestor != null) {
			// Is defined, now check if shadowing is legit
			if (!t_ancestor.isFunction()) {
			    // Code bug -- shadowing not a function
			    report_error();
			}
			TYPE_FUNCTION t_ancestor = (TYPE_FUNCTION)t_ancestor;
			TYPE t_return = t_ancestor.returnType;
			TYPE_LIST ts_params = t_ancestor.params;
			if (!returnType.equals(t_return) || !type_list.equals(ts_params)) {
			    // Code bug -- shadowing of different type
			    report_error();
			}
			// Override case, no need to enter this symbol again
		    }
		}

		// Enter into symbol table now to support recursion
		sym_table.enter(t);

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		sym_table.open(new SYM_TABLE_SCOPE(SCOPE_KIND.FUNCTION_SCOPE, type_list), null, returnType);

		/*******************/
		/* [3] Semant Body */
		/*******************/
		System.out.format("enter body semant:\n");
		body.SemantMe();
		System.out.format("leave body semant:\n");

		/*****************/
		/* [4] End Scope */
		/*****************/
		sym_table.close();

		/*********************************************************/
		/* [6] Return value is irrelevant for funct declarations */
		/*********************************************************/
		System.out.println("out of ast_dec_fumc semant " + name);
		return null;		
	}
	
}
