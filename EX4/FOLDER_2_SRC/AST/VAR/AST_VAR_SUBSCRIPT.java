package AST.VAR;

import AST.*;
import AST.EXP.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;


import java.util.function.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
    public AST_VAR var;
    public AST_EXP subscript;
	
    public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript)
    {
	SerialNumber = AST_Node_Serial_Number.getFresh();

	this.var = var;
	this.subscript = subscript;
    }

    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {
	var.semantPost(st);
        subscript.semantPost(st);
	if (!var.t.isArray() || subscript.t != TYPE_INT.instance)
	    throw new SYMBOL_TABLE_ERROR(this);
        this.t = ((TYPE_ARR) var.t).arrT;
    }

    public Reg irMe(Manager context)
    {
	Reg r = var.irMe(context);
	context.checkNotNullForArray(r);
	Reg i = subscript.irMe(context);
	context.checkLength(r,i);
	Reg t1 = context.newRegister();
	context.command(new IRCommand_BinopRC(t1, i, Operation.Times, Manager.BYTE_COUNT));
        context.command(new IRCommand_Binop(t1, i, Operation.Plus, t1));
        context.command(new IRCommand_BinopRC(t1, t1, Operation.Plus, Manager.ARR_DATA_INITIAL_OFFSET));
	
	Reg t2 = context.newRegister();
	context.command(new IRCommand_Load(t2, t1));
	return t2;
    }

    public void ir_assign(Manager context, Supplier<Reg> data)
    {
	Reg r = var.irMe(context);
	context.checkNotNullForArray(r);
	Reg i = subscript.irMe(context);
	context.checkLength(r,i);
	Reg c = data.get();
	Reg t1 = context.newRegister();
	context.command(new IRCommand_BinopRC(t1, i, Operation.Times, Manager.BYTE_COUNT));
        context.command(new IRCommand_Binop(t1, i, Operation.Plus, t1));
        context.command(new IRCommand_BinopRC(t1, t1, Operation.Plus, Manager.ARR_DATA_INITIAL_OFFSET));
	context.command(new IRCommand_Store(t1, c));

    }

}
