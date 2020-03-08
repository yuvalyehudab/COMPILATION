package AST.DEC;

import AST.*;
import AST.STMT.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;


import java.util.*;
import java.util.stream.*;


import static TYPES.SCOPE_BOUNDARY.Scope.*;

public class AST_DEC_FUNC extends AST_DEC
{
    public List<AST_ID> params;
    public AST_STMT[] stmts;
    public SYMBOL symbol;
    public List<SYMBOL> locals;
	
    public AST_DEC_FUNC(String returnTypeName, String name, AST_STMT[] stmts, List<AST_ID> params)
    {
	super(returnTypeName, name);
	this.params = params;
	this.stmts = stmts;
	
	SerialNumber = AST_Node_Serial_Number.getFresh();
    }
    public AST_DEC_FUNC(String returnTypeName, String name, AST_STMT[] stmts)
    {
	this(returnTypeName, name, stmts, Collections.emptyList());
    }



    public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
    {
	st.beginScope(FNC, null, symbol, "func" + name);
	for(AST_ID p : params)
	    {
		st.enter(p.name, p.t, true, true);
	    }
	for(AST_STMT s: stmts)
	    {
		s.semantPost(st);
	    }
		
        Set<String> paramNames = params.stream().map(p -> p.name).collect(Collectors.toSet());
        locals = st.endScope().stream().filter(x -> !paramNames.contains(x.name)).collect(Collectors.toList());
    }

   

    public void semantPre(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR {
        TYPE returnType = st.findType(type);
        if (returnType == null) {
	    throw new SYMBOL_TABLE_ERROR(this);
        }

        TYPE_FUNCTION representingType = new TYPE_FUNCTION(name, returnType, new ArrayList<>(), st.ownerClass);

        SYMBOL_TABLE_ERROR deferredException = null;
        st.beginScope(FNC, null, null, "funcParams " + name);
        try {
            for (AST_ID p : params) {
                p.semantPost(st);
            }
            params.stream().map(node -> node.t).forEachOrdered(representingType.params::add);
        } catch (SYMBOL_TABLE_ERROR e) {
            deferredException = e;
        }
        st.endScope();

        if (st.ownerClass != null) {
            SYMBOL declaredFunc = st.ownerClass.searchMethodLocal(name);
            if (declaredFunc != null || st.findInCurrentScope(name) != null) {
		throw new SYMBOL_TABLE_ERROR(this);
            }
            declaredFunc = st.ownerClass.searchMethod(name);
            if (declaredFunc != null && !declaredFunc.getFunc().compareParamList(representingType)) {
		throw new SYMBOL_TABLE_ERROR(this);
            }

            if (st.ownerClass.searchField(name) != null) {
		throw new SYMBOL_TABLE_ERROR(this);
            }

            if (st.ownerClass.name.equals(name)) {
		throw new SYMBOL_TABLE_ERROR(this);
            }

        } else if (st.find(name) != null) {
	    throw new SYMBOL_TABLE_ERROR(this);
        }

        TYPE nameType = st.findType(name);
        if (nameType == TYPE_STRING.instance || nameType == TYPE_INT.instance || nameType == TYPE_VOID.instance) {
	    throw new SYMBOL_TABLE_ERROR(this);
        }

        if (deferredException != null) {
            throw deferredException;
        }

        st.enter(name, representingType);
        symbol = st.findMethod(name, true);
    }


    public Reg irMe(Manager context) {
        IRCommand_Func functionInfo = new IRCommand_Func(name, params.size() + (symbol.isBounded() ? 1 : 0), 0);
        context.openScope(symbol.toString(), params.stream().map(id -> new SYMBOL(id.name, id.t)).collect(Collectors.toList()), Manager.ScopeType.Function, true, symbol.isBounded());

        context.resetLoadedFieldsCounter();
        context.openScope(symbol.toString() + "_locals", locals, Manager.ScopeType.Inner, false, false);

        context.label(context.functionLabelFor(symbol));
        context.command(functionInfo);
        for (AST_STMT s : stmts) {
            s.irMe(context);
        }

        context.closeScope();
        context.closeScope();

        context.label(context.returnLabelFor(symbol));
        context.command(new IRCommand_Return());

        functionInfo.localsCount = context.getLoadedFieldsCount();
        return NonExistsRegister.instance;
    }
}
