package IR.REG;

public class LocalReg extends Reg{
    public LocalReg(int id) {
        super(id);
    }
    @Override
    public String toString() {
        return "l" + getId();
    }

}
