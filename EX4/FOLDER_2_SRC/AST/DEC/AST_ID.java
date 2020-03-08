package AST.DEC;

import AST.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.COMMAND.*;
import IR.REG.*;

import java.util.*;

public class AST_ID extends AST_Node
{
    public String type;
    public String name;
    public TYPE t;
	
    public AST_ID(String type, String name)
    {
	SerialNumber = AST_Node_Serial_Number.getFresh();

	this.type = type;
	this.name = name;
    }

    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {

	t = st.findType(type);

	if (t == null)
	    throw new SYMBOL_TABLE_ERROR(this);
	if (t == TYPE_VOID.instance)
	    throw new SYMBOL_TABLE_ERROR(this);
	if (!AST_DEC_VAR.available(st, name))
	    throw new SYMBOL_TABLE_ERROR(this);
		
	st.enter(name, t, true, true);		
    }
	
    public Reg irMe()
    {
	return NonExistsRegister.instance;
    }
	
}
