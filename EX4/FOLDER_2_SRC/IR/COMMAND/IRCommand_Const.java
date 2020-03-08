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

public class IRCommand_Const extends IRCommand
{
	
    public int value;
    public Reg dest;
	
    public IRCommand_Const(Reg dest, int value)
    {
	this.dest = dest;
	this.value = value;
	this.writes = new HashSet<>(Arrays.asList(dest));
    }
	
    /***************/
    /* MIPS me !!! */
    /***************/
    /*public void MIPSme()
      {
      sir_MIPS_a_lot.getInstance().li(t,value);
      }*/
    public boolean canOptimize() {
        return !dest.isGlobal();
    }
}
