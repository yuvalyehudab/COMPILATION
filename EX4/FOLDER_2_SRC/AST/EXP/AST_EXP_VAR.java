package AST.EXP;

import TYPES.*;
import AST.*;
import AST.VAR.*;
import IR.REG.*;
import IR.*;
import SYMBOL_TABLE.*;

public class AST_EXP_VAR extends AST_EXP
{
	public AST_VAR var;
	
	public AST_EXP_VAR(AST_VAR var)
	{
		this.var = var;
	}
	
	public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
	{
		var.semantPost(st);
		t = var.t;
	}
	public Reg irMe(Manager context){return var.irMe(context);}
}
