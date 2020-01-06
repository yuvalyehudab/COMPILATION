package SYM_TABLE;

private TYPE_DEC_LIST defaults = null; // TODO: put int, string, lib-funcs

public class SYM_TABLE {
    private TYPE_CLASS extending = null; // If currently defining a class that extends, this should point to father
    private SYM_TABLE_SCOPE_LIST scopes = SYM_TABLE_SCOPE_LIST(null, null); // never null

    public SCOPE_KIND getKind () {
        if (scopes.head == null) {
            return GLOBAL;
        }
        return (scopes.head.getKind());
    }

    public boolean isGlobal () {
        return (scopes.tail == null);
    }

    public TYPE find(String name) {
        if (current != null) {
            // Look here in the current scope
            TYPE here = current.find(name);
            if (here != null) {
                // Found here in the current scope
                return here;
            }
        }
        if (previous != null) {
            // Look recursively in previous scopes
            return previous.find(name);
        }
        // Look in the defaults, if not there then nowhere
        return defaults.find(name);
    }

    public void enter(String name, TYPE t) {
        if (previous != null && t.isGlobal()) {
            // COMPILER BUG
        }
        // Check that it was not already declared in current scope
        if (current.find(name) != null) {
            // TODO: Code bug
        }
        // Check that there is no shadowing different types
        if (extending != null) {
            // Look if method/constant is already defined
            TYPE type_in_ancestors = extending.find(name);
            if (type_in_ancestors != null && !t.equals(type_in_ancestors)) {
                // TODO: Code bug
            }
        }
        current = SYM_TABLE_SCOPE(TYPE_DEC(name, t), current.list);
    }

    public void open(SCOPE_KIND k, TYPE_CLASS ec) {
        previous = SYM_TABLE_SCOPE_LIST(current, previous);
        current = SYM_TABLE_SCOPE(k, null);
        extending = ec;
    }

    public void close() {
        if (previous == null) {
            // COMPILER BUG
        }
        current = previous.head;
        previous = previous.tail;
    }
    
    public SYM_TABLE ()

// TODO: Wrap into a singleton instance

    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
/*    private static SYMBOL_TABLE instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
/*    protected SYMBOL_TABLE() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
/*    public static SYMBOL_TABLE getInstance()
    {
        if (instance == null)
        {
            instance = new SYMBOL_TABLE();

        }
    }
		return instance;

 */
}
