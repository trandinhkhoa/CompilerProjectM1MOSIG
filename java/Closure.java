import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Closure implements ObjVisitor<Exp> {
	
	List<Closure_Element> closure_list;
	Stack<Id> s = new Stack<Id>();
	List<Id> fun_List = new LinkedList<Id>();
	Hashtable<String,List<Id>> ht = new Hashtable<String,List<Id>>(); 
	boolean main_done = false;
	public Closure() {
		closure_list = new LinkedList<Closure_Element>();
	}
	
    public Exp visit(Unit e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(e));
    	}
        return e;
    }

    public Exp visit(Bool e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(e));
    	}
        return e;
    }

    public Exp visit(Int e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(e));
    	}
    	return e;
    }

    public Exp visit(Float e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(e));
    	}
        return e;
    }

    public Exp visit(Not e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Not(e.e.accept(this))));
    	}
       return new Not(e.e.accept(this));
    }

    public Exp visit(Neg e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Neg(e.e.accept(this))));
    	}
    	return new Neg(e.e.accept(this));
    }

    public Exp visit(Add e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Add(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new Add(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(Sub e) {
		if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Sub(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new Sub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FNeg e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new FNeg(e.e.accept(this))));
    	}
    	return new FNeg(e.e.accept(this));
    }

    public Exp visit(FAdd e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new FAdd(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new FAdd(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(FSub e) {
		if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new FSub(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new FSub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FMul e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new FMul(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new FMul(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FDiv e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new FDiv(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new FDiv(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Eq e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Eq(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new Eq(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(LE e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new LE(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new LE(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(If e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this))));
    	}
       return new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
       
    }

    public Exp visit(Let e) {
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this))));
    	}
    	if (!s.isEmpty()){
    	Id i = s.peek();
	    	List<Id> li= ht.get(i.id);
	    	li.add(e.id);
	    	ht.put(i.id, li);
    	}
    	return new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Var e){
    	if (s.isEmpty()) {
    		ht.put("_",new LinkedList<Id>() );
    		s.push(new Id("_"));
    	}
    	Id i = s.peek();
    	List<Id> li= ht.get(i.id);
    	ht.remove(i.id);
    	li.add(e.id);
    	ht.put(i.id, li);
    	
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(e));
    	}
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
    	List<Id> fv = new LinkedList<Id>();
    	if (!s.isEmpty()) {
	    	Id i = s.peek();
	    	fv = ht.get(i.id);
    	}
    	
    	s.push(e.fd.id);
    	ht.remove(e.fd.id.id);
    	ht.put(e.fd.id.id, new LinkedList<Id>());
    	ht.get(e.fd.id.id).addAll(e.fd.args);
    	Id id = new Id("_"+e.fd.id.id);
    	FunDef fd2= new FunDef(id, e.fd.type, e.fd.args, e.fd.e.accept(this));
    	fun_List.add(e.fd.id);
    	Id i = s.pop();
    	LetRec lr = new LetRec(fd2, e.e.accept(this));
    	Closure_Element ce = new Closure_Element(lr);
    	ce.free_variables = fv;
    	closure_list.add(ce);
    	
    	return lr;
    }
    
    public Exp visit(App e){
    	boolean here = false;
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
			here = true;
    	}
    	Var name = (Var) e.e;
    	Var v = new Var(new Id("_"+name.id.id));
    	//App t = new App(v,printInfix2(e.es));
    	List<Exp> lis = new LinkedList<Exp>();
    	lis.add(v);
    	lis.addAll(printInfix2(e.es));
    	Tuple t = new Tuple(lis);
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(t);
    	App a = new App(new Var(new Id("apply_direct")), l);
    	if (here) {
		closure_list.add(new Closure_Element(a));
    	}
    	return a;
       
    }

    public Exp visit(Tuple e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Tuple(printInfix2(e.es))));
    	}
    	
    	return new Tuple(printInfix2(e.es));
    }

    public Exp visit(LetTuple e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this))));
    	}if (!s.isEmpty()){
    	Id i = s.peek();
	    	List<Id> li= ht.get(i.id);
	    	li.addAll(e.ids);
	    	ht.put(i.id, li);
    	}
    	return new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Array e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Array(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return new Array(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Get e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element( new Get(e.e1.accept(this),e.e2.accept(this))));
    	}
    	return  new Get(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Put e){
    	if ((s.isEmpty())&&(!main_done)) {
			main_done = true;
    		closure_list.add(new Closure_Element(new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this))));
    	}
    	return new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
    }
}



