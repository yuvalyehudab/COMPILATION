package AST;

public class AST_STMT_RETURN extends AST_STMT
{
	/******************************/
	/*       exp                  */
	/******************************/
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp) {
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for a function statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/**********************************************/
		/* AST NODE TYPE = AST FUNCTION USE STATEMENT */
		/**********************************************/
		System.out.print("AST NODE EXP STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT       EXP ... */
		/***********************************/
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"EXP\n\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/

		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
}
