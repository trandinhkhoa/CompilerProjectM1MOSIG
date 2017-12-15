import java.util.*;



class K_Norm implements ObjVisitor<Exp> {
	
	static int x = -1;
    static String gen() {
        x++;
        return "temp" + x;
    }
    
    public Exp visit(Unit e) {
        return e;
    }

    public Exp visit(Bool e) {
        return e;
    }

    public Exp visit(Int e) {
    	Var v1 = new Var(new Id(gen()));
    	Let l = new Let(v1.id,new TInt(),e, v1);   
    	return l;
    }

    public Exp visit(Float e) {
        return e;
    }

    public Exp visit(Not e) {
       return e.e.accept(this);
    }

    public Exp visit(Neg e) {
    	Var v1 = new Var(new Id(gen()));
    	Let l = new Let(v1.id,new TInt(),e.e.accept(this), new Neg(v1));   
    	return l;
    }

    public Exp visit(Add e) {
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TInt(),e.e1.accept(this), new Add(v1,v2));  
    	Let l1 = new Let(v2.id,new TInt(),e.e2.accept(this), l2);  
    	return l1;
    }

	public Exp visit(Sub e) {
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TInt(),e.e1.accept(this), new Sub(v1,v2));  
    	Let l1 = new Let(v2.id,new TInt(),e.e2.accept(this), l2);  
    	return l1;
    }

    public Exp visit(FNeg e){
    	Var v1 = new Var(new Id(gen()));
    	Let l = new Let(v1.id,new TFloat(),e.e.accept(this), new FNeg(v1));   
    	return l;
    }

    public Exp visit(FAdd e){
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TFloat(),e.e1.accept(this), new FAdd(v1,v2));  
    	Let l1 = new Let(v2.id,new TFloat(),e.e2, l2);  
    	return l1;
    }

    public Exp visit(FSub e){
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TFloat(),e.e1.accept(this), new FSub(v1,v2));  
    	Let l1 = new Let(v2.id,new TFloat(),e.e2.accept(this), l2);  
    	return l1;
    }

    public Exp visit(FMul e) {
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TFloat(),e.e1.accept(this), new FMul(v1,v2));  
    	Let l1 = new Let(v2.id,new TFloat(),e.e2.accept(this), l2);  
    	return l1;
    }

    public Exp visit(FDiv e){
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TFloat(),e.e1.accept(this), new FDiv(v1,v2));  
    	Let l1 = new Let(v2.id,new TFloat(),e.e2.accept(this), l2);  
    	return l1;
    }

    public Exp visit(Eq e){
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TInt(),e.e1.accept(this), new Eq(v1,v2));  
    	Let l1 = new Let(v2.id,new TInt(),e.e2.accept(this), l2);  
    	return l1;
    }

    public Exp visit(LE e){
    	Var v1 = new Var(new Id(gen()));
    	Var v2 = new Var(new Id(gen()));
    	Let l2 = new Let(v1.id,new TInt(),e.e1.accept(this), new LE(v1,v2));  
    	Let l1 = new Let(v2.id,new TInt(),e.e2.accept(this), l2);  
    	return l1;
    }

    public Exp visit(If e){
       Let l1 = (Let) e.e1.accept(this);
       Let l2 = (Let) l1.e2;
       Exp eq = l2.e2;
       If si = new If(eq,e.e2.accept(this),e.e3.accept(this));
       Let lt2 = new Let(l2.id,l2.t,l2.e1,si);
       Let lt1 = new Let(l1.id,l1.t,l1.e1,lt2);
       return lt1;
       
    }

    public Exp visit(Let e) {
    	Let l = new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this));
    	return l;
    }

    public Exp visit(Var e){
        return e;
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
    
    public Exp visit(App e){
       App app = new App(e.e.accept(this),printInfix2(e.es));
       return app;
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


