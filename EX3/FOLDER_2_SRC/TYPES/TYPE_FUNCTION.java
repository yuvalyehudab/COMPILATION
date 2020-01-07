package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(String name,TYPE returnType,TYPE_LIST params)
	{
		this.kind = FUNCTION;
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}

	public boolean equals(TYPE that) {
		return (that != null && that.kind == FUNCTION
				&& this.name.equals(that.name)
				&& this.returnType.equals(that.returnType)
				&& this.params.equals(that.params)
		);
	}
}
