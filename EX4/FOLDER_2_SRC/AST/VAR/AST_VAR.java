package AST.VAR;

import AST.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import IR.REG.*;

import java.util.function.*;

public abstract class AST_VAR extends AST_Node
{
	
	public TYPE t;
	public abstract void ir_assign(Manager context, Supplier<Reg> data);
}
