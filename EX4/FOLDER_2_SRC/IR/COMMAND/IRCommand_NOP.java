/***********/
/* PACKAGE */
/***********/
package IR.COMMAND;

public class IRCommand_NOP extends IRCommand {
    public IRCommand_NOP() {}

    public boolean canOptimize() {
        return true;
    }
}
