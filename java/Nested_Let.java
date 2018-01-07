import java.util.List;

/**
 * Nested Let class eliminates all the nested lets except letRec 
 * This phase is inclusive in the optimization level.
 * */

public class Nested_Let implements ObjVisitor<Exp> {

	
	@Override
	public Exp visit(Let e) {
		// TODO Auto-generated method stub
        // System.out.println("Current expression is " + e.e1.getClass());
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
		// TODO Auto-generated method stub
		/* No modification for Let recursive */
		return e;
	}
	
	@Override
	public Exp visit(Unit e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Bool e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Int e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Float e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Not e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Neg e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Add e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Sub e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(FNeg e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(FAdd e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(FSub e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(FMul e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(FDiv e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Eq e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(LE e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(If e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Var e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(App e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Tuple e) {
		// TODO Auto-generated method stub
		return e;
	}
	
	@Override
	public Exp visit(Array e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Get e) {
		// TODO Auto-generated method stub
		return e;
	}

	@Override
	public Exp visit(Put e) {
		// TODO Auto-generated method stub
		return e;
	}
	
	
	
	
	
	
}
