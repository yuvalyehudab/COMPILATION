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

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT[] body;
	public List<SYMBOL> locals;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT[] body)
	{
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
		st.beginScope(BLK, null, null, "while");

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
	}
	
	public Reg irMe(Manager context)
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		IRCommand_Label after = context.newLabel("after_while");
		IRCommand_Label cond_label = context.newLabel("while_cond");
	
		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		context.label(cond_label);
		Reg cond_reg = cond.irMe(context);

		/********************/
		/* [3] cond.irMe(); */
		/********************/
		context.command(new IRCommand_Jump_If_Eq_To_Zero(cond_reg, after));


		/*******************/
		/* [5] body.irMe() */
		/*******************/
		context.openScope("while_body", locals, Manager.ScopeType.Inner, false, false);
		for (AST_STMT s : body) {
            s.irMe(context);
        }
        context.closeScope();

		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		context.command(new IRCommand_Goto(cond_label));

		/**********************/
		/* [7] Loop end label */
		/**********************/
		context.label(after);

		/*******************/
		/* [8] return null */
		/*******************/
		return NonExistsRegister.instance;
	}
}
