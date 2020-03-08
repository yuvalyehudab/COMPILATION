package SYMBOL_TABLE;

import TYPES.*;

public class SYMBOL
{
	
	public String name;
	public TYPE type;
	public TYPE_CLASS instance;
	
	public SYMBOL(String name, TYPE type, TYPE_CLASS instance)
	{
		this.name = name;
		this.type = type;
		this.instance = instance;
	}
	public SYMBOL(String name, TYPE type)
	{
		this(name, type, null);
	}
	
	public boolean isFunc(){return type instanceof TYPE_FUNCTION;}
	public TYPE_FUNCTION getFunc(){return (TYPE_FUNCTION)type;}
	
	public boolean isField(){return !isFunc();}
	public boolean isBounded(){return instance != null;}
	
	public boolean equals(Object o)
	{
		if (!(o instanceof SYMBOL))
		{
			return false;
		}
		SYMBOL s = (SYMBOL)o;
		boolean res = s.name.equals(name) && s.type.equals(type) ;
		res = res && ((s.instance == null && instance == null) || (s.instance != null && instance != null && (s.instance.canAssign(instance) || instance.canAssign(s.instance))));
		return	res;
	}
	
	public int hashCode()
	{
		int res = name.hashCode();
		res = 31 * res + type.hashCode();
		return res;
	}
    public String toString() {
        if (instance == null)
            return "_" + name;
        else
            return instance.name + "_" + name;
    }
	
}
