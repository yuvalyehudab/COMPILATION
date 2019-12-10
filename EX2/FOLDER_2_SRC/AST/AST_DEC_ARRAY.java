package AST;

public class AST_DEC_ARRAY extends AST_DEC
{
	/***************/
	/*  class id{} */
	/***************/
	public AST_ARRAY_DEC arrayDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_ARRAY(AST_ARRAY_DEC arrayDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> arrayDec SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.arrayDec = arrayDec;
	}

	/*********************************************************/
	/* The printing message for a array declaration statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST DECLARATION STATEMENT */
		/********************************************/
		System.out.print("AST NODE array DECLARE STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR_DEC ... */
		/***********************************/
		if (arrayDec != null) arrayDec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nARRAY name = name[]\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, arrayDec.SerialNumber);
	}
}
