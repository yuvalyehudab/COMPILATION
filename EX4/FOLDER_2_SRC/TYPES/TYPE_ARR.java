package TYPES;

public class TYPE_ARR extends TYPE
{
	public TYPE arrT;
	
	public TYPE_ARR(String name, TYPE arrT)
	{
		super(name);
		this.arrT = arrT;
	}
	public boolean isArray(){return true;}
	
	public boolean equals(Object o)
	{
		if (!(o instanceof TYPE_ARR))
			return false;
		if (!((TYPE_ARR)o).name.equals(name))
			return false;
		return ((TYPE_ARR)o).arrT.equals(arrT);
	}
	
	public boolean canAssign(TYPE t)
	{
		if (equals(t))
			return true;
		if (t == TYPE_NIL.instance)
			return true;
		if (t instanceof TYPE_ARR_GENERAL && ((TYPE_ARR_GENERAL)t).arrT.equals(arrT))
			return true;
		return (t == TYPE_ERROR.instance);
	}
}
