package AST;

import IR.*;
import IR.REG.*;
import SYMBOL_TABLE.*;
import TYPES.*;

import java.util.*;
import java.util.function.Consumer;

public abstract class AST_Node
{
    /*******************************************/
    /* The serial number is for debug purposes */
    /* In particular, it can help in creating  */
    /* a graphviz dot format of the AST ...    */
    /*******************************************/
    public int SerialNumber;
    public int lineNumber;
	
    /***********************************************/
    /* The default message for an unknown AST node */
    /***********************************************/
    public void PrintMe()
    {
	System.out.print("AST NODE UNKNOWN\n");
    }

    /*****************************************/
    /* The default IR action for an AST node */
    /*****************************************/
    public Reg irMe(Manager context)
    {
	return null;
    }
    public abstract void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR;
}
