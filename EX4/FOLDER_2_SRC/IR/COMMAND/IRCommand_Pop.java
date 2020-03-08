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

public class IRCommand_Pop extends IRCommand
{
    public Reg dest;

    public IRCommand_Pop(Reg dest) 
    {
	this.dest = dest;
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
