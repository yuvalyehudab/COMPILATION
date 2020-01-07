package AST;

public class AST_EXP_VAR_SUBSCRIPT extends AST_EXP_VAR
{
	public AST_EXP_VAR var;
	public AST_EXP subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_SUBSCRIPT(AST_EXP_VAR var,AST_EXP subscript)
	{
		System.out.print("====================== var -> var [ exp ]\n");
		lineNumber = AST_Node_Serial_Number.getLine();
		
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
	}

	public TYPE SemantMe() {
		// Semant the var
		TYPE arrayT = var.SemantMe();
		if (!arrayT.isArray()) {
			// TODO: Code bug -- accessing a non-array
		}
		TYPE indexT = subscript.SemantMe();
		if (indexT != TYPE_INT.getInstance()) {
			// TODO: Code bug -- index is not an int
		}

		return arrayT.memberT;
	}
}
