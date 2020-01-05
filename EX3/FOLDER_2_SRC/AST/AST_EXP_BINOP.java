package AST;

import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
		lineNumber = AST_Node_Serial_Number.getLine();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "*";}
		if (OP == 2) {sOP = "<";}
		if (OP == 3) {sOP = "=";}
		if (OP == 4) {sOP = "-";}
		if (OP == 5) {sOP = "/";}
		if (OP == 6) {sOP = ">";}

		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		/*System.out.print("AST NODE BINOP EXP\n");
		System.out.format("BINOP EXP(%s)\n",sOP);*/

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}
	public TYPE SemantMe()
	{
		TYPE t1 = null;
		TYPE t2 = null;
		
		if (left  != null) t1 = left.SemantMe();
		if (right != null) t2 = right.SemantMe();

		// TODO: What if one of them is null?
		// 		 Behavior depends on what failing means...

		boolean areEqual  =  t1.equals(t2);
		boolean areInt    = (t1 ==    TYPE_INT.getInstance());
		boolean areString = (t1 == TYPE_String.getInstance());

		if (OP == 3 && areEqual) { // =
			return TYPE_INT;
		}

		if ((OP == 2 || OP == 6) && areInt) { // ><
			return TYPE_INT;
		}

		if ((OP == 1 || OP == 4 || OP == 5) && areInt) { // *-/
			return TYPE_INT;
		}

		if (OP == 0) { // +
			if (areInt)		{ return TYPE_INT; 		}
			if (areString) 	{ return TYPE_STRING; 	}
		}

		// TODO: Failing should return null or throw exception?
		System.exit(0);
		return null;
	}

}
