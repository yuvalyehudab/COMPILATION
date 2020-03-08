package AST.DEC;

import AST.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;

import java.util.*;


import static TYPES.SCOPE_BOUNDARY.Scope.*;

public class AST_DEC_CLASS extends AST_DEC
{
	
    private String father;
    
    private AST_DEC[] fields;
    private TYPE_CLASS repType;
	
    public AST_DEC_CLASS(String name, AST_DEC[] data_members, String father)
    {
	super(null, name);
		
	    SerialNumber = AST_Node_Serial_Number.getFresh();
	
	this.fields = data_members;
	this.father = father;
    }
	
    public AST_DEC_CLASS(String name, AST_DEC[] fields) {
        this(name, fields, null);
    }

    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {	
	st.beginScope(SCN, repType, null, name);
		
	SYMBOL_TABLE_ERROR headerException = null;
        AST_DEC brokenField = null;
        for (AST_DEC field : fields) {
            try {
                field.semantPre(st);
            } catch (SYMBOL_TABLE_ERROR e) {
                if (headerException == null) {
                    headerException = e;
                    brokenField = field;
                }
            }
        }
        st.endScope();

        st.beginScope(CLS, repType, null, "class " + name);
        for (AST_DEC field : fields) {
            if (headerException != null && brokenField == field) {
                throw headerException;
            }
            field.semantPost(st);
        }
        st.endScope();		
    }
	
    public void semantPre(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR {
        TYPE_CLASS father = null;
        if (this.father != null) {
            father = st.findTypeClass(this.father);
            if (father == null) {
		throw new SYMBOL_TABLE_ERROR(this);
            }
        }
        repType = new TYPE_CLASS(name, father);

        if (st.find(name) != null) {
	    throw new SYMBOL_TABLE_ERROR(this);
        }

        st.enter(name, repType, false,true, true);
    }
	
    public Reg irMe(Manager context) {
        context.loadClass(repType);
        int size = context.sizeOf(repType);

        IRCommand_Label c = context.constructorOf(repType);
        context.openScope(c.toString(), Collections.emptyList(), Manager.ScopeType.Function, false, false);

        context.label(c);
        context.command(new IRCommand_Func(c.toString(),0, 0));
        Reg allocSize = context.newRegister();
        context.command(new IRCommand_Const(allocSize, size));
        Reg r = context.malloc(allocSize);
        context.assignVTable(r, repType);

        Reg t1 = context.newRegister();

        internalCon(repType, context, r, t1);

        context.command(new IRCommand_Set(RetReg.instance, r));
        context.label(context.returnLabelForConstructor(repType));
        context.command(new IRCommand_Return());

        context.closeScope();

        context.openObjectScope(repType);

        IRCommand_Label internalLabel = context.internalConstructorOf(repType);
        context.label(internalLabel);
        context.command(new IRCommand_Func(internalLabel.toString(), 1, 0));
        context.openScope(internalLabel.toString(), Collections.emptyList(), Manager.ScopeType.Function, false, false);

        for (AST_DEC field : fields) {
            if (field instanceof AST_DEC_VAR) {
                field.irMe(context);
            }
        }
        context.label(context.returnLabelForInternalConstructor(repType));
        context.command(new IRCommand_Return());
        context.closeScope();

        for (AST_DEC field : fields) {
            if (!(field instanceof AST_DEC_VAR)) {
                field.irMe(context);
            }
        }

        context.closeObjectScope();

        return NonExistsRegister.instance;
    }

    private void internalCon(TYPE_CLASS current, Manager context, Reg reg, Reg temp) {
        if (current.father != null) {
            internalCon(current.father, context, reg, temp);
        }

        context.command(new IRCommand_Push(reg));
        context.command(new IRCommand_Call(context.internalConstructorOf(current)));
        context.command(new IRCommand_Pop(temp));
    }
}
	








