package AST;

public class AST_EXP_STRING extends AST_EXP
{
    /************************/
    /* simple variable str */
    /************************/
    public String str;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_STRING(String str)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> STRING( %s )\n",str);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.str = str;
    }

    /**************************************************/
    /* The printing message for a simple var AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE VAR */
        /**********************************/
        System.out.format("AST NODE STRING( %s )\n",str);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STRING\n(%s)",str));
    }
}
