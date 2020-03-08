/***********/
/* PACKAGE */
/***********/
package AST.EXP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import AST.*;
import IR.*;
import IR.ANALYSIS.*;
import IR.COMMAND.*;
import IR.REG.*;
import SYMBOL_TABLE.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

public class AST_EXP_BINOP extends AST_EXP
{
    public Op op;
    public AST_EXP left;
    public AST_EXP right;
    public boolean is_const = false;
	
    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_BINOP(AST_EXP left,AST_EXP right,Op op)
    {
	/******************************/
	/* SET A UNIQUE SERIAL NUMBER */
	/******************************/
	SerialNumber = AST_Node_Serial_Number.getFresh();

	/*******************************/
	/* COPY INPUT DATA MENBERS ... */
	/*******************************/
	this.left = left;
	this.right = right;
	this.op = op;
    }

    public enum Op
    {
	Plus("+", true), Minus("-", false), Times("*", true), Divide("/", false),
	LT("<", false), GT(">", false), EQ("=", true);

        String text;
        boolean is_symmetric;

        Op(String text, boolean is_symmetric) {
            this.text = text;
            this.is_symmetric = is_symmetric;
        }
    }
	
    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {
	left.semantPost(st);
	right.semantPost(st);
		
	if (left.is_const && right.is_const) is_const = true;
		
	if (op == Op.EQ)
	    {
		if (!left.t.canAssign(right.t) && !right.t.canAssign(left.t))
		    throw new SYMBOL_TABLE_ERROR(this);
		this.t = TYPE_INT.instance;
	    }
	else if (op != Op.Plus)
	    {
		if(left.t != TYPE_INT.instance && left.t != TYPE_ERROR.instance)
		    {
			throw new SYMBOL_TABLE_ERROR(this);
		    }
		else if (right.t != TYPE_INT.instance && right.t != TYPE_ERROR.instance)
		    {
			throw new SYMBOL_TABLE_ERROR(this);
		    }
		else
		    {
			this.t = TYPE_INT.instance;
		    }
	    }
	else
	    {
		if (left.t == TYPE_ERROR.instance && right.t == TYPE_ERROR.instance)
		    {
			this.t = TYPE_ERROR.instance;
		    }
		else if ((left.t == TYPE_INT.instance || left.t == TYPE_ERROR.instance) && (right.t == TYPE_INT.instance || right.t == TYPE_ERROR.instance))
		    {
			this.t = TYPE_INT.instance;
		    }
		else if ((left.t == TYPE_STRING.instance || left.t == TYPE_ERROR.instance) && (right.t == TYPE_STRING.instance || right.t == TYPE_ERROR.instance))
		    {
			this.t = TYPE_STRING.instance;
		    }
		else{
		    throw new SYMBOL_TABLE_ERROR(this);
		}
	    }
    }
    Object get_const_value()
    {
	try
	    {
		if (left.t == TYPE_STRING.instance)
		    {
			String left_str = (String) left.get_const_value(), right_str = (String) right.get_const_value();
			if (op == Op.Plus)
			    return left_str+right_str;
			else
			    return left_str.equals(right_str) ? 0 : 1;
		    }
		else
		    {
			Integer l = (Integer)left.get_const_value(), r = (Integer)right.get_const_value();
			if (l == null || r == null)
			    return null;
			switch (op){
			case Plus:
			    return safe(l+r);
			case Minus:
			    return safe(l-r);
			case Times:
			    return safe(l*r);
			case Divide:
			    return safe(l/r);
			case LT:
			    return l < r ? 1 : 0;
			case GT:
			    return l > r ? 1 : 0;
			case EQ:
			    return l.equals(r) ? 1 : 0;
			}
			return -1;
		    }
	    }
	catch (ArithmeticException e){
	    return null;
	}
    }
	
    public Reg irMe(Manager context)
    {
		
	if (left.is_const && right.is_const)
	    {
		Reg t = context.newRegister();
		if (left.t == TYPE_STRING.instance)
		    {
			if (op == Op.Plus)
			    {
				context.command(new IRCommand_Load_From_Label(t, context.labelForConstantString((String) get_const_value())));
				return t;
			    }
			else{
			    context.command(new IRCommand_Const(t, (Integer)get_const_value()));
			}
		    }
		else
		    {
			Integer v = (Integer)get_const_value();
			if (v == null)
			    context.command(new IRCommand_Call(Manager.BASIC_FUNCTION_ERROR_DIVISION_BY_ZERO));
			else
			    context.command(new IRCommand_Const(t, (Integer)get_const_value()));
		    }
		return t;
	    }
	if (left.is_const && op.is_symmetric && !TYPE_STRING.instance.equals(left.t))
	    {
		AST_EXP tmp = left;
		left = right;
		right = tmp;
	    }
	if (right.is_const && !TYPE_STRING.instance.equals(left.t))
	    {
		Reg left_reg = left.irMe(context);
		Reg tmp = context.newRegister();
		Object right_val = right.get_const_value();
		if(op == Op.Divide && right_val != null && (int) right_val == 0)
		    context.command(new IRCommand_Call(Manager.BASIC_FUNCTION_ERROR_DIVISION_BY_ZERO));
		else
		    context.command(new IRCommand_BinopRC(tmp, left_reg, Operation.fromAstOp(op), ((Integer)right.get_const_value())));
		return tmp;
	    }
	Reg left_reg = left.irMe(context);
	Reg right_reg = right.irMe(context);
	Reg tmp = context.newRegister();
	if (TYPE_STRING.instance.equals(left.t) && TYPE_STRING.instance.equals(right.t))
	    {
		if (op == Op.Plus){
		    context.checkNotNull(left_reg);
		    context.checkNotNull(right_reg);
		    context.command(new IRCommand_Binop(tmp, left_reg, Operation.Concat, right_reg));
		} else {
		    context.command(new IRCommand_Binop(tmp, left_reg, Operation.StrEQ, right_reg));
		}
	    }
	else
	    {
		if (op == Op.Divide)
		    context.command(new IRCommand_Jump_If_Eq_To_Zero(right_reg, Manager.BASIC_FUNCTION_ERROR_DIVISION_BY_ZERO));
		context.command(new IRCommand_Binop(tmp, left_reg, Operation.fromAstOp(op), right_reg));
	    }
	return tmp;
    }
    private int safe(int value) {
        if (value > Manager.MAX_INT)
            return Manager.MAX_INT;
        else if (value < Manager.MIN_INT) {
            return Manager.MIN_INT;
        } else {
            return value;
        }
    }
}
