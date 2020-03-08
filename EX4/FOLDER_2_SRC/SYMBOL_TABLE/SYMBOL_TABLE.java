/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import TYPES.SCOPE_BOUNDARY;
import TYPES.SCOPE_BOUNDARY.Scope;

import java.util.function.*;
import java.util.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
    private int hashArraySize = 13;
	
    /**********************************************/
    /* The actual symbol table data structure ... */
    /**********************************************/
    private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
    private SYMBOL_TABLE_ENTRY top;
    private int top_index = 0;
	
    public Stack<Scope> scopeStack = new Stack<>();
	
    public TYPE_CLASS ownerClass;
    public SYMBOL ownerFunc;

    private int currentScopeCounter;
	
    public List<SYMBOL> globalSymbols = new ArrayList<>();
    public List<TYPE_CLASS> knownClasses = new ArrayList<>();
    public List<TYPE_ARR> knownArrays = new ArrayList<>();
	
    /**************************************************************/
    /* A very primitive hash function for exposition purposes ... */
    /**************************************************************/
    private int hash(String s)
    {
	if (s.charAt(0) == 'l') {return 1;}
	if (s.charAt(0) == 'm') {return 1;}
	if (s.charAt(0) == 'r') {return 3;}
	if (s.charAt(0) == 'i') {return 6;}
	if (s.charAt(0) == 'd') {return 6;}
	if (s.charAt(0) == 'k') {return 6;}
	if (s.charAt(0) == 'f') {return 6;}
	if (s.charAt(0) == 'S') {return 6;}
	return 12;
    }

    /****************************************************************************/
    /* Enter a variable, function, class type or array type to the symbol table */
    /****************************************************************************/
	
    public void enter(String name,TYPE t)
    {
	enter(name, t, false, false);
    }
	
    public void enter(String name,TYPE t, boolean isVarDec){
	enter(name, t, isVarDec, false);
    }
	
    public void enter(String name,TYPE t, boolean isVarDec, boolean addToNamspace){
	enter(name, t, isVarDec, addToNamspace, false);
    }
	
    public void enter(String name,TYPE t, boolean isVarDec, boolean addToNamspace, boolean isTypeDec)
    {
	/*************************************************/
	/* [1] Compute the hash value for this new entry */
	/*************************************************/
	int hashValue = hash(name);
	SYMBOL s;
	if (ownerFunc == null)
	    s = new SYMBOL(name, t, ownerClass);
	else
	    s = new SYMBOL(name, t);

	/******************************************************************************/
	/* [2] Extract what will eventually be the next entry in the hashed position  */
	/*     NOTE: this entry can very well be null, but the behaviour is identical */
	/******************************************************************************/
	SYMBOL_TABLE_ENTRY next = table[hashValue];
	
	/**************************************************************************/
	/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
	/**************************************************************************/
	SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(s, isVarDec, isTypeDec, hashValue, next, top, currentScopeCounter, top_index++);

	/**********************************************/
	/* [4] Update the top of the symbol table ... */
	/**********************************************/
	top = e;
		
	/****************************************/
	/* [5] Enter the new entry to the table */
	/****************************************/
	table[hashValue] = e;
		
	if (currentScopeCounter == 0 && !addToNamspace && !isTypeDec)
	    globalSymbols.add(s);
		
	if (t.isArray() && !isVarDec && addToNamspace)
	    knownArrays.add((TYPE_ARR)t);
	/**************************/
	/* [6] Print SYMBOL Table */
	/**************************/
	//PrintMe();
    }

    /***********************************************/
    /* Find the inner-most scope element with name */
    /***********************************************/
    public SYMBOL find(String name){return find(name, null);}
    public SYMBOL find(String name, Predicate<SYMBOL_TABLE_ENTRY> p){return find(name, p, null);}
	
    public SYMBOL find(String name, Predicate<SYMBOL_TABLE_ENTRY> p, Predicate<SYMBOL_TABLE_ENTRY> pcont)
    {
	SYMBOL_TABLE_ENTRY e;
				
	for (e = table[hash(name)]; e != null && (pcont == null || pcont.test(e)); e = e.next)
	    {
		if (name.equals(e.name) && (p == null || p.test(e)))
		    {
			return e.symbol;
		    }
	    }
		
	return null;
    }
	
    public SYMBOL findMethod(String name, boolean inClass)
    {
	if(inClass && ownerClass != null)
	    {
		SYMBOL s = ownerClass.searchMethod(name);
		if (s != null)
		    return s;
	    }
	return find(name, e -> !e.isVarDec && e.symbol.type.isFunc());
    }
	
    public SYMBOL findField(String name, boolean inClass)
    {
	if (ownerFunc != null)
	    {
		SYMBOL s = find(name, null, e -> e.scopeCounter == currentScopeCounter);
		if (s != null)
		    return s;
	    }
	if (inClass && ownerClass != null)
	    {
		SYMBOL s = ownerClass.searchField(name);
		if (s != null)
		    return s;
	    }
	return find(name, e -> e.isVarDec);
    }
	
    public TYPE_CLASS findTypeClass(String name){
	SYMBOL s = find(name, e -> !e.isVarDec && e.symbol.type.isClass());
	return s != null ? (TYPE_CLASS) s.type : null;
    }
	
    public TYPE_ARR findTYPE_ARR(String name){
	SYMBOL s = find(name, e -> !e.isVarDec && e.symbol.type.isArray());
	return s != null ? (TYPE_ARR) s.type : null;
    }
	
    public TYPE findType(String name)
    {
	if (name.equals(TYPE_INT.instance.name))
	    return TYPE_INT.instance;
	if (name.equals(TYPE_STRING.instance.name))
	    return TYPE_STRING.instance;
	if (name.equals(TYPE_VOID.instance.name))
	    return TYPE_VOID.instance;
		
	TYPE arrayType = findTYPE_ARR(name);
	if (arrayType != null)
	    return arrayType;
		
	return findTypeClass(name);
    }

    public SYMBOL findInCurrentEnclosingScope(String name) {
        int currentEnclosingScope = currentScopeCounter % 10;
        return find(name, null, e -> e.scopeCounter % 10 >= currentEnclosingScope);
    }

    public SYMBOL findInCurrentScope(String name) {
        return find(name, null, e -> e.scopeCounter == currentScopeCounter);
    }


    /***************************************************************************/
    /* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
    /***************************************************************************/
    public void beginScope(Scope scope, TYPE_CLASS ownerType, SYMBOL ownerSymbol, String name)
    {
	if (scope == Scope.BLK)
	    currentScopeCounter += 10;
	else
	    currentScopeCounter++;
		
	scopeStack.push(scope);

	/************************************************************************/
	/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
	/* they are not really types. In order to be ablt to debug print them,  */
	/* a special SCOPE_BOUNDARY was developed for them. This     */
	/* class only contain their type name which is the bottom sign: _|_     */
	/************************************************************************/
	enter(
	      "SCOPE-BOUNDARY",
	      new SCOPE_BOUNDARY(name, scope));
			
	if (scope == Scope.FNC)
	    {
		ownerFunc = ownerSymbol;
	    }
	else if (scope == Scope.CLS)
	    {
		ownerClass = (TYPE_CLASS) ownerType;
		knownClasses.add(ownerClass);
	    }
	else if (scope == Scope.SCN)
	    {
		ownerClass = (TYPE_CLASS)ownerType;
	    }
		
	/*********************************************/
	/* Print the symbol table after every change */
	/*********************************************/
	//PrintMe();
    }

    /********************************************************************************/
    /* end scope = Keep popping elements out of the data structure,                 */
    /* from most recent element entered, until a <NEW-SCOPE> element is encountered */
    /********************************************************************************/
    public List<SYMBOL> endScope()
    {
	List<SYMBOL> syms = new ArrayList<>();
		
	/**************************************************************************/
	/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
	/**************************************************************************/
	while (top.name != "SCOPE-BOUNDARY")
	    {
		if(scopeStack.peek() == Scope.SCN && ownerFunc == null)
		    {
			TYPE t = top.symbol.type;
			if (t.isFunc())
			    ownerClass.insertMethod(top.name, (TYPE_FUNCTION)t);
			else
			    ownerClass.insertField(top.name, t);
		    }
		if (top.isVarDec)
		    syms.add(top.symbol);
			
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prev;
	    }
		
	Scope s = ((SCOPE_BOUNDARY) top.symbol.type).scope;
	if (s == Scope.FNC)
	    ownerFunc = null;
	else if (s == Scope.CLS || s == Scope.SCN)
	    ownerClass = null;
	/**************************************/
	/* Pop the SCOPE-BOUNDARY sign itself */		
	/**************************************/
	table[top.index] = top.next;
	top_index = top_index-1;
	top = top.prev;
		
	currentScopeCounter = top == null ? 0 : top.scopeCounter;
	scopeStack.pop();
		
	return syms;

	/*********************************************/
	/* Print the symbol table after every change */		
	/*********************************************/
	//PrintMe();
    }

    public boolean isInFunc() {
        return scopeStack.search(Scope.FNC) != -1;
    }
	
	
	
    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static SYMBOL_TABLE instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected SYMBOL_TABLE() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static SYMBOL_TABLE getInstance()
    {
	if (instance == null)
	    {
		/*******************************/
		/* [0] The instance itself ... */
		/*******************************/
		instance = new SYMBOL_TABLE();

		/*****************************************/
		/* [1] Enter primitive types int, string */
		/*****************************************/
		instance.enter("int",   TYPE_INT.instance, false, true, true);
		instance.enter("string",TYPE_STRING.instance, false, true, true);

		/*************************************/
		/* [2] How should we handle void ??? */
		/*************************************/

		/***************************************/
		/* [3] Enter library function PrintInt */
		/***************************************/
		instance.enter(
			       "PrintInt",
			       new TYPE_FUNCTION(
						 "PrintInt",
						 new ArrayList<>(Arrays.asList(TYPE_INT.instance))));
			
		instance.enter(
			       "PrintString",
			       new TYPE_FUNCTION(
						 "PrintString",
						 new ArrayList<>(Arrays.asList(TYPE_STRING.instance))));
			
	    }
	return instance;
    }
}
