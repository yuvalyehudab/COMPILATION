package TYPES;

public class SCOPE_BOUNDARY extends TYPE
{
	public final Scope scope;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public SCOPE_BOUNDARY(String name, Scope scope)
	{
		super(name);
		this.scope = scope;
	}
    public boolean canAssign(TYPE t) {
        throw new RuntimeException();
    }
	
	public enum Scope {FNC, CLS, SCN, BLK}
	
}
