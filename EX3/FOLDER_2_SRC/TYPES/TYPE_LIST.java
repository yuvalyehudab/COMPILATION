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
		this.tail.equals(that.tail);
	}
}
