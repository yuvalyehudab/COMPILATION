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


public class IRCommand_BinopRC extends IRCommand
{
    public Reg r;
    public int k;
    public Operation op;
    public Reg dest;

    public IRCommand_BinopRC(Reg dest,Reg r,Operation op, int k)
    {
	this.dest = dest;
	this.r = r;
	this.k = k;
	this.op = op;
	this.reads = new HashSet<>(Arrays.asList(r));
	this.writes = new HashSet<>(Arrays.asList(dest));
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    /*public void MIPSme()
      {
      sir_MIPS_a_lot.getInstance().add(dest,t1,t2);
      }*/
    public boolean canOptimize() {
        return !dest.isGlobal();
    }
}
