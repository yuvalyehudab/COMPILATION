package AST;

public class AST_STMT_VAR_DEC extends AST_STMT
{
	/***************/
	/*  type id */
	/***************/
	public AST_VAR_DEC varDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_VAR_DEC(AST_VAR_DEC varDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> varDec SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.varDec = varDec;
	}

	/*********************************************************/
	/* The printing message for a declaration statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST DECLARATION STATEMENT */
		/********************************************/
		System.out.print("AST NODE DEC VAR STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR_DEC ... */
		/***********************************/
		if (varDec != null) varDec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nvarDec;\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
	}
}
