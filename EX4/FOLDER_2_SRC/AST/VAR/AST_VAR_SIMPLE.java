package AST.VAR;

import IR.COMMAND.*;
import IR.REG.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import AST.*;


import java.util.*;
import java.util.function.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
    public String name;
    public SYMBOL symbol;

    public AST_VAR_SIMPLE(String name)
    {
	SerialNumber = AST_Node_Serial_Number.getFresh();

	this.name = name;
    }
	
    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {
	symbol = st.findField(name, false);
	if (symbol == null)
	    throw new SYMBOL_TABLE_ERROR(this);
	this.t = symbol.type;
    }
	
    public Reg irMe(Manager context)
    {
	if (symbol.isBounded())
	    {
		int off = context.getFieldOffset(symbol);
		Reg c = CurrentReg.instance;
		Reg t1 = context.newRegister();
		context.command(new IRCommand_BinopRC(t1, c, Operation.Plus, off));
		Reg t2 = context.newRegister();
		context.command(new IRCommand_Load(t2, t1));
		return t2;
	    }
	else
	    {
		Reg t = context.newRegister();
            context.command(new IRCommand_Set(t, context.registerFor(symbol)));
		return t;
	    }
	}
	public void ir_assign(Manager context, Supplier<Reg> data)
	{
	    Reg d = data.get();
	    if (symbol.isBounded()) {
		int off = context.getFieldOffset(symbol);
		Reg c = CurrentReg.instance;
		Reg t1 = context.newRegister();
		context.command(new IRCommand_BinopRC(t1, c, Operation.Plus, off));
		context.command(new IRCommand_Store(t1, d));
	    } else {
		context.command(new IRCommand_Set(context.registerFor(symbol), d));
	    }
	}
    }
