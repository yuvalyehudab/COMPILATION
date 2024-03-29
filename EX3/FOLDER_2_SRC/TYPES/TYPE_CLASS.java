package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS fatherClass;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_LIST data_members;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(String name,TYPE_LIST data_members, TYPE_CLASS fatherClass)
	{
		this.kind = KIND.CLASS;
		this.name = name;
		this.fatherClass = fatherClass;
		this.data_members = data_members;
	}

	public TYPE find (String name) {
	    if (this.data_members != null) {
		TYPE here = this.data_members.find(name);
		if (here != null) {
			return here;
		}
	    }
	    debug_print("did not find here, is there an ancestor?: " + this.fatherClass);
		if (this.fatherClass != null) {
			return this.fatherClass.find(name);
		}
		return null;
	}

	public boolean isAncestor (String candidate) {
		if (this.name == candidate) { return true; } // Found it!
		if (this.fatherClass == null) { return false; } // End of search.
		return fatherClass.isAncestor(candidate);
	}
}
