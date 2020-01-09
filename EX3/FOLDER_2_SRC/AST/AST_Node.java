package AST;

import java.io.*;

public class AST_Node
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

	public void report_error()
	{
	    AST_Node_Serial_Number.exit_on_error(lineNumber,"NA");
	}

	public void report_error(String error_string)
	{
	    AST_Node_Serial_Number.exit_on_error(lineNumber,error_string);
	}
}
