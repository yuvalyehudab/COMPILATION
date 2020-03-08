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


public class IRCommand_Binop extends IRCommand
{
    public Reg t1;
    public Reg t2;
    public Operation op;
    public Reg dest;

    public IRCommand_Binop(Reg dest,Reg t1,Operation op, Reg t2)
    {
	this.dest = dest;
	this.t1 = t1;
	this.t2 = t2;
	this.op = op;
	this.reads = new HashSet<>(Arrays.asList(t1, t2));
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
