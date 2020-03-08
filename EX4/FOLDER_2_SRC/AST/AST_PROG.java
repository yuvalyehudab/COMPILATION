package AST;

import AST.DEC.*;
import IR.COMMAND.*;
import IR.REG.*;
import SYMBOL_TABLE.*;
import IR.*;

import java.util.*;

public class AST_PROG extends AST_Node
{
	public AST_DEC[] decs;
	public List<SYMBOL> globals;
	
	public AST_PROG(AST_DEC[] decs){this.decs = decs;}
	
	public void PrintMe()
	{
		/*************************************************/
		/* AST NODE TYPE = AST NODE FUNCTION DECLARATION */
		/*************************************************/
		System.out.format("PROG\n");

		/***************************************/
		/* RECURSIVELY PRINT  */
		/***************************************/
		for (AST_DEC d : decs) {
            d.PrintMe();
        }
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNC()\n"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		for (AST_DEC d : decs) {
            AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,d.SerialNumber);
        }
	}
	
	public void semantPost(SYMBOL_TABLE table) throws SYMBOL_TABLE_ERROR
	{
		for (AST_DEC d : decs)
		{
			d.semantPre(table);
			d.semantPost(table);
		}
		globals = table.globalSymbols;
	}

	public Reg irMe(Manager context) {
        context.openScope("global", globals, Manager.ScopeType.Global, false, false);
        for (AST_DEC d : decs) {
                d.irMe(context);
        }

        IRCommand_Label label = Manager.BASIC_FUNCTION_MAIN;
        context.label(label);
        context.command(new IRCommand_Func("compiler_main",0, 0));
        Reg temp = context.newRegister();
        for (IRCommand_Label preMainFunction : context.getPreMainFunctions()) {
            context.command(new IRCommand_Call(preMainFunction));
            context.command(new IRCommand_Pop(temp));
        }
        try {
            context.functionLabelFor(Manager.MAIN_SYMBOL);
        } catch (IllegalArgumentException e) {
            System.out.println("[Error] main not found");
            throw e;
        }

        context.command(new IRCommand_Call(context.functionLabelFor(Manager.MAIN_SYMBOL)));
        context.command(new IRCommand_Pop(temp));
        context.exit();

        // not closing the scope so global variables will be saved
        return NonExistsRegister.instance;
    }
	
}
