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

public class IRCommand_Set extends IRCommand
{
	
    public Reg source;
    public Reg dest;

    public IRCommand_Set(Reg dest, Reg source)
    {
	this.dest = dest;
	this.source = source;
	this.reads = new HashSet<>(Arrays.asList(source));
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
