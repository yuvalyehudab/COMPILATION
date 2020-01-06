package SYM_TABLE;

public class SYM_TABLE_SCOPE_LIST {
    public SYM_TABLE_SCOPE head = null;
    public SYM_TABLE_SCOPE_LIST tail = null;

    public SYM_TABLE_SCOPE_LIST(SYM_TABLE_SCOPE head, SYM_TABLE_SCOPE_LIST tail) {
        this.head = head;
        this.tail = tail;
    }
}
