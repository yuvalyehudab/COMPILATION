package TYPES;

public class TYPE_INT extends TYPE
{
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	public static TYPE_INT instance = new TYPE_INT();

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected TYPE_INT() {super("int");}

	public boolean isBool(){return true;}
	public boolean canAssign(TYPE t){return (t == instance || t == TYPE_ERROR.instance);}
	
	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static TYPE_INT getInstance()
	{
		if (instance == null)
		{
			instance = new TYPE_INT();
			instance.name = "int";
		}
		return instance;
	}
}
