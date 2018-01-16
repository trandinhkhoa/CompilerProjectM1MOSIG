import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

/**
 * EquationGenerator class is the one which generates the type equations
 * for each expressions. The generated type equations are used
 * for type checking the equations
 */

public final class EquationGenerator {

	/**
	 * genEquations generates the type equations
	 * for expressions. This function is called recursively
	 * 
	 * @param List of type equations generated,environment, program, type
	 * 
	 * @return list of generated type equations
	 */
	private void genEquations(final List<Tpair<Type, Type>> out, final StckTbl<Type> env, final Exp exp, final Type type)
	{
		Visitor v = new Visitor(){
			@Override
			/**
		     * Visitor for generating new type TUnit.
		     *
		     * @param e input Unit expression
		     */
        	public void visit(Unit e) {
        		typeEquals(type,new TUnit());
        	}
			
			/**
		     * Visitor for generating new type TBool.
		     *
		     * @param e input Bool expression
		     */
        	@Override
        	public void visit(Bool e) {
        		typeEquals(type,new TBool());
        	}
        	
        	/**
		     * Visitor for generating new type TUnit.
		     *
		     * @param e input Int expression
		     */
        	@Override
        	public void visit(Int e) {
        		typeEquals(type,new TInt());
        	}
        	
        	/**
		     * Visitor for generating new type TFloat.
		     *
		     * @param e input Float expression
		     */
        	@Override
        	public void visit(Float e) {
        		typeEquals(type,new TFloat());				
        	}

        	/**
		     * Visitor for generating new type TBool 
		     * and making recursive call to genEquations.
		     *
		     * @param e input Not expression
		     */
        	@Override
        	public void visit(Not e) {
        		typeEquals(type, new TBool());
                recOfType(e.e, new TBool());				
        	}

        	/**
		     * Visitor for generating new type TInt 
		     * and making recursive call to genEquations
		     * for Negation operation.
		     *
		     * @param e input Neg expression
		     */
        	@Override
        	public void visit(Neg e) {
        		typeEquals(type, new TInt());
                recOfType(e.e, new TInt());				
        	}
        	
        	/**
		     * Visitor for generating new type TInt 
		     * and making recursive call to genEquations
		     * for addition operation.
		     *
		     * @param e input Add expression
		     */
        	@Override
        	public void visit(Add e) {
        		typeEquals(type, new TInt());
                recOfType(e.e1, new TInt());
                recOfType(e.e2, new TInt());				
        	}
        	
        	/**
		     * Visitor for generating new type TInt 
		     * and making recursive call to genEquations
		     * for subtraction operation.
		     *
		     * @param e input Sub expression
		     */
        	@Override
        	public void visit(Sub e) {
        		typeEquals(type, new TInt());
                recOfType(e.e1, new TInt());
                recOfType(e.e2, new TInt());				
        	}

        	/**
		     * Visitor for generating new type TFloat 
		     * and making recursive call to genEquations
		     * for Negation operation on float.
		     *
		     * @param e input FNeg expression
		     */
        	@Override
        	public void visit(FNeg e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e, new TFloat());				
        	}
        	
        	/**
		     * Visitor for generating new type TFloat
		     * and making recursive call to genEquations
		     * for addition operation on float.
		     *
		     * @param e input FAdd expression
		     */
        	@Override
        	public void visit(FAdd e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());				
        	}

        	/**
		     * Visitor for generating new type TFloat 
		     * and making recursive call to genEquations
		     * for subtraction operation on float.
		     *
		     * @param e input FSub expression
		     */
        	@Override
        	public void visit(FSub e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());				
        	}
        	
        	/**
		     * Visitor for generating new type TFloat 
		     * and making recursive call to genEquations
		     * for Multiplication operation on float.
		     *
		     * @param e input FMul expression
		     */
        	@Override
        	public void visit(FMul e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());				
        	}
        	
        	/**
		     * Visitor for generating new type TFloat 
		     * and making recursive call to genEquations
		     * for division operation on float.
		     *
		     * @param e input FDiv expression
		     */
        	@Override
        	public void visit(FDiv e) {
        		typeEquals(type, new TFloat());
                recOfType(e.e1, new TFloat());
                recOfType(e.e2, new TFloat());        		
        	}
        	
        	/**
		     * Visitor for generating new type TBool 
		     * and making recursive call to genEquations
		     * for performing equality operation.
		     *
		     * @param e input Eq expression
		     */
        	@Override
        	public void visit(Eq e) {
        		 Type t = Type.gen();
                 typeEquals(type, new TBool());
                 recOfType(e.e1, t);
                 recOfType(e.e2, t);        		
        	}
        	
        	/**
		     * Visitor for generating new type TBool 
		     * and making recursive call to genEquations
		     * for performing lesser than operation.
		     *
		     * @param e input LE expression
		     */
        	@Override
        	public void visit(LE e) {
                Type t = Type.gen();
                typeEquals(type, new TBool());
                recOfType(e.e1, t);
                recOfType(e.e2, t);        		
        	}

        	/**
		     * Visitor for generating new type TBool 
		     * and making recursive call to genEquations
		     * for performing If Else operation.
		     *
		     * @param e input If expression
		     */
        	@Override
        	public void visit(If e) {
                recOfType(e.e1, new TBool());
                recOfType(e.e2, type);
                recOfType(e.e3, type);        		
        	}
        	
        	/**
		     * Visitor for making recursive call to 
		     * genEquations for resolving Let operation.
		     *
		     * @param e input Let expression
		     */
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

        	/**
		     * Visitor for making recursive call to 
		     * genEquations for resolving Variable
		     * operation.
		     *
		     * @param e input Var expression
		     */
        	@Override
        	public void visit(Var e) {
        		Type t = env.get(e.id.id);
        		typeEquals(t,type);
        	}

        	/**
		     * Visitor for making recursive call to 
		     * genEquations for resolving LetRec operation.
		     *
		     * @param e input LetRec expression
		     */
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

        	/**
		     * Visitor for making recursive call to 
		     * genEquations for resolving application
		     * operation.
		     *
		     * @param e input App expression
		     */
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
	/**
	 * genEquations generates the type equations
	 * for expressions. This function is called recursively
	 * 
	 * @param Program and the predefined equations for the environment
	 * 
	 * @return list of generated type equations
	 */
	public List<Tpair<Type, Type>> genEquations(Exp pgm,
			Map<String, Type> predefs) {
		List<Tpair<Type, Type>> out = new ArrayList<>();
		StckTbl<Type> env = new StckTbl<>();
        env.push();
        env.putAll(predefs);
        genEquations(out, env, pgm,new TUnit());
        return out;
	}
}
