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

    public boolean equals(TYPE that) {
		return (that != null
			&& that.kind != KIND.FUNCTION
			&& this.name.equals(that.name)
			&& this.t.name.equals(((TYPE_VAR_DEC)that).t.name)
		);
    }
}
