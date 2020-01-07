package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_CLASS extends AST_DEC
{
	/********/
	/* NAME */
	/********/
	public String name;
	
	/**********/
	/* FATHER */
	/**********/
	public String father;
	
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_LIST data_members;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_CLASS(String name, AST_DEC_LIST data_members, String father)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();
	
		this.name = name;
		this.data_members = data_members;
		this.father = father;
	}

	/*********************************************************/
	/* The printing message for a class declaration AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		/*System.out.format("CLASS DEC = %s\n",name);*/
		if (data_members != null) data_members.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CLASS\n%s",name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,data_members.SerialNumber);		
	}
	
	public TYPE SemantMe()
	{
		// Point at the table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// Check that name does not already exist in scope
		if (sym_table.find(this.name) != null) {
			// TODO: Code bug -- name not available
		}

		// Check that the current scope is the global scope
		if (!sym_table.isGlobal()) {
			// TODO: Code bug -- declaring class in non-global scope
		}

		TYPE fatherClass;
		// Find father if needed
		if (this.father != null) {
			fatherClass = sym_table.find(this.fatherName);
			if (fatherClass == null) {
				// TODO: Code bug -- class to be extended does not exist
			}
			if (fatherClass.kind != CLASS) {
				// TODO: Code bug -- extending a non-class
			}
		}
		// At this point fatherClass is a class or null

		System.out.println("enter semant AST_DEC_CLASS");
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		sym_table.open(new SYM_TABLE_SCOPE(CLASS_SCOPE, null), fatherClass, null);

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		// Semant the data members
		data_members.SemantMe();

		// Construct the class
		TYPE_CLASS result = new TYPE_CLASS(this.name, sym_table.getConstructedTypeList(), fatherClass);

		/*****************/
		/* [3] End Scope */
		/*****************/
		sym_table.close();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		sym_table.enter(result);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
}
