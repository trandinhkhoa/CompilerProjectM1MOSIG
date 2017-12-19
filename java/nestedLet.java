
public class nestedLet implements ObjVisitor<Exp> {

	@Override
	public Exp visit(Let e) {
		// TODO Auto-generated method stub
		//Id x = e.id;
		//Type tx = e.t;
		if (e.e1.getClass() == Let.class && e.e2.getClass() == Let.class) {
			e.e2 = e.e2.accept(this);

			Let E1 = (Let) e.e1.accept(this);
			//Id y = E1.id;
			//Type ty = E1.t;
			e.e1 = E1.e2;
			E1.e2 = e;
			E1.accept(this);
			return E1;
		}
		e.e1 = e.e1.accept(this);
	
		return e;
	}

	@Override
	public Exp visit(LetTuple e) {
		// TODO Auto-generated method stub
		//List<Id> x = e.ids;
		//List<Type> tx = e.ts;
		if (e.e1.getClass() == LetTuple.class) {
			e.e2 = e.e2.accept(this);

			Let E1 = (Let) e.e1.accept(this);
			//List<Id> y = E1.ids;
			//List<Type> ty = E1.ts;
			
			e.e1 = E1.e2;
			E1.e2 = e;
			E1.accept(this);
			return E1;
		}
		e.e1 = e.e1.accept(this);
		return e;

	}

	@Override
	public Exp visit(LetRec e) {
		// TODO Auto-generated method stub
		/* No modification for Let recursive */
		FunDef fd2 = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
		LetRec lr = new LetRec(fd2, e.e.accept(this));
		return lr;
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
