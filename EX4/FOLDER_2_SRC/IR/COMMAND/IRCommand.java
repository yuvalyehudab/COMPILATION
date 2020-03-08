/***********/
/* PACKAGE */
/***********/
package IR.COMMAND;

/*******************/
/* GENERAL IMPORTS */
/*******************/

import IR.REG.*;
import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/

public abstract class IRCommand
{
	/*****************/
	/* Label Factory */
	/*****************/
	public Set<Reg> reads = Collections.emptySet();
	public Set<Reg> writes = Collections.emptySet();
	public IRCommand(){	}

	/***************/
	/* MIPS me !!! */
	/***************/
	/*public abstract void MIPSme();*/
	
	public boolean canOptimize() {
        return false;
    }
}
