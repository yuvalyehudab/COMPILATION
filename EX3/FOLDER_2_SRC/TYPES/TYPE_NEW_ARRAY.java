package TYPES;

public class TYPE_NEW_ARRAY extends TYPE
{
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE memberT;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_NEW_ARRAY(TYPE memberT)
    {
        this.kind = KIND.ARRAY;
        this.name = "new";
        this.memberT = memberT;
    }
    public String getName()
    {
        return this.name;
    }

}
