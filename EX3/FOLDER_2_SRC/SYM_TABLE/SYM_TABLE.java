package SYM_TABLE;

import TYPES.*;

public class SYM_TABLE {
    

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public void debug_print(String str) { System.out.println(ANSI_GREEN + str + ANSI_RESET); }

    private TYPE_LIST defaults = new TYPE_LIST(TYPE_INT.getInstance(),
					       new TYPE_LIST(TYPE_STRING.getInstance(),
							     new TYPE_LIST(new TYPE_FUNCTION("PrintInt",null,new TYPE_LIST(new TYPE_VAR_DEC("int",""),null)),
									   new TYPE_LIST(new TYPE_FUNCTION("PrintString",null,new TYPE_LIST(new TYPE_VAR_DEC("string",""),null)),
											 new TYPE_LIST(new TYPE_FUNCTION("PrintTrace",null,null), null)
											 ))));
    private TYPE_CLASS extending = null; // If currently defining a class that extends, this should point to father
    private TYPE returnType = null; // If currently defining a function, this should hold its return type
    // Starting with an empty global scope:
    private SYM_TABLE_SCOPE_LIST scopes = new SYM_TABLE_SCOPE_LIST(new SYM_TABLE_SCOPE(null,null));

    public TYPE_LIST getConstructedTypeList () {
        return scopes.head.types;
    }

    public SCOPE_KIND getKind () {
        if (scopes.head == null) {
            return null;
        }
        return (scopes.head.getKind());
    }

    public TYPE_CLASS getExtending () {
	return extending;
    }

    public boolean isGlobal () {
        return (scopes.tail == null);
    }

    public boolean isFunctionScope () {
	return scopes.isFunction();
    }

    public TYPE getReturnType () { return returnType; }

    private TYPE find__(String name, boolean incl) {
        SYM_TABLE_SCOPE_LIST search = scopes;
        while (search != null && search.head != null)
        {
	    SYM_TABLE_SCOPE currentScope = search.head;
            TYPE requestedType = currentScope.find(name);
            if (requestedType != null) {
                return requestedType;
            }
	    if (incl && currentScope.getKind() == SCOPE_KIND.CLASS_SCOPE && extending != null) {
		// Now must look in ancestors
		requestedType = extending.find(name);
		if (requestedType != null) {
			return requestedType;
		    }
	    }
            search = search.tail;
        }

        // Look in the defaults, if not there then nowhere
        return defaults.find(name);
    }

    public TYPE find(String name) {
	return find__(name, true);
    }

    public boolean is_available(String name) {
	return (find__(name, false) == null);
    }

    public void enter(TYPE t) {
	debug_print("adding type: " + t.name);
	if (t.kind != null && t.isClass()) {
	    debug_print(".. a class that extends: ..");
	    TYPE fatherC = ((TYPE_CLASS)t).fatherClass;
	    if (fatherC != null) {
		debug_print(".. " + fatherC.name);
	    } else {
		debug_print(".. nothing");
	    }
	}
	
	scopes.head.add(t);
    }

    public void open(SYM_TABLE_SCOPE init, TYPE_CLASS ec, TYPE rt) {
	
	debug_print("OPEN SCOPE");
	
        // init may hold parameters of function
        // ec may hold father class
        // rt may hold return type
        scopes = new SYM_TABLE_SCOPE_LIST(init, scopes);
	if (ec != null) {
	    extending = ec;
	    debug_print(".. extending: " + ec.name);
	}
	if (rt != null) {
	    returnType = rt;
	    debug_print(".. return type: " + rt.name);
	}
    }

    public void close() {
	// Remove once closing CLASS
	boolean isClassS = scopes.head.getKind() == SCOPE_KIND.CLASS_SCOPE;
	boolean isFuncS  = scopes.head.getKind() == SCOPE_KIND.FUNCTION_SCOPE;
	if (isClassS) {
	    extending = null;
	}
	// Remove once closing FUNCTION
	if (isFuncS) {
	    returnType = null;
	}
	// Get rid of scope
        scopes = scopes.tail;
	// If was defining class, remove dummy class type
	if (isClassS) {
	    TYPE h = scopes.head.pop();
	    debug_print("popped off: " + h.name);
	    debug_print("no members (should say null): " + ((TYPE_CLASS)h).data_members);
	}
	
	debug_print("CLOSE SCOPE");
    }

// TODO: Wrap into a singleton instance

    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static SYM_TABLE instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected SYM_TABLE() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static SYM_TABLE getInstance()
    {
        if (instance == null)
        {
            instance = new SYM_TABLE();

        }
		return instance;
    }

 
}
