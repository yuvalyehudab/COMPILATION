package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}
	
	public boolean isFunc(){ return false;}
	
	public boolean isBool(){ return false;}
	
	public TYPE(String name)
	{
		this.name = name;
	}
	
	public abstract boolean canAssign(TYPE t);
	
	public int hashCode()
	{
		return name.hashCode();
	}
	
}
