package IR;

import IR.COMMAND.*;
import IR.REG.*;
import SYMBOL_TABLE.*;
import TYPES.*;

import java.util.*;
import java.util.stream.*;

public class Manager {
    public static int VTABLE_OFFSET_IN_OBJECT = 4;
    public static int ARR_DATA_INITIAL_OFFSET = 4;
    public static int ARR_LENGTH_OFFSET = 0;
    public static int BYTE_COUNT = 4;
    public static int NIL_VALUE = 0;
    public static int MAX_INT = 32767;
    public static int MIN_INT = -32768;

    private static int OBJECT_FIELDS_INITIAL_OFFSET = 8;
    private static int VTABLE_INITIAL_OFFSET = 0;
    private static int ID_OFFSET_IN_OBJECT = 0;

    private int loadedFields = 0;
    private int labelCounter = 0;
    private static int localCounter = 0;

    private List<IRCommand_Label> preMainFunctions = new ArrayList<>();
    public List<IRCommand> commands = new ArrayList<>();
    private List<InnerManager> localsStack = new ArrayList<>();
    public Map<String, IRCommand_Label> strings = new HashMap<>();
    private Map<TYPE_CLASS, CLASS_TABLE> classTables = new HashMap<>();

    public static IRCommand_Label BASIC_FUNCTION_MAIN = new IRCommand_Label("main").startingLabel();
    public static IRCommand_Label BASIC_FUNCTION_PRINT_INT = generateFunctionLabelFor(new SYMBOL("PrintInt", new TYPE_FUNCTION("PrintInt", TYPE_INT.instance)));
    public static IRCommand_Label BASIC_FUNCTION_PRINT_STRING = generateFunctionLabelFor(new SYMBOL("PrintString", new TYPE_FUNCTION("PrintString", TYPE_STRING.instance)));
    public static IRCommand_Label BASIC_FUNCTION_PRINT_TRACE = generateFunctionLabelFor(new SYMBOL("PrintTrace", new TYPE_FUNCTION("PrintTrace")));

    public static IRCommand_Label BASIC_FUNCTION_ERROR_INVALID_POINTER = new IRCommand_Label("_BASIC_FUNCTION_ERROR_INVALID_POINTER_").startingLabel();
    public static IRCommand_Label BASIC_FUNCTION_ERROR_DIVISION_BY_ZERO = new IRCommand_Label("_BASIC_FUNCTION_ERROR_DIVISION_BY_ZERO_").startingLabel();
    public static IRCommand_Label BASIC_FUNCTION_ERROR_OUT_OF_BOUNDS = new IRCommand_Label("_BASIC_FUNCTION_ERROR_OUT_OF_BOUNDS_").startingLabel();
    public static IRCommand_Label BASIC_FUNCTION_MALLOC = new IRCommand_Label("_BASIC_FUNCTION_MALLOC_").startingLabel();
    public static IRCommand_Label BASIC_FUNCTION_EXIT = new IRCommand_Label("_BASIC_FUNCTION_EXIT_").startingLabel();
    private LabelGenerator constantStringLabelGenerator = s -> new IRCommand_Label("_CONST_STRING_" + labelCounter++ + "_");
    public static SYMBOL MAIN_SYMBOL = new SYMBOL("main", new TYPE_FUNCTION("main"));
    public static Reg FIRST_FUNCTION_PARAMETER = new ParamReg(0);

    public IRCommand_Label labelForConstantString(String s) {
	return strings.computeIfAbsent(s, constantStringLabelGenerator::newLabel);
    }


    public void loadClass(TYPE_CLASS cls) {
	if (classTables.containsKey(cls)) {
	    return;
	}
	if (cls.father != null) {
	    loadClass(cls.father);
	}

	CLASS_TABLE table = new CLASS_TABLE(cls, classTables.get(cls.father), VTABLE_INITIAL_OFFSET, OBJECT_FIELDS_INITIAL_OFFSET);
	cls.getMethods().forEach(table::insertFunction);
	cls.getFields().forEach(table::insertField);
	classTables.put(cls, table);
    }

    public int sizeOf(TYPE_CLASS cls) {
	return classTables.get(cls).getTotalSize();
    }


    public int getLoadedFieldsCount() {
	return loadedFields;
    }

    public void resetLoadedFieldsCounter() {
	loadedFields = 0;
    }

    public int getFieldOffset(SYMBOL symbol) {
	return classTables.get(symbol.instance).getField(symbol);
    }

    public int getMethodOffsetInVtable(SYMBOL symbol) {
	return classTables.get(symbol.instance).getFunction(symbol);
    }

    public void addPreMainFunction(IRCommand_Label label) {
	preMainFunctions.add(label);
    }

    public Reg newRegister() {
	return currentContext().registerAllocator.newRegister();
    }
    
    public IRCommand_Label newLabel( String description) {
	return currentContext().labelGenerator.newLabel(description);
    }

    public void command(IRCommand command) {
	commands.add(command);
    }

    public void label(IRCommand_Label label) {
	command(label);
    }

    public List<IRCommand_Label> getPreMainFunctions() {
	return preMainFunctions;
    }
    public void openScope(String name, List<SYMBOL> symbols, ScopeType type, boolean isParameters, boolean isBounded) {
	RegisterAllocator allocator = localsStack.isEmpty() ? new SimpleRegisterAllocator() : currentContext().registerAllocator;

	InnerManager context = new InnerManager(name, allocator, new SimpleLabelGenerator(), type);

	if (type != ScopeType.Inner) {
	    localCounter = 0;
	}
	if (type == ScopeType.Function && isBounded && isParameters) {
	    localCounter++;
	}

	for (SYMBOL symbol : symbols) {
	    if (symbol.isFunc()) {
		context.addFunction(symbol, generateFunctionLabelFor(symbol));
	    } else if (isParameters) {
		context.addLocal(symbol, new ParamReg(localCounter++));
	    } else if (type == ScopeType.Global) {
		context.addLocal(symbol, new GlobalReg(localCounter++));
	    } else {
		context.addLocal(symbol, new LocalReg(localCounter++));
		loadedFields++;
	    }
	}

	localsStack.add(context);
    }

    public void openObjectScope(TYPE_CLASS cls) {
	if (cls.father != null)
	    openObjectScope(cls.father);

	List<SYMBOL> addedSymbols = cls.getFields();
	addedSymbols.addAll(cls.getMethods());
	openScope(cls.name, addedSymbols, ScopeType.Class, false, true);
    }

    public void closeObjectScope() {
	while (currentContext().isClassScope())
	    closeScope();
    }

    public void closeScope() {
	InnerManager last = currentContext();
	if (last.scopeType == ScopeType.Function)
	    last.registerAllocator.freeAll();

	localsStack.remove(localsStack.size() - 1);
    }

    public Reg registerFor(SYMBOL symbol) {
	for (int i = localsStack.size() - 1; i >= 0; i--) {
	    InnerManager context = localsStack.get(i);
	    if (context.hasLocal(symbol)) {
		return context.getRegister(symbol);
	    }
	}
	throw new IllegalArgumentException("unknown symbol");
    }

    public void checkLength(Reg array, Reg index) {
	Reg temp = newRegister();
	IRCommand_Label outOfBounds = newLabel("outOfBounds"), inBounds = newLabel("inBounds");


	command(new IRCommand_BinopRC(temp, array, Operation.Plus, ARR_LENGTH_OFFSET));
	command(new IRCommand_Load(temp, temp));
	command(new IRCommand_Binop(temp, temp, Operation.GT, index));
	command(new IRCommand_Jump_If_Eq_To_Zero(temp, outOfBounds));
	command(new IRCommand_BinopRC(temp, index, Operation.GT, -1));
	command(new IRCommand_Jump_If_Eq_To_Zero(temp, outOfBounds));
	command(new IRCommand_Goto(inBounds));
	label(outOfBounds);
	command(new IRCommand_Call(BASIC_FUNCTION_ERROR_OUT_OF_BOUNDS));
	label(inBounds);
    }

    public IRCommand_Label functionLabelFor(SYMBOL symbol) {
	for (int i = localsStack.size() - 1; i >= 0; i--) {
	    InnerManager context = localsStack.get(i);
	    if (context.hasFunction(symbol)) {
		return context.getFunction(symbol);
	    }
	}
	throw new IllegalArgumentException("unknown symbol");
    }


    public void checkNotNull(Reg register) {
	Reg temp = newRegister();
	command(new IRCommand_BinopRC(temp, register, Operation.EQ, NIL_VALUE));
	IRCommand_Label notNull = newLabel("notnull");
	command(new IRCommand_Jump_If_Eq_To_Zero(temp, notNull));
	command(new IRCommand_Call(BASIC_FUNCTION_ERROR_INVALID_POINTER));
	label(notNull);
    }

    public void checkNotNullForArray(Reg register) {
	Reg temp = newRegister();
	command(new IRCommand_BinopRC(temp, register, Operation.EQ, NIL_VALUE));
	IRCommand_Label notNull = newLabel("notnull");
	command(new IRCommand_Jump_If_Eq_To_Zero(temp, notNull));
	command(new IRCommand_Call(BASIC_FUNCTION_ERROR_INVALID_POINTER));
	label(notNull);
    }

    public Reg malloc(Reg size) {
	command(new IRCommand_Push(size));
	command(new IRCommand_Call(BASIC_FUNCTION_MALLOC));
	Reg temp = newRegister();
	command(new IRCommand_Pop(temp));
	return temp;
    }

    public void exit() {
	command(new IRCommand_Call(BASIC_FUNCTION_EXIT));
    }


    public void assignVTable(Reg thisReg, TYPE_CLASS cls) {
	Reg offseted = newRegister(), temp = newRegister();
	command(new IRCommand_BinopRC(offseted, thisReg, Operation.Plus, VTABLE_OFFSET_IN_OBJECT));
	command(new IRCommand_Load_From_Label(temp, generateVTableLabelFor(cls)));
	command(new IRCommand_Store(offseted, temp));
	command(new IRCommand_BinopRC(offseted, thisReg, Operation.Plus, ID_OFFSET_IN_OBJECT));
	command(new IRCommand_Const(temp, classTables.get(cls).id));
	command(new IRCommand_Store(offseted, temp));
    }
    
    public IRCommand_Label constructorOf(TYPE_CLASS cls) {
	return new IRCommand_Label("_CTOR_" + cls.name).startingLabel();
    }

    public IRCommand_Label internalConstructorOf(TYPE_CLASS cls) {
	return new IRCommand_Label("_INTERNAL_CTOR_" + cls.name).startingLabel();
    }

    public IRCommand_Label constructorOf(TYPE_ARR array) {
	return new IRCommand_Label("_CTOR_ARR_" + array.arrT.name).startingLabel();
    }

    public IRCommand_Label returnLabelForConstructor(TYPE_ARR array) {
	return new IRCommand_Label("_RETURN_CTOR_ARR_" + array.arrT.name);
    }

    public IRCommand_Label returnLabelForConstructor(TYPE_CLASS cls) {
	return new IRCommand_Label("_RETURN_CTOR_" + cls.name);
    }

    public IRCommand_Label returnLabelForInternalConstructor(TYPE_CLASS cls) {
	return new IRCommand_Label("_RETURN_INTERNAL_CTOR_" + cls.name);
    }

    public IRCommand_Label returnLabelFor(SYMBOL symbol) {
	return new IRCommand_Label("_RETURN_" + symbol.toString());
    }

    public IRCommand_Label returnLabelForPreMainFunction(String fieldName) {
	return new IRCommand_Label("_RETURN_PRE_MAIN_" + fieldName);
    }

    public static boolean isReturnLabel( IRCommand_Label label) {
	return label != null && label.toString().startsWith("_RETURN_");
    }

    private static IRCommand_Label generateFunctionLabelFor(SYMBOL symbol) {
	if (symbol.isBounded()) {
	    return new IRCommand_Label("_GEN_FUNC_" + symbol.instance.name + "_" + symbol.name).startingLabel();
	} else
	    return new IRCommand_Label("_GEN_FUNC_" + symbol.name).startingLabel();
    }

    private IRCommand_Label generateVTableLabelFor(TYPE_CLASS cls) {
	return new IRCommand_Label("_VTABLE_" + classTables.get(cls).id + "_" + cls.name);
    }

    private InnerManager currentContext() {
	return localsStack.get(localsStack.size() - 1);
    }

    public enum ScopeType {Global, Class, Function, Inner}

    private class InnerManager {
	
	private String name;
	private Map<SYMBOL, Reg> locals = new HashMap<>();
	private Map<SYMBOL, IRCommand_Label> functions = new HashMap<>();
	private ScopeType scopeType;
	private RegisterAllocator registerAllocator;
	private LabelGenerator labelGenerator;

	InnerManager(String name, RegisterAllocator registerAllocator, LabelGenerator labelGenerator, ScopeType scopeType) {
	    this.name = name;
	    this.registerAllocator = registerAllocator;
	    this.labelGenerator = labelGenerator;
	    this.scopeType = scopeType;
	}

	void addLocal(SYMBOL symbol, Reg register) {
	    locals.put(symbol, register);
	}

	boolean hasLocal( SYMBOL symbol) {
	    return symbol != null && locals.containsKey(symbol);
	}

	void addFunction(SYMBOL symbol, IRCommand_Label label) {
	    functions.put(symbol, label);
	}

	boolean hasFunction( SYMBOL symbol) {
	    return symbol != null && functions.containsKey(symbol);
	}

	boolean isClassScope() {
	    return scopeType == ScopeType.Class;
	}

	
	Reg getRegister(SYMBOL symbol) {
	    return locals.get(symbol);
	}

	
	IRCommand_Label getFunction(SYMBOL symbol) {
	    return functions.get(symbol);
	}


    }



    public void setCommands(List<IRCommand> commands) {
	this.commands = commands;
    }

    public Map<IRCommand_Label, List<IRCommand_Label>> getVTables() {
	Map<IRCommand_Label, List<IRCommand_Label>> labels = new HashMap<>();
	classTables.forEach((cls, classTable) ->
			    labels.put(generateVTableLabelFor(cls),
				       classTable
				       .getFullVtable()
				       .stream()
				       .map(Manager::generateFunctionLabelFor)
				       .collect(Collectors.toList())));
	return labels;
    }


    public Map<SYMBOL, Reg> getGlobals() {
	Map<SYMBOL, Reg> globals = new HashMap<>();
	InnerManager context = localsStack.get(0);
	for (SYMBOL symbol : context.locals.keySet()) {
	    if (symbol.isField()) {
		Reg r = context.locals.get(symbol);
		globals.put(symbol, r);
	    }
	}
	return globals;
    }

}
