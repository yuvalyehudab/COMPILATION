package AST;

public class AST_DEC_CLASS extends AST_DEC
{
	/***************/
	/*  class id{} */
	/***************/
	public AST_CLASS_DEC classDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC_CLASS(AST_CLASS_DEC classDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> classDec SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.classDec = classDec;
	}

	/*********************************************************/
	/* The printing message for a class declaration statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST DECLARATION STATEMENT */
		/********************************************/
		System.out.print("AST NODE class DECLARE STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR_DEC ... */
		/***********************************/
		if (classDec != null) classDec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nclass name{}\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, classDec.SerialNumber);
	}
}
