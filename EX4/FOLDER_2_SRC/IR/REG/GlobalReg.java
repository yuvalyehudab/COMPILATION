package IR.REG;

public class GlobalReg extends Reg {
    public GlobalReg(int id) {
        super(id);
    }
    
    public String toString() {
        return "g" + getId();
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
