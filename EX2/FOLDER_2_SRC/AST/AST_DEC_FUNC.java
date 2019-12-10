package AST;

public class AST_DEC_FUNC extends AST_DEC
{
	/***************/
	/*  type id(){} */
	/***************/
	public AST_FUNC_DEC varDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_FUNC(AST_FUNC_DEC funcDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> funcDec SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.funcDec = funcDec;
	}

	/*********************************************************/
	/* The printing message for a declaration statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST DECLARATION STATEMENT */
		/********************************************/
		System.out.print("AST NODE function DECLARE STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR_DEC ... */
		/***********************************/
		if (funcDec != null) funcDec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\ntype func(){};\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, funcDec.SerialNumber);
	}
}
