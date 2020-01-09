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

	public boolean isNIL() {
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
    
    public boolean isAsExcpected (TYPE that) {
	boolean equal = this.equals(that);
	boolean nil_legit = that.isNil() && (this.isArray() || this.isClass());
	boolean inheritance = that.isClass() && this.isClass() && ((TYPE_CLASS)t_init).isAncestor(this.name);
	return (equal || nil_legit || inheritance);
    }
}
