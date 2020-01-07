package SYM_TABLE;

private TYPE_DEC_LIST defaults = null; // TODO: put int, string, lib-funcs

public class SYM_TABLE {
    private TYPE_CLASS extending = null; // If currently defining a class that extends, this should point to father
    private TYPE returnType = null; // If currently defining a function, this should hold its return type
    // Starting with an empty global scope:
    private SYM_TABLE_SCOPE_LIST scopes = SYM_TABLE_SCOPE_LIST(new SYM_TABLE_SCOPE(null,null), null);

    public TYPE_LIST getConstructedTypeList () {
        return scopes.head.types;
    }

    public SCOPE_KIND getKind () {
        if (scopes.head == null) {
            return null;
        }
        return (scopes.head.getKind());
    }

    public boolean isGlobal () {
        return (scopes.tail == null);
    }

    public TYPE getReturnType () { return returnType; }

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
        } else {
            scopes.head.add(t);
        }
    }

    public void open(SYM_TABLE_SCOPE init, TYPE_CLASS ec, TYPE rt) {
        // init may hold parameters of function
        // ec may hold father class
        // rt may hold return type
        scopes = new SYM_TABLE_SCOPE_LIST(init, scopes);
        extending = ec;
        returnType = rt;
    }

    public void close() {
        scopes = scopes.tail;
        extending = null;
        returnType = null;
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
