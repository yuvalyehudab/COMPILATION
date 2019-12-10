package AST;

public class AST_VAR_DEC_SIMPLE extends AST_DEC_VAR
{
	/************************/
	/* simple variable decl */
	/************************/
	public String type;
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_DEC_SIMPLE(String type, String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== ID ID( %s %s)\n", type, name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.name = name;
	}

	/**************************************************/
	/* The printing message for a simple var decl AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST SIMPLE VAR DEC */
		/**************************************/
		System.out.format("AST NODE SIMPLE VAR DEC( %s %s )\n", type, name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\nDEC\n(%s\n%s)", type, name));
	}
}
