/***********/
/* PACKAGE */
/***********/
package AST.STMT;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import AST.*;
import AST.DEC.*;
import IR.*;
import IR.COMMAND.*;
import IR.REG.*;
import SYMBOL_TABLE.*;

public abstract class AST_STMT extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

    public Reg irMe(Manager context) {return null;}
	
}
