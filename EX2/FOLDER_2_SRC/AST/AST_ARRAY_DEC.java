package AST;

public class AST_ARRAY_DEC extends AST_Node
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public String thing;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ARRAY_DEC(String name,String thing)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== arrayDec -> IDname( %s )",name);
		System.out.format("IDthing( %s )\n",thing);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.thing = thing;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE ARRAY DEC( %s , ",name);
		System.out.format("%s )\n",thing);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ARRAY\nDEC\n(%s\n%s)",name, thing));
	}
}
