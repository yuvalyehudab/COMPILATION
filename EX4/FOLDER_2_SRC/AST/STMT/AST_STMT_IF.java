/***********/
/* PACKAGE */
/***********/
package AST.STMT;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import AST.*;
import AST.DEC.*;
import AST.VAR.*;
import AST.EXP.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;
import SYMBOL_TABLE.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import static TYPES.SCOPE_BOUNDARY.Scope.*;
import java.util.*;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT[] body;
	public SYMBOL symbol;
	public List<SYMBOL> locals;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond,AST_STMT[] body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.cond = cond;
		this.body = body;
	}

	public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
	{
		/****************************/
		/* [0] semant the Condition */
		/****************************/
		cond.semantPost(st);
		if (!cond.t.isBool() )
		{
			throw new SYMBOL_TABLE_ERROR(this);
		}
		
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		st.beginScope(BLK, null, null, "if");

		/***************************/
		/* [2] semant Data Members */
		/***************************/
		for (AST_STMT s : body) {
            s.semantPost(st);
        }

		/*****************/
		/* [3] End Scope */
		/*****************/
		locals = st.endScope();

		
		symbol = st.ownerFunc;
	}
	public Reg irMe(Manager context)
	{
		Reg cond_reg = cond.irMe(context);
		IRCommand_Label after = context.newLabel("after_if");
		
        context.command(new IRCommand_Jump_If_Eq_To_Zero(cond_reg, after));
		context.openScope("if_body", locals, Manager.ScopeType.Inner, false, false);
		for (AST_STMT s : body) {
            s.irMe(context);
        }
        context.closeScope();

        context.label(after);
        return NonExistsRegister.instance;
	}
}
