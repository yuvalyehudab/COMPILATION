package TYPES;

public class TYPE_ERROR extends TYPE
{
	
	public static final TYPE_ERROR instance = new TYPE_ERROR();

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_ERROR() {super("ERROR");}
	
	public boolean isClass(){return true;}
	public boolean isArray(){return true;}
	public boolean canAssign(TYPE t){return true;}
	public boolean isBool(){return true;}
}
