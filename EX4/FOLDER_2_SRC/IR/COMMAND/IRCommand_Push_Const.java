/***********/
/* PACKAGE */
/***********/
package IR.COMMAND;

import IR.REG.*;

public class IRCommand_Push_Const extends IRCommand
{
	public int con;
	
	public IRCommand_Push_Const(int con) 
	{
		this.con = con;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	/*public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().load(dst,var_name);
	}*/
	
}
