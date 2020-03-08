package AST.VAR;

import AST.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;

import java.util.function.*;

public class AST_VAR_FIELD extends AST_VAR
{
    public AST_VAR var;
    public String fieldName;
    public SYMBOL symbol;
    
    public AST_VAR_FIELD(AST_VAR var,String fieldName)
    {
	SerialNumber = AST_Node_Serial_Number.getFresh();

	this.var = var;
	this.fieldName = fieldName;
    }

	
    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {
	var.semantPost(st);
	if (!var.t.isClass())
	    throw new SYMBOL_TABLE_ERROR(this);
		
	symbol = ((TYPE_CLASS)var.t).searchField(fieldName);
	if (symbol == null)
	    throw new SYMBOL_TABLE_ERROR(this);
	this.t = symbol.type;	
    }
    public Reg irMe(Manager context)
    {
	Reg r = var.irMe(context);
	context.checkNotNull(r);
	int off = context.getFieldOffset(symbol);
	Reg t1 = context.newRegister();
	context.command(new IRCommand_BinopRC(t1, r, Operation.Plus, off));
	Reg t2 = context.newRegister();
	context.command(new IRCommand_Load(t2, t1));
	return t2;
    }
    public void ir_assign(Manager context, Supplier<Reg> data)
    {
	Reg r = var.irMe(context);
	context.checkNotNull(r);
	Reg c = data.get();
	int off = context.getFieldOffset(symbol);
	Reg t1 = context.newRegister();
	context.command(new IRCommand_BinopRC(t1, r, Operation.Plus, off));
	context.command(new IRCommand_Store(t1, c));
    }
}
