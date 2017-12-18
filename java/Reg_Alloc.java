import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Reg_Alloc implements ObjVisitor<Exp> {
	
	static String[] register_tab = new String[16];
	 
	
	public Reg_Alloc() {
		for (int i = 0 ; i <=15;i++) {
			register_tab[i]="";
		}
	}
	
	public int get_free_register() {
		for (int i = 4 ; i <=12;i++) {
			//System.out.println(register_tab[i]);
			if (register_tab[i].equals("")) {
				return i;
			}
		}
		return -1;
	}
	
	public int get_var_register(String s) {
		for (int i = 4 ; i <=12;i++) {
			//System.out.println( register_tab[i] +" = " + s +" ?");
			if (register_tab[i].equals(s)) {
				return i;
			}
		}
		return -1;
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
    		register_tab[i] = e.id.id;
    		Let l = new Let(new Id("r"+i),e.t,e.e1.accept(this),e.e2.accept(this));
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
    		return new Var(new Id("r"+i));
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
    	
    	FunDef fd2= new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e);
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
      // App app = new App(e.e.accept(this),printInfix2(e.es));
      // return app;
    	/*List<Exp> l = printInfix2(e.es);
    	Iterator it = l.iterator();
    	List<Exp> l2 = new LinkedList<Exp>();
    	Let new_l = new Let(new Id("\0"),new TInt(),new Unit(),new Unit());
    	while (it.hasNext()) {
    		Exp exp = (Exp) it.next();
    		if(exp.getClass()==Let.class){

        		System.out.println("YO");
    			Let lt = (Let) exp;
    			l2.add(new Var(lt.id));
    			new_l = new Let(lt.id,lt.t,lt.e1,new_l);
    		}else {
    			l2.add(exp);
    		}
    	}
    	Exp app;
    	app = new App(e.e.accept(this),l2);
    	if (!new_l.id.id.equals("\0")) {
    		new_l = (Let) rec_app(new_l,app);
    		app = new_l;
    	}
       return app;*/
       
      /* List<Exp> la = new LinkedList<Exp>();
       la.addAll(printInfix2(e.es));
       List<Exp> llet = new LinkedList<Exp>();
       List<Exp> lvar = new LinkedList<Exp>();
       Exp a =  app_rec(la,llet,lvar,e.e.accept(this));
       int i = get_free_register();
       Id id;
       if(i!=-1) {
    	   id = new Id("r"+i);
       }else {
    	   System.err.println("No remaining register");
    	   id = new Id("temp");
       }
       return new Let (id,new TVar(id.id),a,new Var(id));*/
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



