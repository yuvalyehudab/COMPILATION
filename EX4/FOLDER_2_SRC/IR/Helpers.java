package IR;

import IR.REG.*;
import IR.COMMAND.*;

import SYMBOL_TABLE.*;
import TYPES.*;

import java.util.*;
import java.util.stream.*;

interface RegisterAllocator {
    Reg newRegister();

    void free(Reg register);

    void freeAll();
}

class SimpleRegisterAllocator implements RegisterAllocator {
    private int counter = 0;

    public Reg newRegister() {
        return new TmpReg(counter++);
    }

    public void free(Reg register) {
        if (register.isTemp() && register.getId() == counter - 1) {
            counter--;
        }
    }

    public void freeAll() {
        counter = 0;
    }
}


interface LabelGenerator {
    IRCommand_Label newLabel(String description);
}

class SimpleLabelGenerator implements LabelGenerator {
    private static int counter = 0;
    private String prefix;

    private SimpleLabelGenerator(String prefix) {
        this.prefix = prefix;
    }

    SimpleLabelGenerator() {
        this(null);
    }

    public IRCommand_Label newLabel(String description) {
        return new IRCommand_Label("_" + (prefix != null ? prefix + "_" : "") + description + "_" + counter++ + "_");
    }
}

class CLASS_TABLE {
    public final int id;
    private static final int OFFSET_CHANGE = Manager.BYTE_COUNT;
    private static int current_id = 1;
    private CLASS_TABLE parentTable;
    private TYPE_CLASS type;
    private int functionsLastOffset;
    private int fieldsLastOffset;
    private Map<SYMBOL, Integer> functionOffsets = new HashMap<>();
    private Map<SYMBOL, Integer> fieldsOffsets = new HashMap<>();


    CLASS_TABLE(TYPE_CLASS type, CLASS_TABLE parentTable, int initialVtableOffset, int initialFieldOffset) {
        this.type = type;
        this.parentTable = parentTable;

        if (parentTable != null) {
            fieldsLastOffset = parentTable.fieldsLastOffset;
            functionsLastOffset = parentTable.functionsLastOffset;
        } else {
            fieldsLastOffset = initialFieldOffset;
            functionsLastOffset = initialVtableOffset;
        }
        id = current_id++;
    }

    void insertFunction(SYMBOL SYMBOL) {
        assert type.equals(SYMBOL.instance);
        assert SYMBOL.isFunc();

        int offset = getFunction(SYMBOL);
        if (offset < 0) {
            functionOffsets.put(SYMBOL, functionsLastOffset);
            functionsLastOffset += OFFSET_CHANGE;
        } else {
            functionOffsets.put(SYMBOL, offset);
        }
    }

    void insertField(SYMBOL SYMBOL) {
        assert type.equals(SYMBOL.instance);
        assert !hasField(SYMBOL);
        assert SYMBOL.isField();


        fieldsOffsets.put(SYMBOL, fieldsLastOffset);
        fieldsLastOffset += Manager.BYTE_COUNT;
    }

    int getFunction(SYMBOL SYMBOL) {
        if (functionOffsets.containsKey(SYMBOL)) {
            return functionOffsets.get(SYMBOL);
        } else if (parentTable != null) {
            return parentTable.getFunction(SYMBOL);
        } else {
            return -1;
        }
    }

    int getField(SYMBOL SYMBOL) {
        if (fieldsOffsets.containsKey(SYMBOL)) {
            return fieldsOffsets.get(SYMBOL);
        } else if (parentTable != null) {
            return parentTable.getField(SYMBOL);
        } else {
            throw new IllegalArgumentException("cannot find the field: " + SYMBOL);
        }
    }

    private boolean hasField(SYMBOL SYMBOL) {
        assert type.canAssign(SYMBOL.instance);

        if (fieldsOffsets.containsKey(SYMBOL)) {
            return true;
        } else if (parentTable != null) {
            return parentTable.hasField(SYMBOL);
        } else {
            return false;
        }
    }

    int getTotalSize() {
        return fieldsLastOffset;
    }

    List<SYMBOL> getFullVtable() {
        Map<SYMBOL, Integer> vtable = new HashMap<>();
        CLASS_TABLE instance = this;
        while (instance != null) {
            for (Map.Entry<SYMBOL, Integer> entry : instance.functionOffsets.entrySet()) {
                if (!vtable.containsKey(entry.getKey()))
                    vtable.put(entry.getKey(), entry.getValue());
            }
            instance = instance.parentTable;
        }
        return vtable.entrySet()
	    .stream()
	    .sorted(Map.Entry.comparingByValue())
	    .map(Map.Entry::getKey)
	    .collect(Collectors.toList());
    }
}
