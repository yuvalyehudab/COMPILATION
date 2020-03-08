   
import java.io.*;
import java.util.*;

import AST.*;
import AST.DEC.*;
import AST.VAR.*;
import AST.EXP.*;
import AST.STMT.*;
import IR.*;
import IR.ANALYSIS.*;
import IR.COMMAND.*;
import IR.REG.*;
import TYPES.*;
import SYMBOL_TABLE.*;
import MIPS.*;

public class Main
{
    static public void main(String argv[])
    {
	Lexer l;
	Parser p;
	//Symbol s; - How should I use this????
	AST_PROG AST;
	String inputFilename = argv[0];
	String outputFilename = argv[1];
	
	try (/********************************/
	     /* [1] Initialize a file reader */
	     /********************************/
	     FileReader file_reader = new FileReader(inputFilename);
	     /********************************/
	     /* [2] Initialize a file writer */
	     /********************************/
	     PrintWriter file_writer = new PrintWriter(outputFilename))
	    {
			
			
		/******************************/
		/* [3] Initialize a new lexer */
		/******************************/
		l = new Lexer(file_reader);
			
		/*******************************/
		/* [4] Initialize a new parser */
		/*******************************/
		p = new Parser(l);

		/***********************************/
		/* [5] 3 ... 2 ... 1 ... Parse !!! */
		/***********************************/
		AST = (AST_PROG) p.parse().value;
			
		/*************************/
		/* [6] Print the AST ... */
		/*************************/
		//AST.PrintMe();

		/**************************/
		/* [7] semant the AST ... */
		/**************************/
		AST.semantPost(SYMBOL_TABLE.getInstance());

		/**********************/
		/* [8] IR the AST ... */
		/**********************/
		Manager c = new Manager();
		AST.irMe(c);
			
		/***********************/
		/* [9] MIPS the IR ... */
		/***********************/
		//IR.getInstance().MIPSme();

		/**************************************/
		/* [10] Finalize AST GRAPHIZ DOT file */
		/**************************************/
		//AST_GRAPHVIZ.getInstance().finalizeFile();			

		/***************************/
		/* [11] Finalize MIPS file */
		/***************************/
		//sir_MIPS_a_lot.getInstance().finalizeFile();
			
		MIPS m = new MIPS(c.strings, c.getVTables(), c.getGlobals(), c.commands, file_writer);
		m.writeFile();
		// file_writer.write(m.to_str());
		//System.out.print(m.to_str());
		/**************************/
		/* [12] Close output file */
		/**************************/
	    }
			     
	catch (Exception e)
	    {
		//file_writer.write("ERROR");
		e.printStackTrace();
	    }
	// catch (lexException e)
	// {
	// 	file_writer.write("ERROR(" + l.getLine() + ")\n");
	// }
	// catch (parseException e)
	// {
	// 	file_writer.write("ERROR(" + e.getNode().lineNumber + ")\n");
	// }
    }
}


