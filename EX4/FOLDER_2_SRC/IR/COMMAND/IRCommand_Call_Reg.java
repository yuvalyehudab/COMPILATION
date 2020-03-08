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

public class IRCommand_Call_Reg extends IRCommand
{
	public Reg func;
	public IRCommand_Call_Reg(Reg func) 
	{
		this.func = func;
		this.reads = new HashSet<>(Arrays.asList(func));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	/*public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().load(dst,var_name);
	}*/
	
}
