package TYPES;

public class TYPE_ARRAY extends TYPE
{
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE memberT;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ARRAY(String name, TYPE memberT)
    {
        this.kind = KIND.ARRAY;
        this.name = name;
        this.memberT = memberT;
    }
}
