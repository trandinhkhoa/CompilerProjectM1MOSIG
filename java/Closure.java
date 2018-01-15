import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Closure Conversion class eliminates the nested functions and letRec.
 * It generates a flatten code using blocks of Closure Elements.  
 * 
 * 
 * */

// khoaNOTE: Function list size is 4
// Closure list size is 5
// Function list is [succ double succ double]

public class Closure implements ObjVisitor<Exp> {
	
	List<Closure_Element> closure_list;
	Stack<Id> s = new Stack<Id>();
	List<Id> fun_List = new LinkedList<Id>();
	Hashtable<String,List<Id>> ht = new Hashtable<String,List<Id>>(); 
	boolean main_done = false;
	boolean continueFlag = true;
    //library function (predefined) list
    List<String> libraryFuncList = new ArrayList<String>();

	public Closure() {
		closure_list = new LinkedList<Closure_Element>();
        closure_list.add(new Closure_Element()); 
        //initialize pre-defined function list
        libraryFuncList.add("print_int");
        libraryFuncList.add("print_char");
        libraryFuncList.add("print_newline");
	}
	
    public Exp visit(Unit e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(e));
    	// }
        return e;
    }

    public Exp visit(Bool e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(e));
    	// }
        return e;
    }

    public Exp visit(Int e) {
        // System.out.println("Current exp is Int " + e.i);
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(e));
    	// }
    	return e;
    }

    public Exp visit(Float e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(e));
    	// }
        return e;
    }

    public Exp visit(Not e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Not(e.e.accept(this))));
    	// }
       return new Not(e.e.accept(this));
    }

    public Exp visit(Neg e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Neg(e.e.accept(this))));
    	// }
    	return new Neg(e.e.accept(this));
    }

    public Exp visit(Add e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Add(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new Add(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(Sub e) {
		// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Sub(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new Sub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FNeg e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new FNeg(e.e.accept(this))));
    	// }
    	return new FNeg(e.e.accept(this));
    }

    public Exp visit(FAdd e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new FAdd(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new FAdd(e.e1.accept(this),e.e2.accept(this));
    }

	public Exp visit(FSub e) {
		// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new FSub(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new FSub(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FMul e) {
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new FMul(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new FMul(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(FDiv e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new FDiv(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new FDiv(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Eq e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Eq(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new Eq(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(LE e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new LE(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new LE(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(If e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this))));
    	// }
       return new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
       
    }

    public Exp visit(Let e) {
        // System.out.println("Current exp is LET " + e.id);
        Exp newE1 = e.e1;
        Exp newE2 = e.e2;
    	if ((s.isEmpty())&&(!main_done)) {
            // System.out.println("FUCK YOU  :)");
			main_done = true;
            newE1 = e.e1.accept(this);
            newE2 = e.e2.accept(this);
    		// closure_list.add(new Closure_Element(new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this))));
    		// closure_list.add(new Closure_Element(new Let(e.id,e.t,newE1,newE2)));
    		closure_list.set(0, new Closure_Element(new Let(e.id,e.t,newE1,newE2)));
            // System.out.println("[LET] ADDED TO CLOSURE LIST " + e.id);
            continueFlag = false;
            main_done = false;
    	}
    	if (!s.isEmpty()){
            Id i = s.peek();
            List<Id> li= ht.get(i.id);
            li.add(e.id);
            ht.put(i.id, li);
    	}

        //khoaNOTE: when reach temp0, s is not Empty so e.e1.accept (and e2) didnt run
        // but if accept(this) run here, it will repeat once the part inside if (s.isEmpty)) finished
        // If first LET is after Let Rec => stack no longer empty  => skip if => main_done still = false => main is added first => maybe change visit order => but that would affect the scope? 
        //
        // System.out.println("[LET]FUCK THIS SHIT MAINDONE is " + main_done);
      // System.out.println("Closure List Size is " + this.closure_list.size());
		// 				for (int i = this.closure_list.size()-1 ; i >=0 ; i--){
		// 				  this.closure_list.get(i).print();
		// 				  System.out.println();
		// 				} System.out.println();

        //khoaNOTE: continueFlag = true => when let does not lead to the end of program (happen when let is e1 of letrec)
        if ((main_done)||(continueFlag)){
            newE1 = e.e1.accept(this);
            newE2 = e.e2.accept(this);
        }
    	// return new Let(e.id,e.t,e.e1.accept(this),e.e2.accept(this));
    	// return new Let(e.id,e.t,e.e1,e.e2);
    	return new Let(e.id,e.t,newE1,newE2);
    }

    public Exp visit(Var e){
        // System.out.println("Current exp is Var " + e.id);
    	// if (s.isEmpty()) {
    	// 	ht.put("_",new LinkedList<Id>() );
    	// 	s.push(new Id("_"));
    	// }
    	// Id i = s.peek();
    	// List<Id> li= ht.get(i.id);
    	// ht.remove(i.id);
    	// li.add(e.id);
    	// ht.put(i.id, li);
    	//
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(e));
    	// }
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
        //LetRec go from left to right
        // System.out.println("Current Exp is" + e);
        // System.out.println("\tFunDef is " + e.fd.id + " \tArg List is " + e.fd.args + " and e is " + e.e);
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
    	fun_List.add(id);
    	FunDef fd2= new FunDef(id, e.fd.type, e.fd.args, e.fd.e.accept(this));
    	Id i = s.pop();
    	LetRec lr = new LetRec(fd2, e.e.accept(this));
    	Closure_Element ce = new Closure_Element(lr);
    	ce.free_variables = fv;
    	closure_list.add(ce);
        // System.out.println("[LETREC] ADDED TO CLOSURE LIST " + e.fd.id);

    	return lr.e;

    	// LetRec lr = new LetRec(e.fd, e.e.accept(this));
    	// return lr;
    }
    
    public Exp visit(App e){
        // System.out.println("Current expression is " + e.toString() + "\t App is " + ((Var)e.e).id + "\tArgument list is " + e.es.get(0));
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
        if (libraryFuncList.contains(((Var)e.e).id.id)){
            // System.out.println("\n Predefined call this is " + ((Var)e.e).id + "\n");
            return e;
        } else {
            // System.out.println("\nNot Predefined call\n");
            App a = new App(new Var(new Id("apply_direct")), l);
            if (here) {
                closure_list.add(new Closure_Element(a));
                // System.out.println("[APP] ADDED TO CLOSURE LIST");
                // System.out.println("[APP] MAINDONE is " + main_done);
            }
            return a;
        }
    }

    public Exp visit(Tuple e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Tuple(printInfix2(e.es))));
    	// }
    	
    	return new Tuple(printInfix2(e.es));
    }

    public Exp visit(LetTuple e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this))));
    	// }if (!s.isEmpty()){
    	// Id i = s.peek();
	    // 	List<Id> li= ht.get(i.id);
	    // 	li.addAll(e.ids);
	    // 	ht.put(i.id, li);
    	// }
    	return new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Array e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Array(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return new Array(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Get e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element( new Get(e.e1.accept(this),e.e2.accept(this))));
    	// }
    	return  new Get(e.e1.accept(this),e.e2.accept(this));
    }

    public Exp visit(Put e){
    	// if ((s.isEmpty())&&(!main_done)) {
		// 	main_done = true;
    	// 	closure_list.add(new Closure_Element(new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this))));
    	// }
    	return new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
    }
}



