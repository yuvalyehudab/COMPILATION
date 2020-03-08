package IR.REG;


public class CurrentReg extends Reg {
    public static final CurrentReg instance = new CurrentReg();
    private CurrentReg() {
        super(-2);
    }

    @Override
    public String toString() {
        return "current";
    }
}
