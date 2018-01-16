import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

public class Nested_Let implements ObjVisitor<Exp> {

	
	@Override
	public Exp visit(Let e) {
        if (e.e1.getClass() == e.getClass()){
            Id y = e.id;
            Let let1 = (Let)e.e1.accept(this);
            Let let2= new Let(e.id, e.t, let1.e2, e.e2); 
            Let newLet = new Let(let1.id, let1.t, let1.e1, let2);     
            Let newLet2 = (Let)newLet.accept(this);                //Travel the tree again to check if there is nested Let left
            return newLet2;
        } else{
            Let newLet = new Let(e.id, e.t, e.e1, e.e2.accept(this));
            Let newLet2 = new Let(newLet.id,newLet.t, newLet.e1,newLet.e2);
            return newLet2;
        }
	}
	
	@Override
	public Exp visit(LetTuple e) {
        return e;
		
	}

	@Override
	public Exp visit(LetRec e) {
		/* No modification for Let recursive */
        Exp e22 = e.e.accept(this);
        FunDef fd2 = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        LetRec lr = new LetRec(fd2, e22);
        
		return lr;
	}
	
	@Override
	public Exp visit(App e) {
        App app;
        List<Exp> argList = printInfix2(e.es);
        app = new App(e.e.accept(this),argList);
		return app;
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

	@Override
	public Exp visit(Unit e) {
		return e;
	}

	@Override
	public Exp visit(Bool e) {
		return e;
	}

	@Override
	public Exp visit(Int e) {
		return e;
	}

	@Override
	public Exp visit(Float e) {
		return e;
	}

	@Override
	public Exp visit(Not e) {
		return e;
	}

	@Override
	public Exp visit(Neg e) {
		return e;
	}

	@Override
	public Exp visit(Add e) {
		return e;
	}

	@Override
	public Exp visit(Sub e) {
		return e;
	}

	@Override
	public Exp visit(FNeg e) {
		return e;
	}

	@Override
	public Exp visit(FAdd e) {
		return e;
	}

	@Override
	public Exp visit(FSub e) {
		return e;
	}

	@Override
	public Exp visit(FMul e) {
		return e;
	}

	@Override
	public Exp visit(FDiv e) {
		return e;
	}

	@Override
	public Exp visit(Eq e) {
		return e;
	}

	@Override
	public Exp visit(LE e) {
		return e;
	}

	@Override
	public Exp visit(If e) {
		return e;
	}

	@Override
	public Exp visit(Var e) {
		return e;
	}

	@Override
	public Exp visit(Tuple e) {
		return e;
	}
	
	@Override
	public Exp visit(Array e) {
		return e;
	}

	@Override
	public Exp visit(Get e) {
		return e;
	}

	@Override
	public Exp visit(Put e) {
		return e;
	}
}
