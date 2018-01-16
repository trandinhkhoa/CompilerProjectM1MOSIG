import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



class Copy implements ObjVisitor<Exp> {
	
	static int x = -1;
    static String gen() {
        x++;
        return "temp" + x;
    }
    
    public Exp visit(Unit e) {
        return new Unit();
    }

    public Exp visit(Bool e) {
        return new Bool(e.b);
    }

    public Exp visit(Int e) {
    	return new Int(e.i);
    }

    public Exp visit(Float e) {
        return new Float(e.f);
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

    public Exp visit(FAdd e){
    	return new FAdd(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FSub e){
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
    	return new Let(new Id(e.id.id),e.t,e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Var e){
        return new Var(new Id(e.id.id));
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
    
    // print sequence of identifiers 
    List<Id> printId(List<Id> l) {
    	List<Id> new_list = new LinkedList<Id>();
        if (l.isEmpty()) {
            return new_list;
        }
        Iterator<Id> it = l.iterator();
        while (it.hasNext()) {
           new_list.add(new Id( it.next().id ));
        }
        return new_list;
    }

   
	// print sequence of Exp
    List<Exp> printInfix2(List<Exp> l) {
    	List<Exp> new_list = new LinkedList<Exp>();
    	new_list.clear();
        if (!l.isEmpty()) {
        Iterator<Exp> it = l.iterator();
        while (it.hasNext()) {
        	  new_list.add(it.next().accept(this));
        }
        }
        return new_list;
    }

    public Exp visit(LetRec e){
    	FunDef fd = new FunDef(new Id(e.fd.id.id),e.fd.type,printId(e.fd.args),e.fd.e.accept(this));
    	return new LetRec(fd,e.e.accept(this));
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
       return new App(e.e.accept(this),printInfix2(e.es));
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
       return new Tuple(printInfix2(e.es));
    }

    public Exp visit(LetTuple e){
       return new LetTuple(printId(e.ids),e.ts,e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Array e){
    	return new Array(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Get e){
        return new Get(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Put e){
    	return new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
    }
}


