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
	/* EQUALITY
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
	} */

	/***/
	/* FIND */
	/***/
	public TYPE find(String name) 
	{
		TYPE_LIST tl = this;
		while (tl != null && tl.head != null)
		{
			if (tl.head.getName().equals(name))
			{
				return tl.head;
			}
			tl = tl.tail;
		}
		return null;
	}
    
    public boolean isAsExpected (TYPE_LIST that) {
	boolean result = true;
		TYPE_LIST this_tl = this;
		TYPE_LIST that_tl = that;
		while (result && this_tl != null && that_tl != null && this_tl.head != null && that_tl != null)
		{
			if (!this_tl.head.isAsExpected(that_tl.head))
			{
				return false;
			}
			this_tl = this_tl.tail;
			that_tl = that_tl.tail;
		}
		if (this_tl == null && that_tl == null)
		{
			return true;
		}
		return false;
    }
}
