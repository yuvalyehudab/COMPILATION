package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public String fatherName;
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
	public TYPE_CLASS(String fatherName,String name,TYPE_LIST data_members)
	{
		this.kind = CLASS;
		TYPE fatherClass = SYM_TABLE.getInstance().find(fatherName);
		if (fatherClass == null || fatherClass.kind != CLASS) {

		}
		this.fatherClass = fatherClass;
		this.data_members = data_members;
	}

	public void SemantMe () {
		// Initialize pointer to symbol table
		SYM_TABLE sym_table = SYM_TABLE.getInstance();

		// TODO: Check that scope is global

		// TODO: Rest
	}
}
