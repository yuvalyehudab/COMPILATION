/***********/
/* PACKAGE */
/***********/
package AST.EXP;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import IR.COMMAND.*;
import IR.*;
import IR.REG.*;
import SYMBOL_TABLE.*;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;

public class AST_NEW_EXP extends AST_EXP
{
	public String class_name;
	public TYPE_CLASS type_class;
	
	public AST_NEW_EXP(String class_name)
	{
		this.class_name = class_name;
	}
	public void semantPost(SYMBOL_TABLE st) throws SYMBOL_TABLE_ERROR
	{
		type_class = st.findTypeClass(class_name);
		if (type_class == null)
			throw new SYMBOL_TABLE_ERROR(this);
		else
			this.t = type_class;
	}
	public Reg irMe(Manager context)
	{
		Reg t = context.newRegister();
		context.command(new IRCommand_Call(context.constructorOf(type_class)));
        context.command(new IRCommand_Pop(t));
        return t;
	}
}
