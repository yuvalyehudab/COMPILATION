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
import AST.VAR.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

public abstract class AST_CALLABLE_VAR_CONT 
{
	public AST_CALLABLE_VAR_CONT next;
	public int lineNumber;
	
	public AST_CALLABLE_VAR_CONT(AST_CALLABLE_VAR_CONT next) {
        this.next = next;
    }
	
	public static AST_EXP create(String id, AST_CALLABLE_VAR_CONT next) {
		AST_VAR current = new AST_VAR_SIMPLE(id);
		int lineNumber = next.lineNumber;
        current.lineNumber = lineNumber;
		
		while (next != null)
		{
			if (next instanceof AST_CALLABLE_VAR_CONT_FUNC) {
                
                AST_EXP ret_val;
                if (current instanceof AST_VAR_SIMPLE) {
                    ret_val = new AST_EXP_CALL(((AST_VAR_SIMPLE) current).name, ((AST_CALLABLE_VAR_CONT_FUNC) next).expressions);
                } else {
                    ret_val = new AST_EXP_CALL(((AST_VAR_FIELD) current).fieldName, ((AST_VAR_FIELD) current).var, ((AST_CALLABLE_VAR_CONT_FUNC) next).expressions);
                }
                ret_val.lineNumber = lineNumber;
                return ret_val;
            } else {
                if (next instanceof AST_CALLABLE_VAR_CONT_FIELD) {
                    current = new AST_VAR_FIELD(current, ((AST_CALLABLE_VAR_CONT_FIELD) next).fieldName);
                } else {
                    current = new AST_VAR_SUBSCRIPT(current, ((AST_CALLABLE_VAR_CONT_SUBSCRIPT) next).exp);
                }
                current.lineNumber = lineNumber;
            }
            next = next.next;
		}
		return new AST_EXP_VAR(current);
	}
	public static AST_EXP createId(String id) {
        return new AST_EXP_VAR(new AST_VAR_SIMPLE(id));
    }
}
