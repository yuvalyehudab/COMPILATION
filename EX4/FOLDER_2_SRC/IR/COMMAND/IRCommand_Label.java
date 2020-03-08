/***********/
/* PACKAGE */
/***********/
package IR.COMMAND;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IRCommand_Label extends IRCommand
{
    public String name;
    public boolean isFuncInit;
	
    public IRCommand_Label(String name, boolean isFuncInit) {
	this.name = name;
	this.isFuncInit = isFuncInit;
    }

    public IRCommand_Label(String name) {
	this(name, false);
    }

    public boolean isStartingLabel() {
	return isFuncInit;
    }

    public IRCommand_Label startingLabel() {
        isFuncInit = true;
        return this;
    }
	
    public boolean equals(Object o) {
        return o instanceof IRCommand_Label && ((IRCommand_Label) o).name.equals(name);
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
	return name;
    }
}
