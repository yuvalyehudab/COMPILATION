package SYM_TABLE;

private TYPE_DEC_LIST defaults = null; // TODO: put int, string, lib-funcs

public class SYM_TABLE {
    private TYPE_CLASS extending = null; // If currently defining a class that extends, this should point to father
    private SYM_TABLE_SCOPE_LIST scopes = SYM_TABLE_SCOPE_LIST(null, null); // never null

    public TYPE_LIST getConstructedTypeList () {
        return scopes.head.types;
    }

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
        if (scopes.head != null) {
            // Look here in the current scope
            TYPE here = scopes.head.find(name);
            if (here != null) {
                // Found here in the current scope
                return here;
            }
        }
        if (scopes.tail != null) {
            // Look recursively in previous scopes
            return scopes.tail.find(name);
        }
        // Look in the defaults, if not there then nowhere
        return defaults.find(name);
    }

    public void enter(TYPE t) {
        if (scopes.tail != null && t.isGlobal()) {
            // COMPILER BUG
        }
        // Check that there is no shadowing different types
        if (extending != null) {
            // Look if method/constant is already defined
            TYPE type_in_ancestors = extending.find(name);
            if (type_in_ancestors != null) {
                // Is defined, now check if shadowing is legit
                if (!t.equals(type_in_ancestors)) {
                    // TODO: Code bug -- shadowing of different type
                }
                // Override case, no need to enter this symbol again
            }
        }
        current.add(t);
    }

    public void open(SYM_TABLE_SCOPE init, TYPE_CLASS ec) {
        // init may holds parameters of function
        // ec may hold father class
        scopes = new SYM_TABLE_SCOPE_LIST(init, scopes);
        extending = ec;
    }

    public void close() {
        if (previous == null) {
            // COMPILER BUG
        }
        current = previous.head;
        previous = previous.tail;
        extending = null;
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