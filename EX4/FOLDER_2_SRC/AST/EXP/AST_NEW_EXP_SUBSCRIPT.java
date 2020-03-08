/***********/
/* PACKAGE */
/***********/
package AST.EXP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;
import SYMBOL_TABLE.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

public class AST_NEW_EXP_SUBSCRIPT extends AST_NEW_EXP
{
	
	public AST_EXP subscript;
	
	public AST_NEW_EXP_SUBSCRIPT(String class_name, AST_EXP subscript)
	{
		super(class_name);
		this.subscript = subscript;
	}
	public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
	{
		subscript.semantPost(st);
		TYPE c = st.findType(class_name);
		
		if (c == TYPE_VOID.instance)
			throw new SYMBOL_TABLE_ERROR(this);
		else if (c != null){
			if (subscript.t == TYPE_INT.instance) {
                this.t = c;
            } else {
                throw new SYMBOL_TABLE_ERROR(this);
            }
		}
		else
			throw new SYMBOL_TABLE_ERROR(this);
		this.t = new TYPE_ARR_GENERAL(c);
	}
	public Reg irMe(Manager context)
	{
		Reg t = subscript.irMe(context);
		context.command(new IRCommand_Push(t));
		context.command(new IRCommand_Call(context.constructorOf((TYPE_ARR)this.t)));
		Reg t2 = context.newRegister();
        context.command(new IRCommand_Pop(t2));
		
        return t2;
	}
}
