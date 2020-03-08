package IR.REG;

public class NonExistsRegister extends Reg{
    public static final NonExistsRegister instance = new NonExistsRegister();
    private NonExistsRegister() {
        super(-1);
    }

    public int getId() {
        throw new IllegalArgumentException("do not use");
    }
}
