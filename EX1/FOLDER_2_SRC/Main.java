   
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		StringBuffer token_string = new StringBuffer("");
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF && s.sym != TokenNames.error)
			{
				/************************/
				/* [6] Print to console */
				/************************/
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]:");
				System.out.print(s.value);
				System.out.print("\n");
				
				/*********************/
				/* [7] Print to file */
				/*********************/
				token_string.append(l.getLine());
				token_string.append(": ");
				token_string.append(s.value);
				token_string.append("\n");
				/*********************/
				file_writer.print(l.getLine());
				file_writer.print(": ");
				file_writer.print(s.value);
				file_writer.print("\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			if (s.sym == TokenNames.error)
			{
				file_writer.print("ERROR");
			}
			else
			{
				file_writer.print(token_string);
			}
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


