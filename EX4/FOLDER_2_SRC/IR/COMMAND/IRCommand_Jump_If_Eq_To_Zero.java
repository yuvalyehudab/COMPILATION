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


public class IRCommand_Jump_If_Eq_To_Zero extends IRCommand
{
    public Reg cond;
    public IRCommand_Label label;
	
    public IRCommand_Jump_If_Eq_To_Zero(Reg cond, IRCommand_Label l)
    {
	this.cond = cond;
	this.label = l;
	this.reads = new HashSet<>(Arrays.asList(cond));
    }
	
    /***************/
    /* MIPS me !!! */
    /***************/
    /*public void MIPSme()
      {
      sir_MIPS_a_lot.getInstance().beqz(t,label_name);
      }*/
}
