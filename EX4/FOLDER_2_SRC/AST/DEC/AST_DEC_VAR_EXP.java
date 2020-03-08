package AST.DEC;

import AST.*;
import AST.EXP.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;


public class AST_DEC_VAR_EXP extends AST_DEC_VAR
{

    private AST_EXP exp;

    public TYPE t;
    private SYMBOL symbol;
    private SYMBOL enclosingFunction;

    public AST_DEC_VAR_EXP(String type,String name) {
	super(type, name);
    }

    public AST_DEC_VAR_EXP(String type,String name, AST_EXP exp) {
	super(type, name);
	this.exp = exp;
    }

    public void semantPost(SYMBOL_TABLE st) {} // must override abstract defn

    public void semantPre(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR {
	t = st.findType(type);
	if (t == null || t == TYPE_VOID.instance) {
	    throw new SYMBOL_TABLE_ERROR(this);
	}

	if (exp != null) {
	    exp.semantPost(st);
	    if (!t.canAssign(exp.t)) {
		throw new SYMBOL_TABLE_ERROR(this);
	    } else if (st.ownerFunc == null && st.ownerClass != null) {
		if (!(exp instanceof AST_EXP_INT) && !(exp instanceof AST_EXP_STRING) && !(exp instanceof AST_EXP_NIL)) {
		    throw new SYMBOL_TABLE_ERROR(this);
		}
	    }
	}

	if (!available(st, name)) {
	    throw new SYMBOL_TABLE_ERROR(this);
	}

	st.enter(name, t, true);

	symbol = st.find(name);
	enclosingFunction = st.ownerFunc;
    }




    public Reg irMe(Manager context) {
	if (symbol.isBounded()) {
	    if (exp != null) {
		Reg expIR = exp.irMe(context);

		int off = context.getFieldOffset(symbol);
		Reg c = CurrentReg.instance;
		Reg t1 = context.newRegister();
		context.command(new IRCommand_BinopRC(t1, c, Operation.Plus, off));
		context.command(new IRCommand_Store(t1, expIR));
	    }
	} else if (enclosingFunction != null) {
	    if (exp != null) {
		Reg varIR = context.registerFor(symbol);
		Reg expIR = exp.irMe(context);

		context.command(new IRCommand_Set(varIR, expIR));
	    }
	} else if (exp != null){
	    IRCommand_Label preper = context.newLabel("prep_" + symbol.name).startingLabel();
	    context.addPreMainFunction(preper);

	    context.label(preper);
	    context.command(new IRCommand_Func("init_" + symbol.name, 0, 0));
	    Reg varIR = context.registerFor(symbol);
	    Reg expIR = exp.irMe(context);
	    context.command(new IRCommand_Set(varIR, expIR));
	    context.label(context.returnLabelForPreMainFunction(symbol.name));
	    context.command(new IRCommand_Return());
	}
	return NonExistsRegister.instance;
    }
}

