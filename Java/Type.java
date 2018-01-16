abstract class Type {
    private static int x = 0;
    static Type gen() {
        return new TVar("?" + x++);
    }
    
}

class TUnit extends Type { }

class TBool extends Type { }

class TInt extends Type { }

class TFloat extends Type { }

class TFun extends Type { 
	public final Type arg;

    public final Type ret;

    public TFun(Type arg, Type ret) {
        this.arg = arg;
        this.ret = ret;
    }

    public TFun() {
        this(Type.gen(), Type.gen());
    }

    public String toString() {
        return "(" + arg + " -> " + ret + ")";
    }
}

class TTuple extends Type { }

class TArray extends Type { }

class TUnresolvedType extends Type { }

class TVar extends Type {
    String v;
    TVar(String v) {
        this.v = v;
    }
    @Override
    public String toString() {
        return v; 
    }
}

