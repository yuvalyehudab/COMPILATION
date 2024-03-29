package AST;

public class AST_VAR_DEC_NEWEXP extends AST_VAR_DEC
{
	/*********************/
	/*  type name := ne */
	/*********************/
	public String type;
	public String name;
	public AST_EXP_NEW ne;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_VAR_DEC_NEWEXP(String type, String name, AST_EXP_NEW ne)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== varDec -> type var ASSIGN ne SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.name = name;
		this.ne = ne;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE decl ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT EXP ...	   */
		/***********************************/
		
		if (ne != null) ne.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ne.SerialNumber);
	}
}
