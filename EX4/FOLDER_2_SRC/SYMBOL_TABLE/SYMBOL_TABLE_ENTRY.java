/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/**********************/
/* SYMBOL TABLE ENTRY */
/**********************/
public class SYMBOL_TABLE_ENTRY
{
	/*********/
	/* index */
	/*********/
	int index;
	
	/********/
	/* name */
	/********/
	public String name;

	/******************/
	/* TYPE value ... */
	/******************/
	public SYMBOL symbol;

	/*********************************************/
	/* prevtop and next symbol table entries ... */
	/*********************************************/
	public SYMBOL_TABLE_ENTRY prev;
	public SYMBOL_TABLE_ENTRY next;

	/****************************************************/
	/* The prevtop_index is just for debug purposes ... */
	/****************************************************/
	public int prevtop_index;
	
	public boolean isVarDec;
	public boolean isTypeDec;
	
	public int scopeCounter;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public SYMBOL_TABLE_ENTRY(
		SYMBOL symbol,
		boolean isVarDec,
		boolean isTypeDec,
		int index,
		SYMBOL_TABLE_ENTRY next,
		SYMBOL_TABLE_ENTRY prev,
		int scopeCounter,
		int prevtop_index)
	{
		this.index = index;
		this.name = symbol.name;
		this.symbol = symbol;
		this.isTypeDec = isTypeDec;
		
		this.isVarDec = isVarDec;
		this.next = next;
		this.prev = prev;
		this.prevtop_index = prevtop_index;
		this.scopeCounter = scopeCounter;
	}
}
