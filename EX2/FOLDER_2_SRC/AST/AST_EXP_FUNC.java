package AST;

public class AST_EXP_FUNC extends AST_EXP
{
	public AST_VAR var;
	public AST_EXP_LIST exps;
	public String name;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_FUNC(AST_VAR var, String name, AST_EXP_LIST exps)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exps != null) System.out.print("====================== exp -> var.ID LPAREN exps RPAREN\n");
		if (exps == null) System.out.print("====================== exp -> var.ID LPAREN      RPAREN\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exps = exps;
		this.name = name;
	}
	
	public AST_EXP_FUNC(String name, AST_EXP_LIST exps)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exps != null) System.out.print("====================== exp -> ID LPAREN exps RPAREN\n");
		if (exps == null) System.out.print("====================== exp -> ID LPAREN      RPAREN\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = null;
		this.exps = exps;
		this.name = name;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		
		/*************************************/
		/* AST NODE TYPE = AST FUNC EXP */
		/*************************************/
		System.out.print("AST NODE FUNC EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (var != null) var.PrintMe();
		if (exps != null) exps.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VAR FUNC EXP"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exps != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exps.SerialNumber);
	}
}
