package AST;

public class AST_CLASS_DEC extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String name;
	public String parent;
	public AST_CFIELD_LIST cfields;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_LIST(String name,String parent,AST_CFIELD_LIST cfields)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (parent != null) {
			System.out.print("====================== classDec -> CLASS ID( %s )",name);
			System.out.print("EXTENDS ID( %s ) LBRACE cfields RBRACE\n",parent);
		}
		if (parent == null) {
			System.out.print("====================== classDec -> CLASS ID( %s )",name);
			System.out.print("LBRACE cfields RBRACE\n");
		}

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.parent = parent;
		this.cfields = cfields;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE CLASS DEC\n");

		/*************************************/
		/* RECURSIVELY PRINT PARENT + CFIELDS ... */
		/*************************************/
		if (name != null) name.PrintMe();
		if (parent != null) parent.PrintMe();
		if (cfields != null) cfields.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nDEC\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (name != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,name.SerialNumber);
		if (parent != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,parent.SerialNumber);
		if (cfields != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cfields.SerialNumber);
	}
	
}
