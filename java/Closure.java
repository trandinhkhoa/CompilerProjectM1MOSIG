import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Closure implements ObjVisitor<Exp> {
	
	List<Closure_Element> closure_list;
	Stack<Id> s = new Stack<Id>();
	Hashtable<String,List<Id>> ht = new Hashtable<String,List<Id>>(); 
	boolean main_done = false;
	public Closure() {
		closure_list = new LinkedList<Closure_Element>();
	}
    
	public void if_main(Exp e) {
		if ((s.isEmpty())&&(!main_done)) {
    		closure_list.add(new Closure_Element(e));
    		main_done = true;
    	}
	}
	
    public Exp visit(Unit e) {
    	if_main(e);
        return e;
    }

    public Exp visit(Bool e) {
    	if_main(e);
        return e;
    }

    public Exp visit(Int e) {
    	if_main(e);
    	return e;
    }

    public Exp visit(Float e) {
    	if_main(e);
        return e;
    }

    public Exp visit(Not e) {
    	if_main(e);
       return new Not(e.e.accept(this));
    }

    public Exp visit(Neg e) {
    	if_main(e);
    	return new Neg(e.e.accept(this));
    }

    public Exp visit(Add e) {
    	if_main(e);
    	return new Add(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(Sub e) {
		if_main(e);
    	return new Sub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FNeg e){
    	if_main(e);
    	return new FNeg(e.e.accept(this));
    }

    public Exp visit(FAdd e) {
    	if_main(e);
    	return new FAdd(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(FSub e) {
		if_main(e);
    	return new FSub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FMul e) {
    	if_main(e);
    	return new FMul(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FDiv e){
    	if_main(e);
    	return new FDiv(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Eq e){
    	if_main(e);
    	return new Eq(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(LE e){
    	if_main(e);
    	return new LE(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(If e){
    	if_main(e);
       return new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
       
    }

    public Exp visit(Let e) {
    	if_main(e);
    	return new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Var e){
    	if_main(e);
    	if (s.isEmpty()) {
    		ht.put("main",new LinkedList<Id>() );
    		s.push(new Id("main"));
    	}
    	Id i = s.peek();
    	ht.get(i.id).add(e.id);
    	return e;
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
    	s.push(e.fd.id);
    	ht.remove(e.fd.id.id);
    	ht.put(e.fd.id.id, new LinkedList<Id>());
    	ht.get(e.fd.id.id).add(e.fd.id);
    	FunDef fd2= new FunDef(new Id("_"+e.fd.id.id), e.fd.type, e.fd.args, e.fd.e.accept(this));
    	s.pop();
    	LetRec lr = new LetRec(fd2, e.e.accept(this));
    	closure_list.add(new Closure_Element(lr));
    	return lr;
    }
    
    public Exp visit(App e){
    	if_main(e);
    	Var name = (Var) e.e;
    	Var v = new Var(new Id("_"+name.id.id));
    	App a =  new App( v  ,printInfix2(e.es));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(a);
    	return new App(new Var(new Id("apply_direct")), l);
       
    }

    public Exp visit(Tuple e){
    	if_main(e);
       Tuple t = new Tuple(printInfix2(e.es));
    	return t;
    }

    public Exp visit(LetTuple e){
    	if_main(e);
       LetTuple lt = new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this));
    	return lt;
    }

    public Exp visit(Array e){
    	if_main(e);
    	Array a = new Array(e.e1.accept(this),e.e2.accept(this));
    	return a;
    }

    public Exp visit(Get e){
    	if_main(e);
        Get g = new Get(e.e1.accept(this),e.e2.accept(this));
    	return g;
    }

    public Exp visit(Put e){
    	if_main(e);
       Put p = new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
    	return p;
    }
}



