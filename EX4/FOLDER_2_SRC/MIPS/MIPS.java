package MIPS;

import java.io.*;

import IR.ANALYSIS.*;
import IR.COMMAND.*;
import IR.REG.*;
import IR.*;

import SYMBOL_TABLE.*;
import java.util.*;
import java.util.stream.*;

public class MIPS {
    private int BYTE_COUNT = Manager.BYTE_COUNT;
    private int TEMP_COUNT = 8;
    private int TEMP_OFFSET = 8;
    private int SKIP = 2;
    private PrintWriter file_writer;

    private String dataSection = "";
    private String codeSection = "";
    private List<String> codeSectionList = new ArrayList<>();
    private Map<Reg, IRCommand_Label> globals = new HashMap<>();
    private Map<String, Integer> functionIds = new HashMap<>();
    private Map<Reg, Integer> regs;
    private int parametersCount;
    private int localsCount;
    private int functionIdCounter = 0;
    private int boundedLabelCounter = 0;

    private String INDENT = "\t";
    private String BREAK = "\n";


    private int SYSCALL_INT = 1;
    private int SYSCALL_STRING = 4;
    private int SYSCALL_MALLOC = 9;
    private int SYSCALL_EXIT = 10;
    private int SYSCALL_CHAR = 11;


    private IRCommand_Label STRING_EQUALS = new IRCommand_Label("_STRING_EQUALS_");
    private IRCommand_Label STRING_CONCAT = new IRCommand_Label("_STRING_CONCAT_");
    private IRCommand_Label STRING_COMPARE_LOOP = new IRCommand_Label("_STRING_COMPARE_LOOP_");
    private IRCommand_Label STRING_COMPARE_EQUAL = new IRCommand_Label("_STRING_COMPARE_EQUAL_");
    private IRCommand_Label STRING_COMPARE_NEQ = new IRCommand_Label("_STRING_COMPARE_NEQ_");
    private IRCommand_Label STRING_COMPARE_AFTER = new IRCommand_Label("_STRING_COMPARE_AFTER_");
    private IRCommand_Label STRING_COMPARE_FST_NNULL = new IRCommand_Label("_STRING_COMPARE_FST_NNULL_");
    private IRCommand_Label STRING_COMPARE_SND_NNULL = new IRCommand_Label("_STRING_COMPARE_SND_NNULL_");
    private IRCommand_Label STRING_CONCAT_FST_PRE = new IRCommand_Label("_STRING_CONCAT_FST_PRE_");
    private IRCommand_Label STRING_CONCAT_FST_POST = new IRCommand_Label("_STRING_CONCAT_FST_POST_");
    private IRCommand_Label STRING_CONCAT_SND_PRE = new IRCommand_Label ("_STRING_CONCAT_SND_PRE_");
    private IRCommand_Label STRING_CONCAT_SND_POST = new IRCommand_Label("_STRING_CONCAT_SND_POST_");
    private IRCommand_Label STRING_CONCAT_FST_PRE_WRITE = new IRCommand_Label("_STRING_CONCAT_FST_PRE_WRITE_");
    private IRCommand_Label STRING_CONCAT_FST_POST_WRITE = new IRCommand_Label("_STRING_CONCAT_FST_POST_WRITE_");
    private IRCommand_Label STRING_CONCAT_SND_PRE_WRITE = new IRCommand_Label("_STRING_CONCAT_SND_PRE_WRITE_");
    private IRCommand_Label STRING_CONCAT_SND_POST_WRITE = new IRCommand_Label("_STRING_CONCAT_SND_POST_WRITE_");
    private IRCommand_Label MALLOC_ZERO_COND = new IRCommand_Label("_MALLOC_ZERO_COND_");
    private IRCommand_Label MALLOC_ZERO_AFTER = new IRCommand_Label("_MALLOC_ZERO_AFTER_");
    
    private IRCommand_Label FUNCTION_NAMES = new IRCommand_Label("FUNCs");
    private String FUNCTION_NAME_PREFIX = "FUNC_";
    private String MAIN = "main";


    private int $0 = 0; // always 0
    private int $at = 1; // reserved ASSEMBLY
    private int $v0 = 2; // result
    private int $v1 = 3; // result
    private int $a0 = 4; // argument
    private int $a1 = 5; // argument
    private int $a2 = 6; // argument
    private int $a3 = 7; // argument
    private int $t0 = 8; // temporary
    private int $t1 = 9; // temporary
    private int $t2 = 10; // temporary
    private int $t3 = 11; // temporary
    private int $t4 = 12; // temporary
    private int $t5 = 13; // temporary
    private int $t6 = 14; // temporary
    private int $t7 = 15; // temporary
    private int $s0 = 16; // saved
    private int $s1 = 17; // saved
    private int $s2 = 18; // saved
    private int $s3 = 19; // saved
    private int $s4 = 20; // saved
    private int $s5 = 21; // saved
    private int $s6 = 22; // saved
    private int $s7 = 23; // saved
    private int $t8 = 24; // temporary
    private int $t9 = 25; // temporary
    private int $k0 = 26; // reserved OS
    private int $k1 = 27; // reserved OS
    private int $gp = 28; // global pointer
    private int $sp = 29; // stack pointer
    private int $fp = 30; // frame pointer
    private int $ra = 31; // return address

    private String regName(int reg) {
	switch (reg) {
	case  0 : return "$0"; // always 0
	case  1 : return "$at"; // reserved ASSEMBLY
	case  2 : return "$v0"; // result
	case  3 : return "$v1"; // result
	case  4 : return "$a0"; // argument
	case  5 : return "$a1"; // argument
	case  6 : return "$a2"; // argument
	case  7 : return "$a3"; // argument
	case  8 : return "$t0"; // temporary
	case  9 : return "$t1"; // temporary
	case  10: return "$t2"; // temporary
	case  11: return "$t3"; // temporary
	case  12: return "$t4"; // temporary
	case  13: return "$t5"; // temporary
	case  14: return "$t6"; // temporary
	case  15: return "$t7"; // temporary
	case  16: return "$s0"; // saved
	case  17: return "$s1"; // saved
	case  18: return "$s2"; // saved
	case  19: return "$s3"; // saved
	case  20: return "$s4"; // saved
	case  21: return "$s5"; // saved
	case  22: return "$s6"; // saved
	case  23: return "$s7"; // saved
	case  24: return "$t8"; // temporary
	case  25: return "$t9"; // temporary
	case  26: return "$k0"; // reserved OS
	case  27: return "$k1"; // reserved OS
	case  28: return "$gp"; // global pointer
	case  29: return "$sp"; // stack pointer
	case  30: return "$fp"; // frame pointer
	case  31: return "$ra"; // return address
	}
	return "$?";
    }

    private void writeFlush(String s) {
	file_writer.write(s);
	file_writer.flush();
    }

    public void writeFile() {
	writeFlush(".data" + BREAK + dataSection + BREAK + ".text" + BREAK);
	for (String c : codeSectionList) {
	    writeFlush(c);
	}
        writeFlush(codeSection + BREAK);
    }


    public String to_str() {
	StringBuilder output = new StringBuilder();
	output.append(".data" + BREAK + dataSection + BREAK + ".text" + BREAK);
	for (String c : codeSectionList) {
	    output.append(c);
	}
        output.append(codeSection + BREAK);
	return output + "";
    }
    
    private String word(String s) {
	return ": .word " + s;
    }
    
    private String asciiz(String s) {
	return ": .asciiz \"" + s + "\"";
    }

    private String symGLabel(SYMBOL sym) {
	return "_global_" + sym.name;
    }

    private String func(int i) {
	return "_func_" + i;
    }

    private String comment(String s) {
	return "# " + s;
    }

    
    public MIPS(Map<String, IRCommand_Label> constantStrings, Map<IRCommand_Label, List<IRCommand_Label>> virtualTables, Map<SYMBOL, Reg> globals, List<IRCommand> commands, PrintWriter file_writer) {
	this.file_writer = file_writer;
	commentLine("constants data: ");
        constantStrings
	    .forEach((constant, label) ->
		     {
			 dataSection += label.name + this.asciiz(constant) + comment(label.name + " constant string") + BREAK;
		     }
		     );

	commentLine("vtable data: ");
        virtualTables
	    .forEach((label, methods) ->
		     {
			 dataSection += label.name + this.word(methods.isEmpty() ? "0" : methods.stream().map(l -> l.name).collect(Collectors.joining(","))) + comment(label.name + " virtual table") + BREAK;
		     }
		     );
	
	commentLine("globals data: ");
	globals
	    .forEach((symbol, register) ->
		     {
			 dataSection += symGLabel(symbol) + word("0") + comment(symbol.name + " global symbol") + BREAK;
			 this.globals.put(register, new IRCommand_Label(symGLabel(symbol)));
		     });

	commentLine("for simulator without main starter: ");
        jump(new IRCommand_Label(MAIN));

	commentLine("adding main to functions: ");
        functionIds.put(MAIN, functionIdCounter++);

	commentLine("adding standard libraries: ");
	errorThrow(Manager.BASIC_FUNCTION_ERROR_DIVISION_BY_ZERO,"Division By Zero");
	errorThrow(Manager.BASIC_FUNCTION_ERROR_INVALID_POINTER,"Invalid Pointer Dereference");
	errorThrow(Manager.BASIC_FUNCTION_ERROR_OUT_OF_BOUNDS,"Access Violation");
	writeExit();
	writePrintInt();
	writePrintString();
	writeMalloc();
	writeStringEquality();
	writeStringConcat();

	commentLine("adding commands: ");
        loadCmds(commands);

	commentLine("record function names: ");
        writeFuncNames();
    }

    
    private void errorThrow(IRCommand_Label errLabel, String errStr) {
	commentHeader(errStr + ": ");
	String errStrNoSpaces = errStr.replaceAll(" ","");
	    dataSection += errStrNoSpaces + asciiz(errStr) + BREAK;
	label(errLabel);
	syscallPrintString(new IRCommand_Label(errStrNoSpaces));
	syscallExit();
	commentFooter();
    }

    private void writeExit() {
	commentHeader("exit: ");
	label(Manager.BASIC_FUNCTION_EXIT);
	syscallExit();
	commentFooter();
    }

    private void writePrintInt() {
	commentHeader("PrintInt: ");
	label(Manager.BASIC_FUNCTION_PRINT_INT);

	commentLine("int to print: ");
	pop($a0);

	commentLine("make INT system call: ");
	constant($v0, SYSCALL_INT);
	syscall();

	commentLine("print an extra whitespace: ");
	syscallPrintChar(' ');

	commentLine("return: ");
	jumpRegister($ra);
	
	commentFooter();
    }

    private void writePrintString() {
	commentHeader("PrintString: ");
	label(Manager.BASIC_FUNCTION_PRINT_STRING);

	commentLine("int to print: ");
	pop($a0);

	commentLine("make STRING system call: ");
	constant($v0, SYSCALL_STRING);
	syscall();

	commentLine("return: ");
	jumpRegister($ra);
	
	commentFooter();
    }

    private void writeMalloc() {
	commentHeader("Malloc: ");
	label(Manager.BASIC_FUNCTION_MALLOC);

	commentLine("keep size: ");
	pop($t8);

	commentLine("size to malloc: ");
	move($a0, $t8);

	commentLine("make MALLOC system call: ");
	constant($v0, SYSCALL_MALLOC);
	syscall();

	commentLine("prep for zero out space: ");
	selfAddConst($v0, -BYTE_COUNT);
	binop($t8, $v0, Operation.Plus, $t8);

	commentLine("zero out space: ");
	label(MALLOC_ZERO_COND);
	commentLine("check if all zeroed out: ");
	branchEq($v0, $t8, MALLOC_ZERO_AFTER);

	commentLine("zero one single and step once: ");
	storeToMemory($t8, $0);
	selfAddConst($t8, -BYTE_COUNT);
	jump(MALLOC_ZERO_COND);

	commentLine("if all zeroed out: ");
	    label(MALLOC_ZERO_AFTER);

	commentLine("restore value before zero out: ");
	selfAddConst($v0, BYTE_COUNT);

	commentLine("return: ");
	jumpRegister($ra);
	
	commentFooter();
    }

    private void writeStringEquality() {
	commentHeader("String Equality: ");
	label(STRING_EQUALS);

	int str1 = $t8;
	int str2 = $t9;
	int tmp1 = $t1;
	int tmp2 = $t2;

	commentLine("keep string locs in the extra temporaries: ");
	pop(str2);
	pop(str1);

	commentLine("backup two regular temporaries to free them for use: ");
	push(tmp1);
	push(tmp2);

	commentLine("check if 1st string is null: ");
	constant(tmp1, Manager.NIL_VALUE);
	branchNeq(str1, tmp1, STRING_COMPARE_FST_NNULL);

	commentLine("1st string is null, return nullness of 2nd string: ");
	binop($v0, str2, Operation.EQ, tmp1);
	jump(STRING_COMPARE_AFTER);

	label(STRING_COMPARE_FST_NNULL);
	commentLine("1st string is not null, check if 2nd string is null: ");
	branchNeq(str2, tmp1, STRING_COMPARE_SND_NNULL);

	commentLine("only 2nd string is null, so -false-: ");
	move($v0, $0); /// I think this is a bug!!! should be 1 (ie false)
	jump(STRING_COMPARE_AFTER);
	
	label(STRING_COMPARE_SND_NNULL);
	commentLine("both are not null, start compare: ");
	
	label(STRING_COMPARE_LOOP);
	commentLine("load single byte for each: ");
	loadByteFromMemory(tmp1, str1);
	loadByteFromMemory(tmp2, str2);

	commentLine("check if single byte is equal: ");
	branchNeq(tmp1, tmp2, STRING_COMPARE_NEQ);
	
	commentLine("single byte is equal, check if it is termination byte: ");
	branchEq(tmp1, $0, STRING_COMPARE_EQUAL);
	
	commentLine("single byte is equal, but still more to go: ");
	selfAddConst(str1, 1);
	selfAddConst(str2, 1);
	jump(STRING_COMPARE_LOOP);
	
	label(STRING_COMPARE_EQUAL);
	commentLine("single byte of both is termination, so -true-: ");
	move($v0, $0);
	jump(STRING_COMPARE_AFTER);

	label(STRING_COMPARE_NEQ);
	commentLine("single byte is not equal, so -false-: ");
	constant($v0, 1);
	jump(STRING_COMPARE_AFTER);
	
	label(STRING_COMPARE_AFTER);
	commentLine("restore temporaries: ");
	pop(tmp2);
	pop(tmp1);

	commentLine("return: ");
	jumpRegister($ra);
	
	commentFooter();
    }

    private void writeStringConcat() {
	commentHeader("String Concatination: ");
	label(STRING_CONCAT);

	int str1 = $t8;
	int str2 = $t9;
	int size = $t0;
	int tmp1 = $t1;
	int tmp2 = $t2;

	commentLine("keep string locs in the extra temporaries: ");
	pop(str2);
	pop(str1);

	commentLine("backup three regular temporaries to free them for use: ");
	push(size);
	push(tmp1);
	push(tmp2);

	commentLine("calculate length of first string");
	move(size, $0);
	move(tmp1, str1);

	label(STRING_CONCAT_FST_PRE);
	commentLine("check if single byte of 1st is termination: ");
	loadByteFromMemory(tmp2, tmp1);
	branchEq(tmp2, $0, STRING_CONCAT_FST_POST);
	
	commentLine("single byte of 1st is not termination, keep going: ");
	selfAddConst(size, 1);
	selfAddConst(tmp1, 1);
	jump(STRING_CONCAT_FST_PRE);

	label(STRING_CONCAT_FST_POST);
	commentLine("single byte of 1st is termination, repeat for 2nd: ");
	move(tmp1, str2);
	label(STRING_CONCAT_SND_PRE);
	loadByteFromMemory(tmp2, tmp1);
	branchEq(tmp2, $0, STRING_CONCAT_SND_POST);
	selfAddConst(size, 1);
	selfAddConst(tmp1, 1);
	jump(STRING_CONCAT_SND_PRE);

	label(STRING_CONCAT_SND_POST);
	commentLine("now we have the length of the concatination - start space alloc: ");
	
	commentLine("space alloc 4*: ");
	selfAddConst(size, 5);
	andConst(size, size, "0xFFFC");

	commentLine("backup regs prior to malloc: ");
	push($ra);
	push(str1);
	push(str2);
	push(size);

	commentLine("feed the argument through mem and call malloc: ");
	push(size);
	jumpAndLink(Manager.BASIC_FUNCTION_MALLOC);

	commentLine("restore regs after malloc: ");
	pop(size);
	pop(str2);
	pop(str1);
	pop($ra);

	commentLine("writing string to alloc, starting with empty string: ");
	move(tmp2, $v0);
	label(STRING_CONCAT_FST_PRE_WRITE);
	commentLine("check if single byte of 1st is termination: ");
	loadByteFromMemory(tmp1, str1);
	branchEq(tmp1, $0, STRING_CONCAT_FST_POST_WRITE);
	
	commentLine("single byte of 1st is not termination, keep going: ");
	storeByteToMemory(tmp2, tmp1);
	selfAddConst(tmp2, 1);
	selfAddConst(str1, 1);
	jump(STRING_CONCAT_FST_PRE_WRITE);

	label(STRING_CONCAT_FST_POST_WRITE);
	commentLine("single byte of 1st is termination, repeat for 2nd: ");
	
	label(STRING_CONCAT_SND_PRE_WRITE);	
	loadByteFromMemory(tmp1, str2);
	branchEq(tmp1, $0, STRING_CONCAT_SND_POST_WRITE);
	storeByteToMemory(tmp2, tmp1);
	selfAddConst(tmp2, 1);
	selfAddConst(str2, 1);
	jump(STRING_CONCAT_SND_PRE_WRITE);
	
	label(STRING_CONCAT_SND_POST_WRITE);
	/// perhaps another bug? - if both strings are empty...
	commentLine("add termination: ");
	storeByteToMemory(tmp2, $0);

	commentLine("restore original registers");
	pop(tmp2);
	pop(tmp1);
	pop(size);

	commentLine("return: ");
	jumpRegister($ra);
	
	commentFooter();
    }

    private void writeFuncNames() {
	List<String> names =
	    functionIds
	    .entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue())
	    .map(Map.Entry::getKey)
	    .collect(Collectors.toList());
	List<String> labels = new ArrayList<>(names.size());
	for (int i = 0; i < names.size(); i++) {
	    String label = func(i);
	    dataSection += label + this.asciiz(names.get(i)) + BREAK;
	    labels.add(label);
	}
	dataSection += FUNCTION_NAMES + this.word(String.join(",", labels)) + BREAK;
    }

 

    private void loadCmds(List<IRCommand> commands) {
	Partition partitions = new Partition(commands);
        partitions
	    .juice()
	    .stream()
	    .filter(IRBlock::isStartingBlock)
	    .collect(Collectors.toList())
	    .forEach(this::loadFunction);
    }

    private void loadFunction(IRBlock functionStartingBlock) {
        IRCommand_Func functionInfo = (IRCommand_Func) functionStartingBlock.getFirstCommand();
        regs = IRBlock.colorizor(functionStartingBlock.getAllReachables());
        this.parametersCount = functionInfo.paramsCount;
        this.localsCount = functionInfo.localsCount;
        regs.entrySet().forEach(e -> e.setValue(e.getValue() + TEMP_OFFSET));
        writeFunctionHeader(functionStartingBlock.label, functionInfo.paramsCount, functionInfo.localsCount, functionIds.computeIfAbsent(functionInfo.name, n -> functionIdCounter++));
        IRBlock currentBlock = functionStartingBlock;
        do {
            if (Manager.isReturnLabel(currentBlock.label)) {
                writeFunctionFooter(currentBlock.label, functionInfo.paramsCount, functionInfo.localsCount);
                break;
            } else {
                if (currentBlock != functionStartingBlock && currentBlock.label != null) {
                    label(currentBlock.label);
                }
                currentBlock.commands.forEach(lcmd -> writeCommand(lcmd.command));
            }
            currentBlock = currentBlock.succ;
        } while (currentBlock != null && !currentBlock.isStartingBlock());
    }


    private void writeFunctionHeader(IRCommand_Label label, int parameters, int locals, int functionId) {
	label(label);
	
	commentHeader("function header: " + label.name);
	
	for (int i = 0; i < TEMP_COUNT; i++) {
	    commentLine("register backup: " + i);
	    push(TEMP_OFFSET + i);
	}
	
	commentLine("save to stack - prev frame pointer: ");
	push($fp);
	
	commentLine("save to stack - return address: ");
	push($ra);

	commentLine("update frame pointer: ");
	move($fp, $sp);
	
	for (int i = 0; i < locals; i++) {
	    commentLine("save to stack - default value for stack-local: " + i);
	    pushConst(0);
	}
	
	commentLine("save to stack - default value for return: ");
	pushConst(0);

	commentLine("save to stack - function id");
	pushConst(functionId);
	
	commentLine("save to stack - size of header");
	pushConst((TEMP_COUNT + SKIP + parameters + locals + 3) * BYTE_COUNT);

	commentFooter();
    }

    private void writeFunctionFooter(IRCommand_Label label, int parameters, int locals) {
	label(label);
	
	commentHeader("function footer: " + label.name);

	commentLine("remove - header size + function id: ");
	selfAddConst($sp, 2 * BYTE_COUNT);
	
	commentLine("save - return value: ");
	pop($v0);

	commentLine("go to return address: ");
	selfAddConst($sp, locals * BYTE_COUNT);

	commentLine("save - return address: ");
	pop($ra);
	
	commentLine("save - old frame pointer: ");
	pop($fp);

	for (int i = TEMP_COUNT - 1; i >= 0; i--) {
	    commentLine("register restore: " + i);
	    pop(TEMP_OFFSET + i);
	}

	commentLine("remove - parameters");
	selfAddConst($sp, (parameters * BYTE_COUNT));

	commentLine("return: ");
	jumpRegister($ra);

	commentFooter();
    }

    private void writeCommand(IRCommand command) {
	if (command instanceof IRCommand_Binop) {
	    binopCommand(((IRCommand_Binop) command));
	} else if (command instanceof IRCommand_BinopRC) {
	    binopRCCommand(((IRCommand_BinopRC) command));
	} else if (command instanceof IRCommand_Const) {
	    constCommand(((IRCommand_Const) command));
	} else if (command instanceof IRCommand_Set) {
	    setValueCommand(((IRCommand_Set) command));
	} else if (command instanceof IRCommand_Goto) {
	    gotoCommand(((IRCommand_Goto) command));
	} else if (command instanceof IRCommand_Jump_If_Not_Eq_To_Zero) {
	    ifnzCommand(((IRCommand_Jump_If_Not_Eq_To_Zero) command));
	} else if (command instanceof IRCommand_Jump_If_Eq_To_Zero) {
	    ifzCommand(((IRCommand_Jump_If_Eq_To_Zero) command));
	} else if (command instanceof IRCommand_Call) {
	    callCommand(((IRCommand_Call) command));
	} else if (command instanceof IRCommand_Call_Reg) {
	    callRegisterCommand(((IRCommand_Call_Reg) command));
	} else if (command instanceof IRCommand_Pop) {
	    popCommand(((IRCommand_Pop) command));
	} else if (command instanceof IRCommand_Push) {
	    pushCommand(((IRCommand_Push) command));
	} else if (command instanceof IRCommand_Push_Const) {
	    pushConstCommand(((IRCommand_Push_Const) command));
	}else if (command instanceof IRCommand_Load) {
	    loadCommand(((IRCommand_Load) command));
	} else if (command instanceof IRCommand_Load_From_Label) {
	    loadAddressFromLabelCommand(((IRCommand_Load_From_Label) command));
	} else if (command instanceof IRCommand_Store) {
	    storeCommand(((IRCommand_Store) command));
	}
    }

    private void gotoCommand(IRCommand_Goto command) {
	commentLine("cmd- goto: ");
	jump(command.label);
    }

    private void ifnzCommand(IRCommand_Jump_If_Not_Eq_To_Zero command) {
	commentLine("cmd- if not zero: ");
	int register = prepReg8(command.cond);
	branchNeq(register, $0, command.label);
    }

    private void ifzCommand(IRCommand_Jump_If_Eq_To_Zero command) {
	commentLine("cmd- if zero: ");
	int register = prepReg8(command.cond);
	branchEq(register, $0, command.label);
    }

    private void loadCommand(IRCommand_Load command) {
	commentLine("cmd- load: ");
	int source = prepReg8(command.source);
	loadFromMemory($t8, source);
	setReg(command.dest, $t8);
    }

    private void loadAddressFromLabelCommand(IRCommand_Load_From_Label command) {
	int dest = prepReg8(command.dest);
	loadAddress(dest, command.label);
    }

    private void storeCommand(IRCommand_Store command) {
	int source = prepReg8(command.source);
	if (!regReusable(command.dest) && source == $t8) {
	    move($t9, source);
	    source = $t9;
	}
	int dest = prepReg8(command.dest);
	storeToMemory(dest, source);
    }

    private void binopCommand(IRCommand_Binop command) {
	int left = prepReg8(command.t1);

	if (regReusable(command.t2)) {
	    if (regReusable(command.dest)) {
		binop(prepReg8(command.dest), left, command.op, prepReg8(command.t2));
	    } else {
		binop($t9, left, command.op, prepReg8(command.t2));
		setReg(command.dest, $t9);
	    }
	} else {
	    int right;
	    right = prepReg(command.t2, $t9);

	    if (regReusable(command.dest)) {
		binop(prepReg8(command.dest), left, command.op, right);
	    } else {
		binop($t9, left, command.op, right);
		setReg(command.dest, $t9);
	    }
	}
    }
    
    private void binopRCCommand(IRCommand_BinopRC command) {
        int left = prepReg8(command.r);
        int dest = regReusable(command.dest) ? prepReg8(command.dest) : $t9;
        switch (command.op) {
            case Plus:
                addConst(dest, left, command.k);
                break;
            case Minus:
                addConst(dest, left, -command.k);
                break;
            case Times:
                multiplyByConst(dest, left, command.k, $t9);
                break;
            case LT:
                setOnLTConst(dest, left, command.k);
                break;
            case EQ:
                equalToConst(dest, left, command.k);
                break;
            case Divide:
                if (command.k == 0) {
                    jump(Manager.BASIC_FUNCTION_ERROR_DIVISION_BY_ZERO);
                    break;
                }
            case GT:
            case Concat:
            case StrEQ:
                constant($t9, command.k);
                binop(dest, left, command.op, $t9);
                break;
        }
	setReg(command.dest, dest);
    }


    private void constCommand(IRCommand_Const command) {
	if (regReusable(command.dest)) {
	    constant(prepReg8(command.dest), command.value);
	} else {
	    constant($t8, command.value);
	    setReg(command.dest, $t8);
	}
    }

    private void setValueCommand(IRCommand_Set command) {
	if (regReusable(command.dest)) {
	    if (regReusable(command.source)) {
		move(prepReg8(command.dest), prepReg8(command.source));
	    } else {
		prepReg(command.source, prepReg8(command.dest));
	    }
	} else {
	    int source = prepReg8(command.source);
	    setReg(command.dest, source);
	}
    }

    private void callCommand(IRCommand_Call command) {
	jumpAndLink(command.label);
    }

    private void callRegisterCommand(IRCommand_Call_Reg command) {
	int reg = prepReg8(command.func);
	jumpAndLinkRegister(reg);
    }

    private void popCommand(IRCommand_Pop command) {
	if (!command.dest.isTemp() || regReusable(command.dest)) {
	    setReg(command.dest, $v0);
	}
    }

    private void pushCommand(IRCommand_Push command) {
	int reg = prepReg8(command.source);
	push(reg);
    }

    private void pushConstCommand(IRCommand_Push_Const command) {
	pushConst(command.con);
    }


    private boolean regReusable(Reg register) {
	return false; //regs.containsKey(register);
    }

    private int prepReg8(Reg register) {
	return prepReg(register, $t8);
    }

    private int prepReg(Reg register, int temp) {
	if (regReusable(register)) {
	    return regs.get(register);
	} else if (register instanceof TmpReg) {
	    return temp;
	} else if (register instanceof ParamReg) {
	    loadParam(temp, register.getId(), parametersCount);
	} else if (register instanceof LocalReg) {
	    loadLocal(temp, register.getId());
	} else if (register instanceof CurrentReg) {
	    loadThis(temp, parametersCount);
	} else if (register instanceof RetReg) {
	    loadLocal(temp, localsCount);
	} else if (register instanceof GlobalReg) {
	    loadGlobalVariable(temp, register);
	} else {}
	return temp;
    }

    private void setReg(Reg dest, int src) {
	if (regReusable(dest)) {
	    int realRegister = regs.get(dest);
	    if (realRegister != src)
		move(realRegister, src);
	} else {
	    if (src == $t8) {
		move($t9, src);
		src = $t9;
	    }
	    if (dest instanceof TmpReg) {
	    } else if (dest instanceof ParamReg) {
		storeParam(src, dest.getId(), parametersCount);
	    } else if (dest instanceof LocalReg) {
		storeLocal(src, dest.getId());
	    } else if (dest instanceof CurrentReg) {
		storeThis(src, parametersCount);
	    } else if (dest instanceof RetReg) {
		storeLocal(src, localsCount);
	    } else if (dest instanceof GlobalReg) {
		storeGlobalVariable(src, dest);
	    } else {}
	}
    }

    private int localOffset(int id) {
	return -(id + 1) * BYTE_COUNT;
    }

    private int parameterOffset(int id, int parametersCount) {
	return (TEMP_COUNT + SKIP + parametersCount - (id + 1)) * BYTE_COUNT;
    }

    private void loadGlobalVariable(int dest, Reg register) {
	loadFromMemory(dest, globals.get(register));
    }

    private void storeGlobalVariable(int src, Reg register) {
	storeToMemory(src, globals.get(register));
    }

    private void loadLocal(int dest, int id) {
	loadOffsetedVariable(dest, localOffset(id));
    }

    private void loadParam(int dest, int id, int parametersCount) {
	loadOffsetedVariable(dest, parameterOffset(id, parametersCount));
    }

    private void loadThis(int dest, int parametersCount) {
	loadOffsetedVariable(dest, parameterOffset(0, parametersCount));
    }

    private void storeLocal(int srcReg, int id) {
	storeOffsetedVariable(srcReg, (id));
    }

    private void storeParam(int srcReg, int id, int parametersCount) {
	storeOffsetedVariable(srcReg, parameterOffset(id, parametersCount));
    }

    private void storeThis(int srcReg, int parametersCount) {
	storeOffsetedVariable(srcReg, parameterOffset(0, parametersCount));
    }

    private void loadOffsetedVariable(int dest, int offset) {
	addConst(dest, $fp, offset);
	loadFromMemory(dest, dest);
    }

    private void storeOffsetedVariable(int srcReg, int offset) {
	addConst($t8, $fp, offset);
	storeToMemory($t8, srcReg);
    }



    private void instrL(String ins, IRCommand_Label l) {
	codeSection += INDENT + ins + " " + l.name + BREAK;
    }

    private void instrRL(String ins, int r1, IRCommand_Label l) {
	codeSection += INDENT + ins + " " + regName(r1) + "," + l.name + BREAK;
    }
    
    private void instrR(String ins, int r1) {
	codeSection += INDENT + ins + " " + regName(r1) + BREAK;
    }

    private void instrRR(String ins, int r1, int r2) {
	codeSection += INDENT + ins + " " + regName(r1) + "," + regName(r2) + BREAK;
    }

    private void instrRRR(String ins, int r1, int r2, int r3) {
	codeSection += INDENT + ins + " " + regName(r1) + "," + regName(r2) + "," + regName(r3) + BREAK;
    }

    private void instrRRI(String ins, int r1, int r2, int i1) {
	codeSection += INDENT + ins + " " + regName(r1) + "," + regName(r2) + "," + i1 + BREAK;
    }
    private void instrRS(String ins, int r1, String s1) {
	codeSection += INDENT + ins + " " + regName(r1) + "," + s1 + BREAK;
    }
    private void instrRRS(String ins, int r1, int r2, String s1) {
	codeSection += INDENT + ins + " " + regName(r1) + "," + regName(r2) + "," + s1 + BREAK;
    }

    private void instrRO(String ins, int r1, int offset) {
	codeSection += INDENT + ins + " " + regName(r1) + ",(" + regName(offset) + ")" + BREAK;
    }

    private void instrROR(String ins, int r1, int offset, int r2) {
	codeSection += INDENT + ins + " " + regName(r1) + ",(" + regName(offset) + ")" + "," + regName(r2) + BREAK;
    }

    
    private void jump(IRCommand_Label l) {
	instrL("j", l);
    }

    private void jumpAndLink(IRCommand_Label l) {
	instrL("jal", l);
    }

    private void jumpRegister(int dest) {
	instrR("jr",dest);
    }

    private void jumpAndLinkRegister(int dest) {
	instrR("jalr",dest);
    }

    
    private void move(int target, int source) {
	instrRR("move",target,source);
    }



    private void loadAddress(int dest, IRCommand_Label label) {
	instrRL("la",dest,label);
    }

    private void loadByteFromMemory(int targetR, int sourceS) {
	instrRO("lb",targetR,sourceS);
    }

    private void storeByteToMemory(int targetS, int sourceR) {
	instrRO("sb",sourceR,targetS);
    }

    private void loadFromMemory(int targetR, int sourceS) {
	instrRO("lw",targetR,sourceS);
    }

    private void loadFromMemory(int reg, IRCommand_Label label) {
	instrRS("lw",reg,label.name);
    }

    private void storeToMemory(int targetS, int sourceR) {
	instrRO("sw",sourceR,targetS);
    }

    private void storeToMemory(int reg, IRCommand_Label label) {
	instrRS("sw",reg,label.name);
    }

    private void binop(int target, int left, Operation op, int right) {
	switch (op) {
	case Plus:
	    instrRRR("add",target,left,right);
	    break;
	case Minus:
	    instrRRR("sub",target,left,right);
	    break;
	case Times:
	    instrRR("mult",left,right);
	    instrR("mflo",target);
	    break;
	case Divide:
	    instrRR("div",left,right);
	    instrR("mflo",target);
	    break;
	case EQ:
	    commentLine("calc equality:");
	    instrRRR("subu",target,left,right);
	    instrRRR("sltu",target,$0,target);
	    instrRRI("xori",target,target,1);
	    break;
	case GT:
	    instrRRR("slt",target,right,left);
	    break;
	case LT:
	    instrRRR("slt",target,left,right);
	    break;
	case Concat:
	    push(left);
	    push(right);
	    jumpAndLink(STRING_CONCAT);
	    move(target, $v0);
	    break;
	case StrEQ:
	    push(left);
	    push(right);
	    jumpAndLink(STRING_EQUALS);
	    move(target, $v0);
	    break;
	}
    }

    private void equalToConst(int dest, int reg, int constant) {
	instrRRI("addiu",dest,reg,-constant);
	instrRRR("sltu",dest,$0,dest);
	instrRRI("xori",dest,$0,1);
    }

    private void multiplyByConst(int dest, int reg, int constant, int fallbackRegister) {
	if (constant != 1) {
	    if (constant == 0) {
		move(dest, $0);
	    } else {
		constant(fallbackRegister, constant);
		binop(dest, reg, Operation.Times, fallbackRegister);
	    }
	}
    }



    private void setOnLTConst(int dest, int reg, int constant) {
	instrRRI("slti",dest,reg,constant);
    }

    private void addConst(int dest, int reg, int constant) {
	if (constant != 0) {
	    instrRRI("addi",dest,reg,constant);
	}
	else if (dest != reg) {
	    move(dest, reg);
	}

    }

    private void andConst(int dest, int reg, String constant) {
	instrRRS("andi",dest,reg,constant);
    }

    private void constant(int reg, int constant) {
	if (constant == 0) {
	    move(reg, $0);
	} else {
	    instrRRI("addi",reg,$0,constant);
	}
    }


    private void selfAddConst(int reg, int constant) {
	addConst(reg, reg, constant);
    }



    private void push(int register) {
	selfAddConst($sp, -BYTE_COUNT);
	storeToMemory($sp, register);
    }

    private void pushConst(int constant) {
	selfAddConst($sp, -BYTE_COUNT);
	constant($t8, constant);
	storeToMemory($sp, $t8);
    }

    private void pop(int register) {
	loadFromMemory(register, $sp);
	selfAddConst($sp, BYTE_COUNT);
    }

    private void label(IRCommand_Label label) {
	codeSection += label.name + ":" + BREAK;
    }


    private void branchNeq(int reg1, int reg2, IRCommand_Label label) {
	codeSection += INDENT + "bne " + regName(reg1) + "," + regName(reg2) + "," + label + BREAK;
    }

    private void branchEq(int reg1, int reg2, IRCommand_Label label) {
	codeSection += INDENT + "beq " + regName(reg1) + "," + regName(reg2) + "," + label + BREAK;
    }

    private void syscall() {
	codeSection += INDENT + "syscall" + BREAK;
    }

    private void syscallPrintString(IRCommand_Label label) {
	loadAddress($a0, label);
	constant($v0, SYSCALL_STRING);
	syscall();
    }

    private void syscallExit() {
	constant($v0, SYSCALL_EXIT);
	syscall();
    }

    private void syscallPrintChar(char c) {
	constant($a0, (int) c);
	constant($v0, SYSCALL_CHAR);
	syscall();
    }

    private void commentLine(String s) {
	codeSection += "# " + s + BREAK;
	codeSectionList.add(codeSection);
	codeSection = "";
    }
    
    private void commentBreaker() {
	codeSection += "####################################" + BREAK;
    }
    
    private void commentHeader(String s) {
	commentLine(s);
	commentBreaker();
    }
    
    private void commentFooter() {
	commentBreaker();
    }
    
}
