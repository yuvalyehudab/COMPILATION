package TYPES;

public class TYPE_VAR_DEC extends TYPE
{
	public String typeName;
	public String name;
	
	public TYPE_VAR_DEC(String typeName,String name)
	{
		this.typeName = typeName;
		this.name = name;
	}
	public String getName()
    {
        return this.name;
    }

    public TYPE getTypeName() {
	return this.typeName;
    }
}
