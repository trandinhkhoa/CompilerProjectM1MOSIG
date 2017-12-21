import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class ASML_Gen implements ObjVisitor<Exp> {
	
	List<Id> fun_List = new LinkedList<Id>();
	
	public ASML_Gen(List<Id> fun_List) {
		this.fun_List = fun_List;
		for(int i = 0; i<this.fun_List.size();i++) {
			System.out.println(this.fun_List.get(i));
		}
	}
	
	public boolean contains(Id i) {
		Iterator<Id> it = fun_List.iterator();
		while(it.hasNext()) {
			if(it.next().id.equals(i.id)) {
				return true;
			}
		}return false;
	}
	
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

	
    public Exp visit(Unit e){return e;}
    public Exp visit(Bool e){return e;}
    public Exp visit(Int e){return e;}
    public Exp visit(Float e){return e;}
    public Exp visit(Var e){return e;}
    
    public Exp visit(Not e){return new Not(e.e.accept(this));}
    public Exp visit(Neg e){
    	Var v = new Var(new Id("neg"));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(e.e.accept(this));
    	return new App(v,l);}
    public Exp visit(Add e){
    	Var v = new Var(new Id("add"));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(e.e1.accept(this));
    	l.add(e.e2.accept(this));
    	return new App(v,l);}
    public Exp visit(Sub e){
    	Var v = new Var(new Id("sub"));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(e.e1.accept(this));
    	l.add(e.e2.accept(this));
    	return new App(v,l);}
    public Exp visit(FNeg e){
    	Var v = new Var(new Id("fneg"));
		List<Exp> l = new LinkedList<Exp>();
		l.add(e.e.accept(this));
		return new App(v,l);}
    public Exp visit(FAdd e){
    	Var v = new Var(new Id("fadd"));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(e.e1.accept(this));
    	l.add(e.e2.accept(this));
    	return new App(v,l);}
    public Exp visit(FSub e){
    	Var v = new Var(new Id("fsub"));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(e.e1.accept(this));
    	l.add(e.e2.accept(this));
    	return new App(v,l);}
    public Exp visit(FMul e){
    	Var v = new Var(new Id("fmul"));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(e.e1.accept(this));
    	l.add(e.e2.accept(this));
    	return new App(v,l);}
    public Exp visit(FDiv e){
    	Var v = new Var(new Id("fdiv"));
    	List<Exp> l = new LinkedList<Exp>();
    	l.add(e.e1.accept(this));
    	l.add(e.e2.accept(this));
    	return new App(v,l);}
    public Exp visit(Eq e){return new Eq(e.e1.accept(this),e.e2.accept(this));}
    public Exp visit(LE e){return new LE(e.e1.accept(this),e.e2.accept(this));}
    public Exp visit(If e){return new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));}
    public Exp visit(Array e){return new Array(e.e1.accept(this),e.e2.accept(this));}
    public Exp visit(Tuple e){return new Tuple(printInfix2(e.es));}
   
    public Exp visit(Let e){return new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this));}
    public Exp visit(LetTuple e){return new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this));}
    public Exp visit(LetRec e){
    	FunDef fd = new FunDef(e.fd.id,e.fd.type,e.fd.args,e.fd.e.accept(this));
    	return new LetRec(fd,e.e.accept(this));}
    public Exp visit(App e){
    	Var v = (Var) e.e;
    	if (v.id.id.equals("apply_direct")) {	
    		v = new Var(new Id("call"));
    		Tuple t = (Tuple)e.es.get(0);
    		Var a = (Var) t.es.remove(0);
    		App app = new App(a,t.es);
    		List<Exp> le = new LinkedList<Exp>();
    		le.add(app.accept(this));
    		le.addAll(e.es.subList(1, e.es.size()));
    		return new App(v,le);
    		
    	}else if(!contains(v.id)) {
    		v = new Var(new Id("_min_caml"+v.id.id));
    	}
    	return new App(v,printInfix2(e.es));
    }
    public Exp visit(Get e){return e;}
    public Exp visit(Put e){return e;}
}

