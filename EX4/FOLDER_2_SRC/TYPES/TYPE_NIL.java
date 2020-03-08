package TYPES;

public class TYPE_NIL extends TYPE
{
	public static TYPE_NIL instance = new TYPE_NIL();

	
	public TYPE_NIL()
	{
		super("nil");
	}
	public boolean canAssign(TYPE t)
	{
	    if (t == instance)
		return true;
	    return t == TYPE_ERROR.instance;
	}
}
