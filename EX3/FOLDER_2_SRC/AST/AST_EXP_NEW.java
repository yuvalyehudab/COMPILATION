package AST;

import TYPES.*;

public class AST_EXP_NEW extends AST_EXP
{

	public AST_EXP exp;
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_NEW(String name, AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();
		
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> NEW exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
		this.name = name;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{

		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/****************************************/
		/* RECURSIVELY PRINT exp ...			*/
		/****************************************/
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NEW_EXP(%s)",name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
	public TYPE SemantMe()
	{
		TYPE t1 = null;
		
		if (exp  != null) t1 = exp.SemantMe();
		
		if (t1 == TYPE_INT.getInstance())
		{
			return TYPE_INT.getInstance();
		}
		System.exit(0);
		return null;
	}

}
