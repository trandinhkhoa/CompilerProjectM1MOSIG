import java.util.List;

public class nestedLet implements ObjVisitor<Exp> {

	
	@Override
	public Exp visit(Let e) {
		// TODO Auto-generated method stub
		Id x = e.id;
		Type tx = e.t;
		Let E1 = (Let) e.e1.accept(this);
		
		Id y = E1.id;
		Type ty = E1.t;
		 
		Let e1 = (Let) E1.e1;
		Let e2 = (Let) E1.e2;
		
		Let e3 = (Let) e.e2.accept(this);
		
		return new Let(y,ty,e1,new Let(x,tx,e2,e3));
	}
	
	@Override
	public Exp visit(LetTuple e) {
		// TODO Auto-generated method stub
		List<Id> x = e.ids;
		List<Type> tx = e.ts;
		LetTuple E1 = (LetTuple) e.e1.accept(this);
		
		List<Id> y = E1.ids;
		List<Type> ty = E1.ts;
		Exp e1 = E1.e1;
		Exp e2 = E1.e2;
		
		Exp e3 = e.e2.accept(this);
		return new LetTuple(y,ty,e1,new LetTuple(x,tx,e2,e3));
		
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
