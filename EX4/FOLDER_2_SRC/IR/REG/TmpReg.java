package IR.REG;

public final class TmpReg extends Reg {
    public TmpReg(int id) {
        super(id);
    }

    public String toString() {
        return "t" + getId();
    }

    public boolean isTemp() {
        return true;
    }

}
