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

public class IRCommand_Store extends IRCommand
{
	public Reg source;
	public Reg dest;
	
	public IRCommand_Store(Reg dest, Reg source)
	{
		this.dest = dest;
		this.source = source;
		this.reads = new HashSet<>(Arrays.asList(source, dest));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	/*public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().store(var_name,src);
	}*/
}
