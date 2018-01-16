import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

public class Nested_Let implements ObjVisitor<Exp> {

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
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
	
 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(LetTuple e) {
        return e;
		
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(LetRec e) {
		// TODO Auto-generated method stub
		/* No modification for Let recursive */
        Exp e22 = e.e.accept(this);
        FunDef fd2 = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        LetRec lr = new LetRec(fd2, e22);
        
		return lr;
	}
	
 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(App e) {
		// TODO Auto-generated method stub
        App app;
        List<Exp> argList = printInfix2(e.es);
        app = new App(e.e.accept(this),argList);
        // try{
        //     // System.out.println("Current expression is " + e.toString()  + " It is " + e.e.getClass().toString());
        //     if (e.e.getClass().toString().equals("class Var"))
        //         System.out.println("Current expression is " + e.toString()  + " It is " + e.e.getClass() + "\t" + ((Var)e.e).id);
        //     else if (e.e.getClass().toString().equals("class Let"))
        //         System.out.println("Current expression is " + e.toString()  + " It is " + e.e.getClass() + "\t" + ((Let)e.e).id);
        // } catch(Exception error){
        //     error.printStackTrace(); 
        // }
		return app;
	}

    /**
 	 * Returns a linked list of the expressions.
 	 * 
 	 * @param l	input list of expressions
 	 * 
 	 * @return A linked list of the expressions.
 	 */
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

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Unit e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Bool e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Int e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Float e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
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

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Add e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Sub e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(FNeg e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(FAdd e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(FSub e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(FMul e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(FDiv e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Eq e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(LE e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(If e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Var e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Tuple e) {
		// TODO Auto-generated method stub
		return e;
	}
	
 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Array e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Get e) {
		// TODO Auto-generated method stub
		return e;
	}

 	/**
 	 * Visitor returning the expression after nested Let have been flattened.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return The reduced expression.
 	 */
	@Override
	public Exp visit(Put e) {
		// TODO Auto-generated method stub
		return e;
	}
	
	
	
	
	
	
}
