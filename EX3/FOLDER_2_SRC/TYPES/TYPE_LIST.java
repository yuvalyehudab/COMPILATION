package TYPES;

public class TYPE_LIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head,TYPE_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}

	/***/
	/* EQUALITY */
	/***/
	public boolean equals(TYPE_LIST that) {
		// Check heads
		if (!this.head.equals(that.head)) {
			return false;
		}
		// Check tail null cases
		if (this.tail == null || that.tail == null) {
			if (that.tail == null && that.tail == null) {
				return true;
			}
			return false;
		}
		// Check tail non-null case
		return this.tail.equals(that.tail);
	}

	/***/
	/* FIND */
	/***/
	public TYPE find(String name) 
	{
		/*TYPE_LIST tl = this;
		while (tl.head != null)
		{
			if (tl.head.name.equals(name))
			{
				return tl.head;
			}
			tl = tl.tail;
		}
		*/
		if (this.head.name.equals(name)) {
			return this.head;
		}
		return null;
	}

	/***/
	/* REVERSE */
	/***/
	public TYPE_LIST reversed() {
		TYPE_LIST rev = null;
		for (TYPE_LIST it = this ; it != null ; it = it.tail) {
			rev = new TYPE_LIST(it.head, rev);
		}
		return rev;
	}
}
