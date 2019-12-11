package AST;

public class AST_FUNC_DEC extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String type;
	public String name;
	public AST_ID_LIST ids;
	public AST_STMT_LIST stmts;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNC_DEC(String type,String name,AST_ID_LIST ids,AST_STMT_LIST stmts)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== funcDec -> ID( %s )",type);
		System.out.format(" ID( %s ) LPAREN",name);
		if (ids != null) System.out.format(" ids ");
		System.out.format("RPAREN stmts");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.name = name;
		this.stmts = stmts;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.format("AST NODE FUNC DEC\n");

		/*************************************/
		/* RECURSIVELY PRINT NAME + STMTS ... */
		/*************************************/
//		if (type != null) type.PrintMe();
//		if (name != null) name.PrintMe();
		if (ids != null) ids.PrintMe();
		if (stmts != null) stmts.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC\nDEC\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
//		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
//		if (name != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,name.SerialNumber);
		if (ids != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ids.SerialNumber);
		if (stmts != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmts.SerialNumber);
	}
	
}
