package AST.EXP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;
import SYMBOL_TABLE.*;
import AST.*;
import AST.VAR.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import java.util.*;

public class AST_EXP_CALL extends AST_EXP
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_VAR var;
	public String funcName;
	
	public List<AST_EXP> params;
	public SYMBOL symbol;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_CALL(String funcName,AST_VAR var, List<AST_EXP> params)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		this.funcName = funcName;
		this.var = var;
		this.params = params;
	}
	public AST_EXP_CALL(String funcName,AST_VAR var)
	{this(funcName, var, Collections.emptyList());}
	public AST_EXP_CALL(String funcName, List<AST_EXP> params)
	{this(funcName, null, params);}
	public AST_EXP_CALL(String funcName){this(funcName, null, Collections.emptyList());}
	
	public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
	{
		if (var != null)
			var.semantPost(st);
		for (AST_EXP p : params) {
            p.semantPost(st);
        }
		if (var != null) {
            TYPE c = var.t;
            if (!c.isClass()) {
                throw new SYMBOL_TABLE_ERROR(this);
            } else {
                symbol = ((TYPE_CLASS) c).searchMethod(funcName);
                if (symbol == null) {
                    throw new SYMBOL_TABLE_ERROR(this);
                } else
                    check_call(symbol.getFunc());
            }
        } else {
            
            TYPE_CLASS innerClass = st.ownerClass;
            if (innerClass != null) {
                symbol = innerClass.searchMethod(funcName);
            }
            
            if (symbol == null) {
                symbol = st.findMethod(funcName, true);
            }

            if (symbol == null) {
                throw new SYMBOL_TABLE_ERROR(this);
            } else check_call(symbol.getFunc());
        }
	}
	
	private void check_call(TYPE_FUNCTION f) throws SYMBOL_TABLE_ERROR {
        if (f.params.size() != params.size()) {
            throw new SYMBOL_TABLE_ERROR(this);
        } else {
            for (int i = 0; i < params.size(); i++) {
                TYPE expected = f.params.get(i);
                AST_EXP found = params.get(i);

                if (found.t == TYPE_NIL.instance) {
                    if (expected.isClass() || expected.isArray()) {
                        continue;
                    }

                    throw new SYMBOL_TABLE_ERROR(this);
                } else if (!expected.canAssign(found.t)) {
                    throw new SYMBOL_TABLE_ERROR(this);
                }
            }

            t = f.returnType;
        }
    }
	
	
	public Reg irMe(Manager context)
	{
		Reg instance = ((var != null) ? var.irMe(context) : null);
        if (instance == null && symbol.isBounded()) {
            
            instance = CurrentReg.instance;
        }

        if (instance != null) {
            context.checkNotNull(instance);
            context.command(new IRCommand_Push(instance));
        }

        
        for (AST_EXP p : params) {
            Reg param = p.irMe(context);
            context.command(new IRCommand_Push(param));
        }

        if (symbol.isBounded() && instance != null) {
            
            int ind = context.getMethodOffsetInVtable(symbol);
            Reg t1 = context.newRegister();
            context.command(new IRCommand_BinopRC(t1, instance, Operation.Plus, Manager.VTABLE_OFFSET_IN_OBJECT));
            Reg t2 = context.newRegister();
            context.command(new IRCommand_Load(t2, t1)); 
            context.command(new IRCommand_BinopRC(t2, t2, Operation.Plus, ind)); 
            Reg t3 = context.newRegister();
            context.command(new IRCommand_Load(t3, t2)); 
            context.command(new IRCommand_Call_Reg(t3));
        } else {
            
            IRCommand_Label label = context.functionLabelFor(symbol);
            context.command(new IRCommand_Call(label));
        }
        Reg temp = context.newRegister();
        context.command(new IRCommand_Pop(temp));
        return temp;
	}

}
