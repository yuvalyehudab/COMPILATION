/***********/
/* PACKAGE */
/***********/
package IR.COMMAND;

public class IRCommand_Func extends IRCommand
{
	public String name;
	public int paramsCount;
	public int localsCount;
	
	public IRCommand_Func(String name, int paramsCount ,int localsCount) 
	{
		this.name = name;
		this.paramsCount = paramsCount;
		this.localsCount = localsCount;
	
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	/*public void MIPSme()
	{
		sir_MIPS_a_lot.getInstance().load(dst,var_name);
	}*/
	
}
