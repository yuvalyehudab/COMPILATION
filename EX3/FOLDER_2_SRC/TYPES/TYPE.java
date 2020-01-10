package TYPES;

public abstract class TYPE
{
	/***/
	/* Kind */
	/***/
	public KIND kind;

	/***/
	/* Name */
	/***/
	public String name;
	public KIND getKind()
	{
		return this.kind;
	}

	public boolean isClass() {
		switch (kind) {
			case CLASS:
				return true;

			default:
				return false;
		}
	}

	public boolean isArray() {
		switch (kind) {
			case ARRAY:
				return true;

			default:
				return false;
		}
	}

	public boolean isFunction() {
		switch (kind) {
			case FUNCTION:
				return true;

			default:
				return false;
		}
	}

	public boolean isVoid() {
		switch (kind) {
			case VOID:
				return true;

			default:
				return false;
		}
	}

	public boolean isNil() {
		switch (kind) {
			case NIL:
				return true;

			default:
				return false;
		}
	}

	public boolean isTypeName() {
		switch (kind) {
			case CLASS:
			case ARRAY:
			case BASIC:
				return true;

			default:
				return false;
		}
	}

	/***/
	/* EQUALITY */
	/***/
	public boolean equals(TYPE that) {
	    return this.name.equals(that.name);
	}
    
    public boolean isAsExpected (TYPE that) 
    {
	debug_print("expected type: " + this.name);
	debug_print("argument type: " + that.name);
		boolean equal = this.equals(that);
		boolean nil_legit = that.isNil() && (this.isArray() || this.isClass());
		boolean inheritance = that.isClass() && this.isClass() && ((TYPE_CLASS)that).isAncestor(this.name);
		boolean new_array = this.isArray() && that.isArray() && that.name.equals("new") && ((TYPE_ARRAY)this).memberT.name.equals(((TYPE_NEW_ARRAY)that).memberT.name);
		return (equal || nil_legit || inheritance || new_array);
	}

    public String getName()
    {
        return this.name;
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public void debug_print(String str) { System.out.println(ANSI_YELLOW + str + ANSI_RESET); }
}
