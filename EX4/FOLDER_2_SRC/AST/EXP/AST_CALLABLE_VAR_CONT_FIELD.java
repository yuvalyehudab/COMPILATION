/***********/
/* PACKAGE */
/***********/
package AST.EXP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import IR.COMMAND.*;
import IR.REG.*;
import SYMBOL_TABLE.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

public class AST_CALLABLE_VAR_CONT_FIELD extends AST_CALLABLE_VAR_CONT {

    public String fieldName;

    public AST_CALLABLE_VAR_CONT_FIELD(AST_CALLABLE_VAR_CONT next, String fieldName) {
        super(next);
        this.fieldName = fieldName;
    }
}
