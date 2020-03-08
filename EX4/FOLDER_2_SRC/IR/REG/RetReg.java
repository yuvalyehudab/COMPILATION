package IR.REG;

public class RetReg extends Reg{
    public static final RetReg instance = new RetReg();
    private RetReg() {
        super(-2);
    }

    public String toString() {
        return "r0";
    }
}
