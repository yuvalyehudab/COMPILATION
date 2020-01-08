package SYM_TABLE;

import TYPES.*;

public class SYM_TABLE_SCOPE {
    private SCOPE_KIND kind;
    public TYPE_LIST types;

    public SCOPE_KIND getKind() {
        return kind;
    }

    public TYPE find (String name) {
        if (types == null)
        {
            return null;
        }
        return types.find(name);
    }

    public SYM_TABLE_SCOPE(SCOPE_KIND kind, TYPE_LIST types) {
        this.kind = kind;
        this.types = types;
    }
    public void add (TYPE t)
    {
        this.types.tail = this.types;
        this.types.head = t;
    }

    public boolean isFunction() {
	return (this.kind == SCOPE_KIND.FUNCTION_SCOPE);
    }
}
