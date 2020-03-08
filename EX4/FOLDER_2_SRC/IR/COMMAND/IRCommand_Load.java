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

public class IRCommand_Load extends IRCommand
{
    public Reg source;
    public Reg dest;

    public IRCommand_Load(Reg dest, Reg source)
    {
	this.dest = dest;
	this.source = source;
	this.writes = new HashSet<>(Arrays.asList(dest));
	this.reads = new HashSet<>(Arrays.asList(source));
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
