package AST;

import TYPES.*;
import SYM_TABLE.*;

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
		// Initialize pointers to symbol table and singletons
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		TYPE_INT type_int = TYPE_INT.getInstance();
		TYPE_STRING type_string = TYPE_STRING.getInstance();

		TYPE t1 = left.SemantMe();
		TYPE t2 = right.SemantMe();

		boolean areInt    = (t1 ==    type_int && t1 == t2);
		boolean areString = (t1 == type_string && t1 == t2);

		if (OP == 3) { // =
			// Booleans are represented by integers, so return this if all is well
			TYPE_INT ok = type_int;
			// Handling the nil case
			if (t1.kind == KIND.NIL || t2.kind == KIND.NIL) {
				if (t1.kind == KIND.NIL && t2.kind == KIND.NIL) {
					// Comparing nil with nil is ok
					return ok;
				}
				if (t1.kind == KIND.CLASS || t2.kind == KIND.CLASS || t1.kind == KIND.ARRAY || t2.kind == KIND.ARRAY) {
    				        // Comparing nil with class or array is ok
				        return ok;
				}
				// TODO: Code bug -- comparing nil with something other than nil, class, array
			}

			// Handling the class case
			if (t1.kind == KIND.CLASS || t2.kind == KIND.CLASS) {
				if (((TYPE_CLASS)t1).isAncestor(t2.name) || ((TYPE_CLASS)t2).isAncestor(t1.name)) {
					return ok;
				}
				// TODO: Code bug -- one is a class and the other is not ancestor or decendent
			}

			// Any other case must have strictly equal types
			// No need to check t.isTypeName() because these are expression
			if (t1.name.equals(t2.name)) {
				return ok;
			}
			// TODO: Code bug -- mismatching types of some disallowed kind
		}

		if ((OP == 2 || OP == 6) && areInt) { // ><
			return type_int;
		}

		if ((OP == 1 || OP == 4 || OP == 5) && areInt) { // *-/
			return type_int;
		}

		if (OP == 0) { // +
			if (areInt)		{ return type_int; 		}
			if (areString) 	{ return type_string; 	}
		}
		// TODO: Code bug -- types do not match operation
		return null;
	}

}
