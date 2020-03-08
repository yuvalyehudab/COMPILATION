package IR.REG;

public class ParamReg extends Reg{
    public ParamReg(int id) {
        super(id);
    }
    @Override
    public String toString() {
        return "p" + getId();
    }

}
