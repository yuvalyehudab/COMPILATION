package SYM_TABLE;

import TYPES.*;

public class SYM_TABLE_SCOPE {
    private SCOPE_KIND kind;
    public TYPE_LIST types;

    public SCOPE_KIND getKind() {
        return kind;
    }

    public TYPE find (String name) {
        if (types == null)
        {
            return null;
        }
        return types.find(name);
    }

    public SYM_TABLE_SCOPE(SCOPE_KIND kind, TYPE_LIST types) {
        this.kind = kind;
        this.types = types;
    }
    public void add (TYPE t)
    {
        if (this.types == null)
        {
            System.out.println(":::::::::::::::::::debug start:::::::::::::");
            System.out.println("==================SYM_TABLE_SCOPE.add==================");
            System.out.println("this.types before");
            System.out.println(this.types);
            this.types = new TYPE_LIST(t, null);
            System.out.println("this.types after");
            System.out.println(this.types);
            System.out.println("==================SYM_TABLE_SCOPE.add==================");
            System.out.println(":::::::::::::::::::debug end:::::::::::::");
        }
        else
        {
            System.out.println(":::::::::::::::::::debug start - else:::::::::::::");
            System.out.println("==================SYM_TABLE_SCOPE.add==================");
            System.out.println("this.types before");
            System.out.println(this.types);
                        System.out.println("this.types.tail before");
            System.out.println(this.types.tail);

            this.types = new TYPE_LIST(t, this.types);
            System.out.println("this.types after");
            System.out.println(this.types);
            System.out.println("this.types.tail after");
            System.out.println(this.types.tail);
            System.out.println("==================SYM_TABLE_SCOPE.add==================");
            System.out.println(":::::::::::::::::::debug end:::::::::::::");
        }
    }

    public boolean isFunction() {
	return (this.kind == SCOPE_KIND.FUNCTION_SCOPE);
    }
}
