package TYPES;

public class TYPE_STRING extends TYPE
{
    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    public static TYPE_STRING instance = new TYPE_STRING();

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected TYPE_STRING() {super("string");}
	
    public boolean canAssign(TYPE t){
	if (t == instance)
	    return true;
	return t == TYPE_ERROR.instance;
    }

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static TYPE_STRING getInstance()
    {
	if (instance == null)
	    {
		instance = new TYPE_STRING();
		instance.name = "string";
	    }
	return instance;
    }
}
