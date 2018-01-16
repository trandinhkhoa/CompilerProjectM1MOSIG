import java.util.*;

/**
 * K-Normalization class is the first bridge between Min-Caml and Assembly
 * language. for each nested expression, a new variable generated to be ready
 * eventually for the register allocation phase Followed the pseudo-code
 * mentioned in the project paper.
 */
public class K_Norm implements ObjVisitor<Exp> {
 
	/* Prepare variables generator function */
	static int x = -1;

	static String gen() {
		x++;
		return "temp" + x;
	}
	/* **************************************/

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Unit expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Unit e) {
		return e;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Bool expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Bool e) {
		return e;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Int expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Int e) {
		return e;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Float expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Float e) {
		return e;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Not expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Not e) {
		return new Not(e.e.accept(this));
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Neg expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Neg e) {
		Var v1 = new Var(new Id(gen()));
		Sub s = new Sub(new Int(0),e.e);
		Let l = new Let(v1.id, new TInt(), s.accept(this), v1);
		return l;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Add expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Add e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Add(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		return l3;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Sub expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Sub e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Sub(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		return l3;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input FNeg expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(FNeg e) {
		Var v1 = new Var(new Id(gen()));
		Let l = new Let(v1.id, new TFloat(), e.e.accept(this), new FNeg(v1));
		return l;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input FAdd expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(FAdd e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FAdd(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		return l3;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input FSub expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(FSub e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FSub(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		return l3;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input FMul expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(FMul e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FMul(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		return l3;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input FDiv expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(FDiv e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FDiv(v1, v2));
		Let l3 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
		return l3;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Eq expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Eq e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Eq(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		return l3;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input LE expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(LE e) {
		Var v1 = new Var(new Id(gen()));
		Var v2 = new Var(new Id(gen()));
		Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new LE(v1, v2));
		Let l3 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
		return l3;
	}

	Exp rec_If(Exp e, If i) {
		if (e.getClass() == Let.class) {
			return new Let(((Let) e).id, ((Let) e).t, ((Let) e).e1, rec_If(((Let) e).e2, i));
		} else {
			return new If(e, i.e2.accept(this), i.e3.accept(this));
		}
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input If expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(If e) {
		if (e.e1.getClass() == Not.class) {
			If new_e = new If(((Not) e.e1).e, e.e3, e.e2);
			return new_e.accept(this);
		} else if ((e.e1.getClass() != Eq.class) && (e.e1.getClass() != LE.class)) {
			If new_e = new If(new Eq(e.e1, new Bool(false)), e.e3, e.e2);
			return new_e.accept(this);
		} else {
			Exp l1 = e.e1.accept(this);
			return rec_If(l1, e);
		}

	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Let expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Let e) {
		Let l = new Let(e.id, e.t, e.e1.accept(this), e.e2.accept(this));
		return l;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Var expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Var e) {
		return e;
	}

	
	// print sequence of Exp
	List<Exp> rec_list(List<Exp> l) {
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

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input LetRec expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
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

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input App expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(App e) {
		List<Exp> la = new LinkedList<Exp>();
		Exp a = app_rec(rec_list(e.es), la, e.e);
		return a;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Tuple expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Tuple e){
		try {
			throw new NotYetImplementedException("Tuple NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input LetTuple expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(LetTuple e){

		try {
			throw new NotYetImplementedException("Let Tuple NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Array expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Array e){		
		try {
			throw new NotYetImplementedException("Array NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Get expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Get e) {
		try {
			throw new NotYetImplementedException("Get NotYetImplemented");
		} catch (NotYetImplementedException e1) { 
			
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}

	
	/**
 	 * Visitor returning the new expression K-Normalization process.
 	 * 
 	 * @param e	input Put expression
 	 * 
 	 * @return the new expression.
 	 */
	@Override
	public Exp visit(Put e) {
		try {
			throw new NotYetImplementedException("Put NotYetImplemented");
		} catch (NotYetImplementedException e1) {
			
			System.err.println(e1.getMessage()); System.exit(1);
		}
		return null;
	}
}
