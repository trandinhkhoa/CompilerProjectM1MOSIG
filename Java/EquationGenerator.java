import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;



public final class EquationGenerator {

	private void genEquations(final List<Tpair<Type, Type>> out, final stck<Type> env, final Exp exp, final Type type)
	{
		Visitor v = new Visitor(){
			@Override
        	public void visit(Unit e) {
        		typeEquals(type,new TUnit());
        	}

        	@Override
        	public void visit(Bool e) {
        		typeEquals(type,new TBool());
        	}

        	@Override
        	public void visit(Int e) {
        		typeEquals(type,new TInt());
        	}

        	@Override
        	public void visit(Float e) {
        		typeEquals(type,new TFloat());				
        	}

        	@Override
        	public void visit(Not e) {
        		typeEquals(type, new TBool());
                recOfType(e.e, new TBool());				
        	}

        	@Override
        	public void visit(Neg e) {
        		typeEquals(type, new TInt());
                recOfType(e.e, new TInt());				
        	}

        	@Override
        	public void visit(Add e) {
        		typeEquals(type, new TInt());
                recOfType(e.e1, new TInt());
                recOfType(e.e2, new TInt());				
        	}

        	@Override
        	public void visit(Sub e) {
        		typeEquals(type, new TInt());
                recOfType(e.e1, new TInt());
                recOfType(e.e2, new TInt());				
        	}

        	@Override
        	public void visit(FNeg e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e, new TFloat());				
        	}

        	@Override
        	public void visit(FAdd e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());				
        	}

        	@Override
        	public void visit(FSub e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());				
        	}

        	@Override
        	public void visit(FMul e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());				
        	}

        	@Override
        	public void visit(FDiv e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());        		
        	}

        	@Override
        	public void visit(Eq e) {
        		 Type t = Type.gen();
                 typeEquals(type, new TBool());
                 recOfType(e.e1, t);
                 recOfType(e.e2, t);        		
        	}

        	@Override
        	public void visit(LE e) {
                Type t = Type.gen();
                typeEquals(type, new TBool());
                recOfType(e.e1, t);
                recOfType(e.e2, t);        		
        	}

        	@Override
        	public void visit(If e) {
                recOfType(e.e1, new TBool());
                recOfType(e.e2, type);
                recOfType(e.e3, type);        		
        	}

        	@Override
        	public void visit(Let e) {
        		recOfType(e.e1, e.t);
        		env.push();
                {
            	env.put(e.id.id, e.t);
                recOfType(e.e2, type);
                }
                env.pop();        		
        	}

        	@Override
        	public void visit(Var e) {
        		Type t = env.get(e.id.id);
        		typeEquals(t,type);
        	}

        	@Override
        	public void visit(LetRec e) {
        		//int paraNum = e.fd.args.size();
        		
        		Type t1 = Type.gen();
        		Type t2 = Type.gen();
        		Type t3 = new TFun(t1,t2);
        		env.push();
                {
                	env.put(e.fd.id.id, t3);
                	//typeEquals(t1, type);
                	recOfType(e.e, type);
                	env.put(e.fd.args.get(0).id, t1);
                    recOfType(e.fd.e, t2);
                    //typeEquals(t1, t3);
                }
                env.pop();        		
        	}

        	@Override
        	public void visit(App e) { 
        		Type t1 = Type.gen();
        		recOfType(e.e, t1);
                repApp(t1, e.es.iterator(), type);
            }
        	
        	@Override
        	public void visit(Tuple e) {
        		// TODO Auto-generated method stub
        		
        	}

        	@Override
        	public void visit(LetTuple e) {
        		// TODO Auto-generated method stub
        		
        	}

        	@Override
        	public void visit(Array e) {
        		// TODO Auto-generated method stub
        		
        	}

        	@Override
        	public void visit(Get e) {
        		// TODO Auto-generated method stub
        		
        	}

        	@Override
        	public void visit(Put e) {
        		// TODO Auto-generated method stub
        		
        	}
        	private void repApp(Type t1, Iterator<Exp> args,Type rtrnType) {
                if (!args.hasNext()) {
                    typeEquals(t1, rtrnType);
                    return;
                }
                TFun newTyp = new TFun();
                typeEquals(t1, newTyp);

                Exp arg = args.next();
                recOfType(arg, newTyp.arg);

                repApp(newTyp.ret, args, rtrnType);
            }
        	private void typeEquals(Type t1, Type t2) {
        		out.add(new Tpair<>(t1, t2));
            }
        	
        	private void recOfType(Exp e, Type t) {
                if (e == exp) {
                    throw new RuntimeException("Infinite recursion");
                }
                genEquations(out, env, e, t);
            }
		};
		exp.accept(v);
	}
	public List<Tpair<Type, Type>> genEquations(Exp pgm,
			Map<String, Type> predefs) {
		List<Tpair<Type, Type>> out = new ArrayList<>();
        stck<Type> env = new stck<>();
        env.push();
        env.putAll(predefs);
        genEquations(out, env, pgm,new TUnit());
        return out;
	}
}
