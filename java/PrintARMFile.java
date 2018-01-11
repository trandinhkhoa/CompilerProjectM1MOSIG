import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;


public class PrintARMFile implements Visitor {
	
	static String[] register_tab = new String[16];
    int cpt_then;
    int cpt_else;
    int cpt_next;
    Stack<String> myStack;
    FileWriter fw_arm;	 
	
	public PrintARMFile(FileWriter fw) {
		 cpt_then=0;
	     cpt_else=0;
	     cpt_next=0;
	     myStack = new Stack<String>();
	        fw_arm = fw;
	        // System.out.println("HELOO");
	    	try {
	            this.fw_arm.write(".text\n.global _start\n_start:\n");
	            this.fw_arm.write("mov fp, sp\n");
	    	}
			catch (IOException exception)
			{
				System.err.println ("Error during the reading : " + exception.getMessage());
			}
			for (int i = 0 ; i <=15;i++) {
				register_tab[i]="";
			}
		}

	    public void myWriter(String s){
	    	try {
	            fw_arm.write(s);
	    	}
			catch (IOException exception)
			{
				System.err.println ("Error during the reading : " + exception.getMessage());
			}
	    }

   
	
    public String getFP(Id i) {
    	String s = i.id;
    	if (s.charAt(0)=='s') {
    		s = s.substring(1);
    		s = "[ fp, #"+s+" ]";
    	}
    	return s;
    }
    
    public void visit(Unit e) {
    }

    public void visit(Bool e) {
       if (e.b) {
    	myWriter("1");
       }else {
       	myWriter("0");
       }
    }

    public void visit(Int e) {
    	//System.out.println(e);
    	myWriter("mov r5, #"+e.i+"\n");
            myStack.push("r5");
    }

    public void visit(Float e) {
    	//System.out.println(e);
    	//myWriter(e.f);
        //myWriter(""+e.f);
    }

    public void visit(Not e) {
    	//System.out.println(e);
    }

    public void visit(Neg e) {
    	//System.out.println(e);
    	myStack.push("-");
         e.e.accept(this);  	
    }

    public void visit(Add e) {
    	//System.out.println(e);
        //ERROR
    }

	public void visit(Sub e) {
    	//System.out.println(e);
		 //ERROR
    }

    public void visit(FNeg e){
    	//System.out.println(e);
    	 //ERROR
    }

    public void visit(FAdd e) {
    	//System.out.println(e);
    	 //ERROR
    }

	public void visit(FSub e) {
    	//System.out.println(e);
		 //ERROR
    }

    public void visit(FMul e) {
    	//System.out.println(e);
    	 //ERROR
    }

    public void visit(FDiv e){
    	//System.out.println(e);
    	 //ERROR
    }

    public void visit(Eq e){
    	//System.out.println(e);
    	//
    }

    public void visit(LE e){
    	//System.out.println(e);
    	//
    }

    
    void ifEpilogue(If e) {
    	myWriter("then: \n");
		e.e2.accept(this);
		myWriter("bal exit\n");
		myWriter("else: \n"); 
		e.e3.accept(this);
		myWriter("exit:\n");
    }
    
    public void visit(If e){
    	//System.out.println(e);
    	if (e.e1.getClass() == Eq.class) {
    		((Var)((Eq)e.e1).e1).accept(this);
    		 ((Var)((Eq)e.e1).e2).accept(this);
    		 myWriter("cmp " + myStack.pop() + ", " + myStack.pop()+"\n");
    		 myWriter("beq " + "then\n");
    		 myWriter("bal " + "else\n");
    	}else {//if (e.e1.getClass() == Eq.class) {
    		myWriter("cmp " + ((Var)((Eq)e.e1).e1).id.id + ", " + ((Var)((Eq)e.e1).e2).id.id+"\n");
    		myWriter("ble " + "then\n");
    		myWriter("bal " + "else\n");
    	}

		ifEpilogue(e);
       
    }

    public void visit(Let e) {
    	//System.out.println(e);
    	e.e1.accept(this);
    	if (!myStack.isEmpty()) {
    		myWriter("strb "+myStack.pop()+", " + getFP(e.id)+"\n");
    	}
    	e.e2.accept(this);
    	
    	/*myWriter("mov ");
        myWriter("mov ");
        Var v1 = new Var(e.id);
        v1.accept(this);
        myWriter(",");
        myWriter(", ");
        e.e1.accept(this);
    	myWriter("\n");
        myWriter("\n");
        
    	e.e2.accept(this);*/
    }

    public void visit(Var e){
    	//System.out.println(e);
       /* if (e.id.id.charAt(0)=='s'){
    			String offset = e.id.id.substring(1);
	            myWriter("[ fp , "+offset+" ]");
	            myWriter("[ fp , "+offset+" ]"+ "\n");		
        }else {
        	myWriter(e.id.id);
        }*/
    	myWriter("ldrb  r7, "+getFP(e.id)+"\n");
    	myStack.push("r7");
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
        	  it.next().accept(this);
        }
        return new_list;
    }

    public void visit(LetRec e){
    	//System.out.println(e);
    	
        // System.out.println("Current expression is " + e.toString());
    }

  
    
    public void visit(App e){
    	//System.out.println(e);
       if (((Var)e.e).id.id.equals("add")){
    	   myWriter("ldrb  "+"r4, " +getFP(((Var)e.es.get(0)).id)+"\n");
    	   myWriter("ldrb  "+"r5, " +getFP(((Var)e.es.get(1)).id)+"\n");
    	   myWriter("add r6, r4, r5"+"\n");
    	  // System.out.println("strb "+"r6, " +myStack.pop());
    	   myStack.push("r6");
    	      	   
        /*}else if (((Var)e.e).id.id.equals("sub")){
            System.out.println("sub, " + destReg + ", " + operand1 + ", " + operand2);
            myWriter("sub, " + destReg + ", " + operand1 + ", " + operand2 + "\n");*/
        }else if (((Var)e.e).id.id.equals("call")){
        	 printInfix2(e.es);
        	 
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_int"))||(((Var)e.e).id.id.equals("_min_caml_min_caml_print_int"))){
        	myWriter("ldrb  "+"r0, " +getFP(((Var)e.es.get(0)).id)+"\n");
        	myWriter("bl min_caml_print_int"+"\n");
        }
    }

    public void visit(Tuple e){
        // System.out.println("Current expression is " + e.toString());
    }

    public void visit(LetTuple e){
        // System.out.println("Current expression is " + e.toString());
    }

    public void visit(Array e){
        // System.out.println("Current expression is " + e.toString());
    }

    public void visit(Get e){
        // System.out.println("Current expression is " + e.toString());
    }

    public void visit(Put e){
        // System.out.println("Current expression is " + e.toString());
    }
}
