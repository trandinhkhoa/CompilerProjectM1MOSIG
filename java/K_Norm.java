import java.util.*;



class K_Norm implements ObjVisitor<Exp> {
	
	static int x = -1;
    static String gen() {
        x++;
        return "?v" + x;
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

  /*  private Exp visit(Exp e1) {
		// TODO Auto-generated method stub
		return null;
	}*/

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
    	Let l2 = new Let(v1.id,new TInt(),e.e1.accept(this), new Eq(v1,v2));  
    	Let l1 = new Let(v2.id,new TInt(),e.e2.accept(this), l2);  
    	return l1;
    }

    public Exp visit(If e){
       Let l1 = (Let) e.e1.accept(this);
       Let l2 = (Let) l1.e2.accept(this);
       Eq eq = (Eq) l2.e2;
       If si = new If(eq,e.e2.accept(this),e.e3.accept(this));
       return si;
       
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
        E e = it.next();
        while (it.hasNext()) {
            it.next();
        }
        return new Unit(); //TODO
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

    public Exp visit(App e){
       App app = new App(e.e.accept(this),printInfix2(e.es));
       return app;
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
        /*e.e1.accept(this);
        System.out.print(".(");
        e.e2.accept(this);
        System.out.print(")");*/
    	return new Unit();
    }

    public Exp visit(Put e){
        /*System.out.print("(");
        e.e1.accept(this);
        System.out.print(".(");
        e.e2.accept(this);
        System.out.print(") <- ");
        e.e3.accept(this);
        System.out.print(")");*/
    	return new Unit();
    }
}


