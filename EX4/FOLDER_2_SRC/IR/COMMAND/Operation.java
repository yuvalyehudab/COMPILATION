/***********/
/* PACKAGE */
/***********/
package IR.COMMAND;

import AST.EXP.*;

public enum Operation
{
	Plus("+"), Minus("-"), Times("*"), Divide("/"), EQ("=="), GT(">"), LT("<"), Concat("concat"), StrEQ("~");
    String t;
    Operation(String t) {this.t = t;}

	
    public static int evaluate(int l, Operation op, int r) {
        switch (op) {
            case Plus:
                return overflow(l + r);
            case Minus:
                return overflow(l - r);
            case Times:
                return overflow(l * r);
            case Divide:
                return overflow(l / r);
            case LT:
                return l < r ? 1 : 0;
            case GT:
                return l > r ? 1 : 0;
            case EQ:
                return l == r ? 1 : 0;
        }
        return -1;
    }

    public static Operation fromAstOp(AST_EXP_BINOP.Op op) {
        switch (op) {
    case Plus:
        return Plus;
    case Minus:
        return Minus;
    case Times:
        return Times;
    case Divide:
        return Divide;
    case LT:
        return LT;
    case GT:
        return GT;
    case EQ:
        return EQ;
        }
        return null;
    }

    private static int overflow(int value) {
        if (value > 32767)
            return 32767;
        else if (value < -32768) {
            return -32768;
        } else {
            return value;
        }
    }
	
}
