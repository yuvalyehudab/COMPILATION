package TYPES;

public class TYPE_VAR_DEC extends TYPE
{
	public TYPE t;
	public String name;
	
	public TYPE_VAR_DEC(TYPE t,String name)
	{
		this.t = t;
		this.name = name;
	}
	public String getName()
    {
        return this.name;
    }

    public TYPE getType() {
	return this.t;
    }
}
