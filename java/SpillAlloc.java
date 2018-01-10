import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SpillAlloc implements ObjVisitor<Exp> {
	
	public static HashMap<  String , Integer > register_Map = new HashMap<>();
	 int index;
	
	public SpillAlloc() {
		register_Map.clear();
		index = 0;
	}
	
	public int get_free_register() {
		return index++;
	}
	
	public int get_var_register(String s) {
		Integer i = register_Map.get(s);
		if (i != null) {
			return i;
		}else {
			return -1;
		}
	}
	
    
    public Exp visit(Unit e) {
        return e;
    }

    public Exp visit(Bool e) {
        return e;
    }

    public Exp visit(Int e) {
    	return e;
    }

    public Exp visit(Float e) {
        return e;
    }

    public Exp visit(Not e) {
       return new Not(e.e.accept(this));
    }

    public Exp visit(Neg e) {
    	return new Neg(e.e.accept(this));
    }

    public Exp visit(Add e) {
    	return new Add(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(Sub e) {
    	return new Sub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FNeg e){
    	return new FNeg(e.e.accept(this));
    }

    public Exp visit(FAdd e) {
    	return new FAdd(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(FSub e) {
    	return new FSub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FMul e) {
    	return new FMul(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FDiv e){
    	return new FDiv(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Eq e){
    	return new Eq(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(LE e){
    	return new LE(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(If e){
       return new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
       
    }

    public Exp visit(Let e) {
    	int i = get_free_register();
    	if (i!=-1) { 
    		register_Map.put(e.id.id, i);
    		Let l = new Let(new Id("s"+i),e.t,e.e1.accept(this),e.e2.accept(this));
    		return l;
    	}
    	else {
    		System.err.println("No remaining register");
    		Let l = new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this));
    		return l;
    	}
    }

    public Exp visit(Var e){
    	int i = get_var_register(e.id.id);
    	if (i!=-1) {
    		return new Var(new Id("s"+i));
    	}else {
        		return e;
    	}
    }
    

    // print sequence of identifiers 
    static <E> Exp printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return new Unit();
        }
        Iterator<E> it = l.iterator();
        it.next();
        while (it.hasNext()) {
            it.next();
        }
        return new Unit();
    }

   
	// print sequence of Exp
    List<Exp> printInfix2(List<Exp> l) {
    	List<Exp> new_list = new LinkedList<Exp>();
    	new_list.clear();
        if (l.isEmpty()) {
            new_list.add(new Unit());
        }
        Iterator<Exp> it = l.iterator();
        while (it.hasNext()) {
        	  new_list.add(it.next().accept(this));
        }
        return new_list;
    }

    public Exp visit(LetRec e){
    	
    	FunDef fd2= new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
    	LetRec lr = new LetRec(fd2, e.e.accept(this));
    	return lr;
    }

    public Exp rec_app(Exp e, Exp app) {
    	Let l = (Let)e;
    	if (l.id.id.equals("\0")) {
    		System.out.println(l.id.id);
    		return app;
    	}else {
    		return (Exp) new Let(l.id,l.t,l.e1,rec_app(l.e2,app));
    	}
    }
    
    Exp app_rec(List<Exp> la,List<Exp> llet,List<Exp> lvar,Exp e) {
    	if (la.isEmpty()) {
    		return new App(e,lvar);
    	}else {
    		Exp first = la.remove(0);
    		if(first.getClass()!=Let.class){
    			lvar.add(first);
    			return app_rec(la,llet,lvar ,e);
    		}else {
    			Let l = (Let) first;
    			Var x = new Var(l.id);
    			llet.add(l);
    			lvar.add(x);
    			return new Let(l.id,l.t,l.e1,
    					app_rec(la,llet,lvar,e)
    					);
    		}
    	}
    }
    
    public Exp visit(App e){
    	return new App(e.e,printInfix2(e.es));
       
    }

    public Exp visit(Tuple e){
       Tuple t = new Tuple(printInfix2(e.es));
    	return t;
    }

    public Exp visit(LetTuple e){
       LetTuple lt = new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this));
    	return lt;
    }

    public Exp visit(Array e){
    	Array a = new Array(e.e1.accept(this),e.e2.accept(this));
    	return a;
    }

    public Exp visit(Get e){
        Get g = new Get(e.e1.accept(this),e.e2.accept(this));
    	return g;
    }

    public Exp visit(Put e){
       Put p = new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
    	return p;
    }
}



