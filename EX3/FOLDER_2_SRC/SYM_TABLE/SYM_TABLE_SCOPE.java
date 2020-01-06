package SYM_TABLE;

public class SYM_TABLE_SCOPE {
    private SCOPE_KIND kind;
    private TYPE_LIST types;

    public SCOPE_KIND getKind() {
        return kind;
    }

    public void setKind(SCOPE_KIND kind) {
        this.kind = kind;
    }

    public TYPE find (String name) {
        return types.find(name);
    }

    public SYM_TABLE_SCOPE(SCOPE_KIND kind, TYPE_LIST types) {
        this.kind = kind;
        this.types = types;
    }
}
