import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;


public class ARM_Gen implements ObjVisitor<Exp> {
	
	static String[] register_tab = new String[16];
    FileWriter fw_arm;	 
    Stack<String> myStack;
    boolean tail;
	
	/**
	 * Constructor of the ARM_Gen class.
	 * 
	 * @param fw_arm	a file written in ARM
	 */
	public ARM_Gen(FileWriter fw_arm) {
        myStack = new Stack<String>();
        this.fw_arm = fw_arm;
        this.tail = true;
		for (int i = 0 ; i <=15;i++) {
			register_tab[i]="";
		}
	}


	/**
	 * Tries to write a string into fw_arm and catches an exception if it fails.
	 * 
	 * @param s	a string
	 */
    public void myWriter(String s){
    	try {
            fw_arm.write(s);
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }
	
    
	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Unit e) {
        return e;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Bool e) {
        return e;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Int e) {
        if (this.tail){
            // System.out.println("POPPING");
            String destReg = (String)this.myStack.pop();
            System.out.println("mov\t" + destReg + "\t" + "#" + e.i);
            myWriter("mov\t" + destReg + ", " + "#" + e.i + "\n");
        }else{
            // System.out.println("Pushing");
            this.myStack.push(Integer.toString(e.i));
        }
    	return e;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Float e) {
        return e;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Not e) {
       return new Not(e.e.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Neg e) {
    	return new Neg(e.e.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Add e) {
        // System.out.println("Current expression is " + e.toString() + "\t" + e.e1.toString() + "\t" + e.e2.toString());
    	return new Add(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
	public Exp visit(Sub e) {
    	return new Sub(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(FNeg e){
    	return new FNeg(e.e.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(FAdd e) {
    	return new FAdd(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
	public Exp visit(FSub e) {
    	return new FSub(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(FMul e) {
    	return new FMul(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(FDiv e){
    	return new FDiv(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Eq e){
    	return new Eq(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(LE e){
    	return new LE(e.e1.accept(this),e.e2.accept(this));
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(If e){
    	if (e.e1.getClass() == Eq.class) {
    		System.out.println("cmp\t" + ((Var)((Eq)e.e1).e1).id.id + "\t" + ((Var)((Eq)e.e1).e2).id.id);
    		System.out.println("beq\t" + "then");
    		System.out.println("bal\t" + "else");
    		System.out.println("then:\t");
    		Exp x = e.e2.accept(this);
    		System.out.println("bal\texit");
    		System.out.println("else:\t"); 
    		Exp y = e.e3.accept(this);
    		System.out.println("exit:");
    	}
    	
       return e;//new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
       
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Let e) {
        // System.out.println("Current expression is " + e.toString() + "\t" + e.id.toString());
        // System.out.println("PUSHING");
        this.myStack.push(e.id.id);
        // e.e1.accept(this);  
        // System.out.println("Current stack is " + this.myStack); 
        Exp newE1 = e.e1.accept(this);
        this.tail = true;

        Let l = new Let(e.id,e.t, newE1,e.e2.accept(this));
        return l;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Var e){
        // System.out.println("Current expression is " + e.toString() + "\t" + e.id.toString());
        // System.out.println("Current stack is " + this.myStack); 

        if ((this.tail == true)&&(!this.myStack.empty())){
            // System.out.println("POP");
            String destReg = (String) this.myStack.pop();
            System.out.println("mov\t" + destReg + "\t" + e.id.id);
            myWriter("mov\t" + destReg + ", " + e.id.id + "\n");
        }else{
            // System.out.println("PUSHING");
            this.myStack.push(e.id.id);
        }
        return e;
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

    public Exp visit(LetRec e){
    	
        // System.out.println("Current expression is " + e.toString());
    	FunDef fd2= new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
    	LetRec lr = new LetRec(fd2, e.e.accept(this));
    	return lr;
    }

// USELESS ??
    public Exp rec_app(Exp e, Exp app) {
        // System.out.println("Current expression is " + e.toString());
    	Let l = (Let)e;
    	if (l.id.id.equals("\0")) {
    		System.out.println(l.id.id);
    		return app;
    	}else {
    		return (Exp) new Let(l.id,l.t,l.e1,rec_app(l.e2,app));
    	}
    }
 
// USELESS ??
    Exp app_rec(List<Exp> la,List<Exp> llet,List<Exp> lvar,Exp e) {
    	if (la.isEmpty()) {
    		return new App(e,lvar);
    	}else {
    		Exp first = la.remove(0);
    		if(first.getClass()!=Let.class){
    			lvar.add(first);
    			return app_rec(la,llet,lvar ,e);
    		}else {
    			Let l = (Let) first;
    			Var x = new Var(l.id);
    			llet.add(l);
    			lvar.add(x);
    			return new Let(l.id,l.t,l.e1,
    					app_rec(la,llet,lvar,e)
    					);
    		}
    	}
    }
    
	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(App e){
        //System.out.println("Current expression is " + e.toString() + "\t App is " + ((Var)e.e).id + "\tArgument list is " + e.es.get(0));
        // System.out.println("Current expression is " + e.toString() + "\t App is " + e.e + "\tArgument list is " + e.es);
        this.tail = false;
        List<Exp> argList = printInfix2(e.es);

        if (((Var)e.e).id.id.equals("add")){
            // System.out.println("POPPING");
            String operand1 = (String) this.myStack.pop();
            // System.out.println("POPPING");
            String operand2 = (String) this.myStack.pop();
            // System.out.println("POPPING");
            String destReg = (String) this.myStack.pop();
            System.out.println("add\t" + destReg + "\t" + operand2 + "\t" + operand1);
            myWriter("add\t" + destReg + ", " + operand2 + ", " + operand1 + "\n");
            // System.out.println("Current stack is " + this.myStack); 
        }else if (((Var)e.e).id.id.equals("sub")){
            // System.out.println("POPPING");
            String operand1 = (String) this.myStack.pop();
            // System.out.println("POPPING");
            String operand2 = (String) this.myStack.pop();
            // System.out.println("POPPING");
            String destReg = (String) this.myStack.pop();
            System.out.println("sub\t" + destReg + "\t" + operand1 + "\t" + operand2);
            myWriter("sub\t" + destReg + ", " + operand1 + ", " + operand2 + "\n");
        }else if (((Var)e.e).id.id.equals("call")){
            // System.out.println("This is CALL. ArgList is " + e.es + " size is " + e.es.size());
        }else if (((Var)e.e).id.id.equals("_min_caml_print_int")){
            // System.out.println("This is PRINT. ARG is " + e.es.get(0).getClass() + " size is " + e.es.size());
            String destReg = "r0";
            if (e.es.get(0) instanceof Int){
                String number = (String)this.myStack.pop();
                System.out.println("mov\t" + destReg + "\t" + "#" + number);
                myWriter("mov\t" + destReg + ", " + "#" + number + "\n");
            }else{
                String operand1 = (String) this.myStack.pop();
                System.out.println("mov\t" + destReg + "\t" + operand1);
                myWriter("mov\t" + destReg + ", " + operand1 + "\n");
            }
            System.out.println("bl\tmin_caml_print_int");
            myWriter("bl\tmin_caml_print_int\n");
            tail = true;
            if (!myStack.isEmpty()) {
	            destReg = (String) this.myStack.pop();
	            System.out.println("mov\t" + destReg + "\t" + "r0");
	            myWriter("mov\t" + destReg + ", " + "r0");
            }
        }

        // App app = new App(e.e.accept(this),argList);
        return e;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Tuple e){
        // System.out.println("Current expression is " + e.toString());
        Tuple t = new Tuple(printInfix2(e.es));
        return t;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(LetTuple e){
        // System.out.println("Current expression is " + e.toString());
        LetTuple lt = new LetTuple(e.ids,e.ts,e.e1.accept(this),e.e2.accept(this));
        return lt;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Array e){
        // System.out.println("Current expression is " + e.toString());
    	Array a = new Array(e.e1.accept(this),e.e2.accept(this));
    	return a;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Get e){
        // System.out.println("Current expression is " + e.toString());
        Get g = new Get(e.e1.accept(this),e.e2.accept(this));
    	return g;
    }

	/**
	 * Visitor generating ARM code from the input expression.
	 * 
	 * @param e	input expression
	 * 
	 * @return ARM code from the input expression.
	 */
    public Exp visit(Put e){
        // System.out.println("Current expression is " + e.toString());
       Put p = new Put(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
    	return p;
    }
}
