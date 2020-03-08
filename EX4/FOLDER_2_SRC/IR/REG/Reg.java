package IR.REG;

public abstract class Reg{
    private final int id;

    Reg(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isTemp() {
        return false;
    }
    public boolean isGlobal() {
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return toString().equals(o.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
