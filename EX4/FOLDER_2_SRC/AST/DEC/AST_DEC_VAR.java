package AST.DEC;

import AST.*;
import IR.*;

import MIPS.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_DEC_VAR extends AST_DEC
{
    public static int localVariablesCounter = 0;

    public AST_DEC_VAR(String type,String name)
    {
	super(type, name);

	SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    public static boolean available(SYMBOL_TABLE st, String name) {
        if (st.findType(name) != null) {
            return false;
        } else if (st.ownerFunc != null) {
            return st.findInCurrentScope(name) == null;
        } else if (st.isInFunc()) {
            return st.findType(name) == null && st.findMethod(name, false) == null;
        }else if (st.ownerClass != null) {
            return st.ownerClass.searchField(name) == null
		&& st.ownerClass.searchMethod(name) == null
		&& st.findInCurrentScope(name) == null;
        } else {
            return st.find(name) == null;
        }
    }
}
