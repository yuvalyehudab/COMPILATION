package TYPES;

public class TYPE_VOID extends TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	public static TYPE_VOID instance = new TYPE_VOID();

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_VOID() {super("void");}
	
	public boolean canAssign(TYPE t){return t == instance;}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_VOID getInstance()
	{
		if (instance == null)
		{
			instance = new TYPE_VOID();
		}
		return instance;
	}
}
