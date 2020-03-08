package TYPES;

import SYMBOL_TABLE.*;

import java.util.*;


public class TYPE_CLASS extends TYPE
{
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE_CLASS father;

    /**************************************************/
    /* Gather up all data members in one place        */
    /* Note that data members coming from the AST are */
    /* packed together with the class methods         */
    /**************************************************/
    public Map<String, SYMBOL> fields;
    public Map<String, SYMBOL> methods;
	
    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_CLASS(String name, TYPE_CLASS father, Map<String, SYMBOL> fields, Map<String, SYMBOL> methods)
    {
	super(name);
	this.father = father;
	this.fields = fields;
	this.methods = methods;
    }
    public TYPE_CLASS(String name, TYPE_CLASS father)
    {
	this(name, father, new HashMap<>(), new HashMap<>());
    }
    public TYPE_CLASS(String name)
    {
	this(name, null);
    }
	
    public boolean insertMethod(String name, TYPE_FUNCTION t)
    {
	if (methods.containsKey(name))
	    return false;
	methods.put(name, new SYMBOL(name, t, this));
	return true;
    }
	
    public boolean insertField(String name, TYPE t)
    {
	if (fields.containsKey(name))
	    return false;
	fields.put(name, new SYMBOL(name, t, this));
	return true;
    }
    
    public SYMBOL searchFieldLocal(String name) {
        return fields.get(name);
    }
	
    public SYMBOL searchField(String name)
    {
	TYPE_CLASS current = this;
	while (current != null)
	    {
		SYMBOL s = current.fields.get(name);
		if (s != null)
		    return s;
		current = current.father;
	    }
	return null;
    }
    
    public SYMBOL searchMethodLocal(String name) {
        return methods.get(name);
    }
	
    public SYMBOL searchMethod(String name)
    {
	TYPE_CLASS current = this;
	while (current != null)
	    {
		SYMBOL s = current.methods.get(name);
		if (s != null)
		    return s;
		current = current.father;
	    }
	return null;
    }
	
    public List<SYMBOL> getFields(){return new ArrayList<>(fields.values());}
    public List<SYMBOL> getMethods(){return new ArrayList<>(methods.values());}
	
    public boolean isClass(){return true;}
	
    public boolean canAssign (TYPE t)
    {
	if (t == null) return false;
	if (t == TYPE_NIL.instance) return true;
	else if (t == TYPE_ERROR.instance) return true;
	else if (!t.isClass()) return false;
	TYPE_CLASS check = (TYPE_CLASS) t;
	while (check != null)
	    {
		if (check.equals(this))
		    return true;
		check = check.father;
	    }
	return false;
    }
    public boolean equals(Object o)
    {
	if (!(o instanceof TYPE_CLASS))
	    return false;
	return ((TYPE_CLASS)o).name.equals(name);
    }
}
