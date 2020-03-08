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

public class IRCommand_Goto extends IRCommand
{
    public IRCommand_Label label;

    public IRCommand_Goto(IRCommand_Label l)
    {
	this.label = l;
    }
}
