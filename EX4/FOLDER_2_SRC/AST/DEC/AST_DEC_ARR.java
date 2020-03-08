package AST.DEC;

import IR.*;
import IR.COMMAND.*;
import IR.REG.*;
import TYPES.*;
import SYMBOL_TABLE.*;


import java.util.*;

public class AST_DEC_ARR extends AST_DEC {
    private TYPE_ARR array;

    public AST_DEC_ARR(String type, String name) {
        super(type, name);
    }
    
    public void semantPost(SYMBOL_TABLE st) {} // must override abstract defn

    public void semantPre(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR {
        TYPE arrayType = st.findType(type);
        if (arrayType == null) {
	    throw new SYMBOL_TABLE_ERROR(this);
        } else if (arrayType == TYPE_VOID.instance) {
	    throw new SYMBOL_TABLE_ERROR(this);
        }

        if (st.find(name) != null) {
	    throw new SYMBOL_TABLE_ERROR(this);
        }
        array = new TYPE_ARR(name, arrayType);
        st.enter(name, array, false, true);
    }

    
    public Reg irMe(Manager context) {
        IRCommand_Label constructorLabel = context.constructorOf(array);

        context.openScope(constructorLabel.toString(), Collections.emptyList(), Manager.ScopeType.Function, false, false);
        context.label(constructorLabel);
        context.command(new IRCommand_Func(constructorLabel.toString() , 1, 0));
        Reg allocSize = context.newRegister();
        context.command(new IRCommand_BinopRC(allocSize, Manager.FIRST_FUNCTION_PARAMETER, Operation.Times, Manager.BYTE_COUNT));
        context.command(new IRCommand_BinopRC(allocSize, allocSize, Operation.Plus, Manager.ARR_DATA_INITIAL_OFFSET));
        Reg mallocResult = context.malloc(allocSize);
        Reg temp = context.newRegister();
        context.command(new IRCommand_BinopRC(temp, mallocResult, Operation.Plus, Manager.ARR_LENGTH_OFFSET));
        context.command(new IRCommand_Store(temp, Manager.FIRST_FUNCTION_PARAMETER));
        context.command(new IRCommand_Set(RetReg.instance, mallocResult));
        context.label(context.returnLabelForConstructor(array));
        context.command(new IRCommand_Return());
        context.closeScope();

        return NonExistsRegister.instance;
    }
}
