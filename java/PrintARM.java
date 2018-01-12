import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;


public class PrintARM implements Visitor {
	
	static String[] register_tab = new String[16];
    int cpt_then;
    int cpt_else;
    int cpt_next;
    Stack<String> myStack;
    int current_index;
	List<Id> parameters;
    
	public PrintARM(int ci,List<Id> param) {
		 cpt_then=0;
	     cpt_else=0;
	     cpt_next=0;
	     current_index = ci;
	     parameters = new LinkedList<Id>();
	     parameters.addAll(param);
        myStack = new Stack<String>();
        //System.out.println("mov fp, sp");
		for (int i = 0 ; i <=15;i++) {
			register_tab[i]="";
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
    	System.out.print("1");
       }else {
       	System.out.print("0");
       }
    }

    public void visit(Int e) {
    	//System.out.println(e);
    	System.out.println("mov r5, #"+e.i);
            myStack.push("r5");
    }

    public void visit(Float e) {
    	//System.out.println(e);
    	//System.out.print(e.f);
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
		System.out.println("then: ");
		e.e2.accept(this);
		System.out.println("bal exit");
		System.out.println("else: "); 
		e.e3.accept(this);
		System.out.println("exit:");
    }
    
    public void visit(If e){
    	//System.out.println(e);
    	if (e.e1.getClass() == Eq.class) {
    		((Var)((Eq)e.e1).e1).accept(this);
    		 ((Var)((Eq)e.e1).e2).accept(this);
    		System.out.println("cmp " + myStack.pop() + ", " + myStack.pop());
    		System.out.println("beq " + "then");
    		System.out.println("bal " + "else");
    	}else {//if (e.e1.getClass() == Eq.class) {
    		System.out.println("cmp " + ((Var)((Eq)e.e1).e1).id.id + ", " + ((Var)((Eq)e.e1).e2).id.id);
    		System.out.println("ble " + "then");
    		System.out.println("bal " + "else");
    	}

		ifEpilogue(e);
       
    }

    public void visit(Let e) {
    	//System.out.println(e);
    	e.e1.accept(this);
    	if (!myStack.isEmpty()) {
    		System.out.println("strb "+myStack.pop()+", " + getFP(e.id));
    	}
    	e.e2.accept(this);
    	
    	/*System.out.print("mov ");
        myWriter("mov ");
        Var v1 = new Var(e.id);
        v1.accept(this);
        System.out.print(",");
        myWriter(", ");
        e.e1.accept(this);
    	System.out.print("\n");
        myWriter("\n");
        
    	e.e2.accept(this);*/
    }
    
    public int get_index(List<Id> lid, Id i) {
    	    	
    	for (int j= 0; j< lid.size();j++) {
    		if (lid.get(j).id.equals(i.id)) {
    			return j;
    		}
    	}
    	return -1;
    	
    }

    public void visit(Var e){
    	int index = get_index(parameters, e.id);
    	if(index!=-1) {
    		System.out.println("ldrb  r8, [ fp, #-"+ (1+index)+" ]");
	    	myStack.push("r8");	
    	}else {
	    	System.out.println("ldrb  r7, "+getFP(e.id));
	    	myStack.push("r7");
    	}
    	
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
    	if (((Var)e.e).id.id.equals("sub")){
     	   System.out.println("ldrb  "+"r4, " +getFP(((Var)e.es.get(0)).id));
     	   System.out.println("ldrb  "+"r5, " +getFP(((Var)e.es.get(1)).id));
     	   System.out.println("sub r6, r4, r5");
     	  // System.out.println("strb "+"r6, " +myStack.pop());
     	   myStack.push("r6");
     }else if (((Var)e.e).id.id.equals("add")){
    	   System.out.println("ldrb  "+"r4, " +getFP(((Var)e.es.get(0)).id));
    	   System.out.println("ldrb  "+"r5, " +getFP(((Var)e.es.get(1)).id));
    	   System.out.println("add r6, r4, r5");
    	  // System.out.println("strb "+"r6, " +myStack.pop());
    	   myStack.push("r6");
        }else if (((Var)e.e).id.id.equals("call")){
        	System.out.println("mov r9, #"+current_index);
        	System.out.println("add sp, fp, r9");
        	System.out.println("mov r9, #"+e.es.size());
        	System.out.println("add sp, sp, r9");
        	System.out.println("mov r9, #2");
        	System.out.println("add sp, sp, r9");     	
        	 printInfix2(e.es);
        	 
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_int"))||(((Var)e.e).id.id.equals("_min_caml_min_caml_print_int"))){
        	System.out.println("ldrb  "+"r0, " +getFP(((Var)e.es.get(0)).id));
            System.out.println("bl min_caml_print_int");
            
        }else {
        	for (int i = 0; i < e.es.size() ; i++) {
        		e.es.get(i).accept(this);
        		if (!myStack.isEmpty()) {
        	    	System.out.println("strb "+myStack.pop()+", " + "[ sp , #-" + (i+1) +" ]");
        	    }
        	}
        	System.out.println("bl " + ((Var)e.e).id.id );
        	 if (!myStack.isEmpty()) {
				  System.out.println("strb r0, " + myStack.pop());
			}
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
