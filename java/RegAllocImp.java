import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RegAllocImp implements ObjVisitor<Exp> {

	static String[] register_tab = new String[16];
	public static HashMap<  String , StackAdd > register_Map=new HashMap<>();
	
	public RegAllocImp() {
		for (int i = 0 ; i <=15;i++) {
			register_tab[i]="";
		}
	}
	
	public static int get_free_register() {
		for (int i = 4 ; i <=12;i++) {
			if (register_tab[i].equals("")) {
				return i;
			}
		}
		return -1;
	}
	
	public static int get_var_register(String s) {
		for (int i = 4 ; i <=12;i++) {
			if (register_tab[i].equals(s)) {
				return i;
			}
		}
		return -1;
	}
	
	public static StackAdd setRegister(String id){
		StackAdd new_addr = null;
		int regVal = get_free_register();
		if (regVal==-1){
			new_addr=new StackAdd("[fp,#"+(StackAdd.count)+"]");
			register_Map.put(id, new_addr);
			return new_addr;
		}
		return new_addr;
	}

	public static StackAdd getRegister(String id){
			if(register_Map.containsKey(id)){
				StackAdd new_addr = register_Map.get(id);
				return new_addr;
			}
		return null;
	}
	
	@Override
	public Exp visit(Unit e) {
		return e;
	}

	@Override
	public Exp visit(Bool e) {
		return e;
	}

	@Override
	public Exp visit(Int e) {
		return e;
	}

	@Override
	public Exp visit(Float e) {
		return e;
	}

	@Override
	public Exp visit(Not e) {
		
		return new Not(e.e.accept(this));
	}

	@Override
	public Exp visit(Neg e) {
		
		return new Neg(e.e.accept(this));
	}

	@Override
	public Exp visit(Add e) {
		
		return new Add(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(Sub e) {
		
		return new Sub(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(FNeg e) {
		
		return new FNeg(e.e.accept(this));
	}

	@Override
	public Exp visit(FAdd e) {
		
		return new FAdd(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(FSub e) {
		
		return new FSub(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(FMul e) {
		
		return new FMul(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(FDiv e) {
		
		return new FDiv(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(Eq e) {
		
		return new Eq(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(LE e) {
		
		return new LE(e.e1.accept(this),e.e2.accept(this));
	}

	@Override
	public Exp visit(If e) {
		
		return new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
	}

	@Override
	public Exp visit(Let e) {
		int i = get_free_register();
    	if (i!=-1) { 
    		register_tab[i] = e.id.id;
    		Let l = new Let(new Id("r"+i),e.t,e.e1.accept(this),e.e2.accept(this));
    		return l;
    	}
    	else {
    		StackAdd new_addr=setRegister(e.id.id); 
    		Let l = new Let(new Id("s" + new_addr.count),e.t,e.e1.accept(this),e.e2.accept(this));
    		return l;
    	}
	}

    public Exp visit(Var e){
    	int i = get_var_register(e.id.id);
    	if (i!=-1) {
    		return new Var(new Id("r"+i));
    	}
    	else {
    		StackAdd new_addr=getRegister(e.id.id);
    		if(new_addr!=null){
    			Var v = new Var(new Id("s"+ new_addr.count));
    			new_addr.count--;
    			return v;
    		}
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
