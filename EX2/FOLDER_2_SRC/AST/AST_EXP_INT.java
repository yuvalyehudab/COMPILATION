package AST;

public class AST_EXP_INT extends AST_EXP
{
	public int value;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_INT(int value, boolean hasMinus)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (hasMinus) {
			System.out.format("====================== exp -> MINUS INT( %d )\n", value);
		}
		else {
			System.out.format("====================== exp -> INT( %d )\n", value);
		}

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		if (hasMinus) {
			System.out.format("AST NODE MINUS INT( %d )\n",value);
		}
		else {
			System.out.format("AST NODE INT( %d )\n",value);
		}

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (hasMinus) {
			AST_GRAPHVIZ.getInstance().logNode(
					SerialNumber,
					String.format("MINUS INT(%d)",value));
		}
		else {
			AST_GRAPHVIZ.getInstance().logNode(
					SerialNumber,
					String.format("INT(%d)",value));
		}

	}
}
