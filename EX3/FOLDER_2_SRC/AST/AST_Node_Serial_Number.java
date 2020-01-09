package AST;

import java.io.PrintWriter;

public class AST_Node_Serial_Number
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	private static int line;
	private static PrintWriter writer;

    // Debug info accu
    public String debug_string = "";
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static AST_Node_Serial_Number instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected AST_Node_Serial_Number() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	private static AST_Node_Serial_Number getInstance()
	{
		if (instance == null)
		{
			instance = new AST_Node_Serial_Number();
			instance.SerialNumber = 0;
			
		}
		return instance;
	}

	/**********************************/
	/* GET A UNIQUE SERIAL NUMBER ... */
	/**********************************/
	public int get()
	{
		return SerialNumber++;
	}

	/**********************************/
	/* GET A UNIQUE SERIAL NUMBER ... */
	/**********************************/
	public static int getFresh()
	{
		return AST_Node_Serial_Number.getInstance().get();
	}
	
	/**/
	public static void setLine(int line)
	{
		AST_Node_Serial_Number.line = line;
	}
	public static void setWriter(PrintWriter writer)
	{
		AST_Node_Serial_Number.writer = writer;
	}
	
	public static int getLine()
	{
		return AST_Node_Serial_Number.line;
	}

    public void addDebugInfo(String debug_add) {
	this.debug_string = this.debug_string + "\\n" + debug_add;
    }
    
    public static void exit_on_error(int line, String error_string)
	{
	    // Debuging on:
		writer.format("ERROR(%d)" + ": " + error_string + this.debug_string, line);
	    // Debuging off:
		// writer.format("ERROR(%d)", line);
		
		writer.close();
		System.exit(0);
	}
}
