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

	public boolean isVoid() {
		switch (kind) {
			case VOID:
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

	public boolean isGlobal() {
		switch (kind) {
			case CLASS:
			case ARRAY:
			case FUNCTION:
				return true;

			default:
				return false;
		}
	}

	/***/
	/* EQUALITY */
	/***/
	public boolean equals(TYPE that) {
		return false;
	}
}
