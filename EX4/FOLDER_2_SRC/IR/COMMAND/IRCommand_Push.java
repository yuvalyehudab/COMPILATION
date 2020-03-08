/***********/
/* PACKAGE */
/***********/
package IR.COMMAND;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import IR.REG.*;

public class IRCommand_Push extends IRCommand
{
	public Reg source;
	public IRCommand_Push(Reg source) 
	{
		this.source = source;
		this.reads = new HashSet<>(Arrays.asList(source));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	/*public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().load(dst,var_name);
	}*/
	
}
