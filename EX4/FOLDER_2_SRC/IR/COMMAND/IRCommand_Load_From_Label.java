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

public class IRCommand_Load_From_Label extends IRCommand
{
    public IRCommand_Label label;
    public Reg dest;

    public IRCommand_Load_From_Label(Reg dest, IRCommand_Label l)
    {
	this.dest = dest;
	this.label = l;
	this.writes = new HashSet<>(Arrays.asList(dest));
    }
	
    /***************/
    /* MIPS me !!! */
    /***************/
    /*public void MIPSme()
      {
      sir_MIPS_a_lot.getInstance().load(dst,var_name);
      }*/
    public boolean canOptimize()
    {
        return true;
    }
}
