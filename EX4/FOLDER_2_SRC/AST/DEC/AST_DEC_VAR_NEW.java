package AST.DEC;

import AST.*;
import AST.EXP.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;


public class AST_DEC_VAR_NEW extends AST_DEC_VAR {
    
    private AST_NEW_EXP newExp;

    public TYPE t;
    private SYMBOL symbol;
    private SYMBOL enclosingFunction;

    public AST_DEC_VAR_NEW(String type, String name, AST_NEW_EXP newExp) {
        super(type, name);
        this.newExp = newExp;
    }

    public void semantPost(SYMBOL_TABLE st) {} // must override abstract defn
    
    public void semantPre(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR {
        t = st.findType(type);
        if (t == null || t == TYPE_VOID.instance) {
	    throw new SYMBOL_TABLE_ERROR(this);
        }

        newExp.semantPost(st);
        if (!t.canAssign(newExp.t)) {
	    throw new SYMBOL_TABLE_ERROR(this);
        } else if (st.ownerFunc == null && st.ownerClass != null) {
	    throw new SYMBOL_TABLE_ERROR(this);
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
            Reg expIR = newExp.irMe(context);

            int off = context.getFieldOffset(symbol);
            Reg c = CurrentReg.instance;
            Reg t1 = context.newRegister();
            context.command(new IRCommand_BinopRC(t1, c, Operation.Plus, off));
            context.command(new IRCommand_Store(t1, expIR));
        } else if (enclosingFunction != null) {
            Reg varIR = context.registerFor(symbol);
            Reg expIR = newExp.irMe(context);

            context.command(new IRCommand_Set(varIR, expIR));
        } else {
            IRCommand_Label preper = context.newLabel("prep_" + symbol.name).startingLabel();
            context.addPreMainFunction(preper);

            context.label(preper);
            context.command(new IRCommand_Func("init_" + symbol.name, 0, 0));
            Reg varIR = context.registerFor(symbol);
            Reg expIR = newExp.irMe(context);
            context.command(new IRCommand_Set(varIR, expIR));
            context.label(context.returnLabelForPreMainFunction(symbol.name));
            context.command(new IRCommand_Return());
        }
        return NonExistsRegister.instance;
    }
}
