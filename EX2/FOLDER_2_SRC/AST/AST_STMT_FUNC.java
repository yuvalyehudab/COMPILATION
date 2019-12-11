package AST;

public class AST_STMT_FUNC extends AST_STMT
{
	/******************************/
	/*  [var .] ID([exp[, exp]*]) */
	/******************************/
	public AST_VAR var;
	public AST_EXP_LIST exps;
	public String name;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_FUNC(AST_VAR var, String name, AST_EXP_LIST exps)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var.ID (expList) SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.name = name;
		this.exps = exps;
	}
	
	public AST_STMT_FUNC(String name, AST_EXP_LIST exps)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> ID (expList) SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MENBERS ... */
		/*******************************/
		this.var = null;
		this.name = name;
		this.exps = exps;
	}

	/*********************************************************/
	/* The printing message for a function statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/**********************************************/
		/* AST NODE TYPE = AST FUNCTION USE STATEMENT */
		/**********************************************/
		System.out.print("AST NODE FUNC USE STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exps != null) exps.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC()\n\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exps != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exps.SerialNumber);
	}
}
