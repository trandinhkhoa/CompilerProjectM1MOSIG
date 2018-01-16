import java.util.*;

/**
 * K-Normalization class is the first bridge between Min-Caml and Assembly
 * language. for each nested expression, a new variable generated to be ready
 * eventually for the register allocation phase Followed the pseudo-code
 * mentioned in the project paper.
 */

class K_Norm implements ObjVisitor<Exp> {

	/* Prepare variables generator function */
	static int x = -1;

	static String gen() {
		x++;
		return "temp" + x;
	}
	/* **************************************/

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
		Var v1 = new Var(new Id(gen()));
		Sub s = new Sub(new Int(0),e.e);
		Let l = new Let(v1.id, new TInt(), s.accept(this), v1);
		return l;
	}

	public Exp visit(Add e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Add(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TInt(),l3,v3);
		return l3;
	}

	public Exp visit(Sub e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Sub(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TInt(),l3,v3);
		return l3;
	}

	public Exp visit(FNeg e) {
		Var v1 = new Var(new Id(gen()));
		Let l = new Let(v1.id, new TFloat(), e.e.accept(this), new FNeg(v1));
		return l;
	}

	public Exp visit(FAdd e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FAdd(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TFloat(),l3,v3);
		return l3;
	}

	public Exp visit(FSub e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FSub(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TFloat(),l3,v3);
		return l3;
	}

	public Exp visit(FMul e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FMul(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TFloat(),l3,v3);
		return l3;
	}

	public Exp visit(FDiv e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FDiv(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TFloat(),l3,v3);
		return l3;
	}

	public Exp visit(Eq e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Eq(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TBool(),l3,v3);
		return l3;
	}

	public Exp visit(LE e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		// Var v3 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new LE(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		// Let l1 = new Let(v3.id,new TBool(),l3,v3);
		return l3;
	}

	public Exp rec_If(Exp e, If i) {
		if (e.getClass() == Let.class) {
			return new Let(((Let) e).id, ((Let) e).t, ((Let) e).e1, rec_If(((Let) e).e2, i));
		} else {
			return new If(e, i.e2.accept(this), i.e3.accept(this));
		}
	}

	public Exp visit(If e) {
		if (e.e1.getClass() == Not.class) {
			If new_e = new If(((Not) e.e1).e, e.e3, e.e2);
			return new_e.accept(this);
		} else if ((e.e1.getClass() != Eq.class) && (e.e1.getClass() != LE.class)) {
			If new_e = new If(new Eq(e.e1, new Bool(false)), e.e3, e.e2);
			return new_e.accept(this);
		} else {
			Exp l1 = e.e1.accept(this);
			/*
			 * Var v1 = new Var(new Id(gen())); If si = new
			 * If(v1,e.e2.accept(this),e.e3.accept(this)); Let lt2 = new Let(v1.id,new
			 * TBool(),l1,si);
			 */
			return rec_If(l1, e);
		}

	}

	public Exp visit(Let e) {
		Let l = new Let(e.id, e.t, e.e1.accept(this), e.e2.accept(this));
		return l;
	}

	public Exp visit(Var e) {
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

	public Exp visit(LetRec e) {

		FunDef fd2 = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
		LetRec lr = new LetRec(fd2, e.e.accept(this));
		return lr;
	}

	Exp app_rec(List<Exp> llet, List<Exp> la, Exp e) {
		if (llet.isEmpty()) {
			return new App(e, la);
		} else {
			Id i = new Id(gen());
			la.add(new Var(i));
			Exp elt = llet.remove(0);
			Let l = new Let(i, new TUnresolvedType(), elt, app_rec(llet, la, e));
			return l;
		}
	}

	public Exp visit(App e) {
		List<Exp> la = new LinkedList<Exp>();
		Exp a = app_rec(printInfix2(e.es), la, e.e);
		return a;
	}

	public Exp visit(Tuple e){
		/*Tuple t = new Tuple(printInfix2(e.es));
		return t;*/

		try {
			throw new NotYetImplementedException("Tuple NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			// TODO Auto-generated catch block
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	public Exp visit(LetTuple e){
		/*LetTuple lt = new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
		return lt;*/

		try {
			throw new NotYetImplementedException("Let Tuple NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			// TODO Auto-generated catch block
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	public Exp visit(Array e){		
	/*	Var size = new Var(new Id(gen()));
		Var init = new Var(new Id(gen()));

		Let l1 = new Let(size.id, new TArray(), e.e1.accept(this), new Array(size,init));
		Let l2 = new Let(init.id, new TArray(), e.e2.accept(this), l1);
		return l2;*/
		try {
			throw new NotYetImplementedException("Array NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			// TODO Auto-generated catch block
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	public Exp visit(Get e) {
		/*e.e1.accept(this)
		string s1 = myStack.pop();
		e.e2.accept(this)
		string s2 = myStack.pop();
		load r1, [s1, s2]
		myStack.push("r1");	*/
		
		
		/*Get g = new Get(e.e1.accept(this), e.e2.accept(this));
		return g;*/
		try {
			throw new NotYetImplementedException("Get NotYetImplemented");
		} catch (NotYetImplementedException e1) { 
			// TODO Auto-generated catch block
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	public Exp visit(Put e) {
		/*Put p = new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
		return p;*/

		/*Exp e1 = e.e2.accept(this);
		Exp e2 = e.e3.accept(this);
		Id i1 = new Id(gen());
		Id i2 = new Id(gen());
		Var v1 = new Var(i1);
		Var v2 = new Var(i2);
		Let l1 = new Let(i1, new TUnit(), e1, new Put(e.e1,v1,v2));
		Let l2 = new Let(i2, new TUnit(), e2, l1);*/
		
		try {
			throw new NotYetImplementedException("Put NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			// TODO Auto-generated catch block
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}
}
