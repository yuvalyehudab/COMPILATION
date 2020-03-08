package TYPES;

import java.util.*;

public class TYPE_FUNCTION extends TYPE
{
    /***********************************/
    /* The return type of the function */
    /***********************************/
    public TYPE returnType;

    /*************************/
    /* types of input params */
    /*************************/
    public List<TYPE> params;
	
    public TYPE_CLASS self_type;
	
    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_FUNCTION(TYPE returnType, String name, List<TYPE> params, TYPE_CLASS self_type)
    {
	super(name);
	this.returnType = returnType;
	this.params = params;
	this.self_type = self_type;
    }
    public TYPE_FUNCTION(String name, TYPE returnType, List<TYPE> params, TYPE_CLASS self_type)
    {
	this(returnType, name, params, self_type);
    }
    public TYPE_FUNCTION(TYPE returnType, String name, List<TYPE> params)
    {
	this(returnType, name, params, null);
    }
    public TYPE_FUNCTION(TYPE returnType, String name)
    {
	this(returnType, name, Collections.emptyList());
    }
    public TYPE_FUNCTION(String name)
    {
	this(TYPE_VOID.instance, name);
    }
    public TYPE_FUNCTION(String name, List<TYPE> params)
    {
	this(TYPE_VOID.instance, name, params);
    }
    public TYPE_FUNCTION(String name, TYPE param)
    {
	this(name, Collections.nCopies(1,param));
    }
	
    public boolean isFunc(){return true;}
    public boolean compareParamList(TYPE_FUNCTION f)
    {
	if ((self_type == null && f.self_type != null) || (self_type != null && f.self_type == null))
	    return false;
	if (self_type != null && !f.self_type.canAssign(self_type) && self_type.canAssign(f.self_type))
	    return false;
	if (f.params.size() != params.size() || !f.returnType.equals(returnType))
	    return false;
		
	for(int i = 0; i < f.params.size(); i++)
	    {
		if (!params.get(i).equals(f.params.get(i)))
		    return false;
	    }
	return true;
    }
    public boolean canAssign(TYPE t) {
        throw new RuntimeException();
    }
    public boolean equals(Object o)
    {
	if (!(o instanceof TYPE_FUNCTION))
	    return false;
	if (!((TYPE_FUNCTION) o).name.equals(name))
	    return false;
	return compareParamList((TYPE_FUNCTION)o);
    }
}
