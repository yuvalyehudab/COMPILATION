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

public class IRCommand_Call extends IRCommand
{
	public IRCommand_Label label;
	
	public IRCommand_Call(IRCommand_Label l)
	{
		this.label = l;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	/*public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().load(dst,var_name);
	}*/
}
