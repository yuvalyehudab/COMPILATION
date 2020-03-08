/***********/
/* PACKAGE */
/***********/
package AST.EXP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import AST.*;
import IR.COMMAND.*;
import IR.REG.*;
import SYMBOL_TABLE.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import java.util.*;

public class AST_CALLABLE_VAR_CONT_SUBSCRIPT extends AST_CALLABLE_VAR_CONT {
    
    public AST_EXP exp;

    public AST_CALLABLE_VAR_CONT_SUBSCRIPT(AST_CALLABLE_VAR_CONT next, AST_EXP exp) {
        super(next);
        this.exp = exp;
    }
}
