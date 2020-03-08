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

public class AST_CALLABLE_VAR_CONT_FUNC extends AST_CALLABLE_VAR_CONT {

    List<AST_EXP> expressions;

    public AST_CALLABLE_VAR_CONT_FUNC(List<AST_EXP> expressions) {
        super(null);
        this.expressions = expressions;
    }

    public AST_CALLABLE_VAR_CONT_FUNC() {
        this(Collections.emptyList());
    }
}
