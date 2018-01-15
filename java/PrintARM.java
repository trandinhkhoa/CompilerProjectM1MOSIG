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
    int if_i;
    int els_i;
    int ex_i;
	
	public PrintARM(int ci,List<Id> param,int if_i,int els_i,int ex_i) {
	     cpt_else=0;
	     cpt_next=0;
	     this.if_i=if_i;
	     this.els_i=els_i;
	     this.ex_i=ex_i;
	     current_index = ci;
	     parameters = new LinkedList<Id>();
	     parameters.addAll(param);
	     myStack = new Stack<String>();
			for (int i = 0 ; i <=15;i++) {
				register_tab[i]="";
			}
		}


   
	
    public String getFP(Id i) {
    	String s = i.id;
    	if (s.charAt(0)=='s') {
    		s = s.substring(1);
    		int sn = Integer.parseInt(s);
    		// s = "[ fp, #"+(4*sn)+" ]";
    		s = "[ fp, #-"+(4*sn)+" ]";
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
    	//System.out.print(e);
    	System.out.print("mov r5, #"+e.i+"\n");
            myStack.push("r5");
    }

    public void visit(Float e) {
    	//System.out.print(e);
    	//System.out.print(e.f);
        //System.out.print(""+e.f);
    }

    public void visit(Not e) {
    	//System.out.print(e);
    }

    public void visit(Neg e) {
    	//System.out.print(e);
    	//myStack.push("-");
       //  e.e.accept(this);  	
    }

    public void visit(Add e) {
    	//System.out.print(e);
        //ERROR
    }

	public void visit(Sub e) {
    	//System.out.print(e);
		 //ERROR
    }

    public void visit(FNeg e){
    	//System.out.print(e);
    	 //ERROR
    }

    public void visit(FAdd e) {
    	//System.out.print(e);
    	 //ERROR
    }

	public void visit(FSub e) {
    	//System.out.print(e);
		 //ERROR
    }

    public void visit(FMul e) {
    	//System.out.print(e);
    	 //ERROR
    }

    public void visit(FDiv e){
    	//System.out.print(e);
    	 //ERROR
    }

    public void visit(Eq e){
    	//System.out.print(e);
    	//
    }

    public void visit(LE e){
    	//System.out.print(e);
    	//
    }

    
    void ifEpilogue(If e, int if_i, int els_i, int ex_i) {
    	System.out.print("then"+if_i+" :\n");
		e.e2.accept(this);
		System.out.print("bal exit"+ex_i+"\n");
		System.out.print("else"+els_i+": \n"); 
		e.e3.accept(this);
		System.out.print("exit"+ex_i+":\n");
    }
    
    public void visit(If e){
    	//System.out.print(e);
    	
    	int if_i2 = if_i;
    	int els_i2 = els_i;
    	int ex_i2=ex_i;

		this.if_i++;
		this.els_i++;
		this.ex_i++;
    	
    	if (e.e1.getClass() == Eq.class) {
    		((Var)((Eq)e.e1).e1).accept(this);
            System.out.print("mov r12, " + myStack.pop() + "\n");
    		 ((Var)((Eq)e.e1).e2).accept(this);
             // System.out.print("Im HERE\n" + myStack);
             //khoaNote: cmp r7 r7
    		 // System.out.print("cmp " + myStack.pop() + ", " + myStack.pop()+"\n");
    		 System.out.print("cmp r12" + ", " + myStack.pop()+"\n");
    		 System.out.print("beq " + "then"+if_i2+"\n");
    		 System.out.print("bal " + "else"+els_i2+"\n");
    	}else {//if (e.e1.getClass() == Eq.class) {
    		((Var)((LE)e.e1).e1).accept(this);
            System.out.print("mov r12, " + myStack.pop() + "\n");
    		 ((Var)((LE)e.e1).e2).accept(this);
    		// System.out.print("cmp " + ((Var)((Eq)e.e1).e1).id.id + ", " + ((Var)((Eq)e.e1).e2).id.id+"\n");
    		 System.out.print("cmp r12" + ", " + myStack.pop()+"\n");
    		System.out.print("ble " + "then"+if_i2+"\n");
    		System.out.print("bal " + "else"+els_i2+"\n");
    	}

		ifEpilogue(e,if_i2,els_i2,ex_i2);
       
    }

    public void visit(Let e) {
    	//System.out.print(e);
    	e.e1.accept(this);
    	if (!myStack.isEmpty()) {
    		System.out.print("str "+myStack.pop()+", " + getFP(e.id)+"\n");
    	}
    	e.e2.accept(this);
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
        // System.out.print("IM HERE. Parameter list size is " + parameters.size() +"\n");
    	if(index!=-1) {
    		// System.out.print("ldr  r8, [ fp, #-"+ ((1+index)*4)+" ]\n");
    		System.out.print("ldr  r8, [ fp, #"+ ((1+index)*4 + 8)+" ]\n");
	    	myStack.push("r8");	
    	}else {
    		System.out.print("ldr  r7, "+getFP(e.id)+"\n");
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
    	//System.out.print(e);
    	
        // System.out.print("Current expression is " + e.toString());
    }

  
    
    public void visit(App e){
    	//System.out.print(e);
    	if (((Var)e.e).id.id.equals("sub")){
     	   System.out.print("ldr  "+"r4, " +getFP(((Var)e.es.get(0)).id)+"\n");
     	   System.out.print("ldr  "+"r5, " +getFP(((Var)e.es.get(1)).id)+"\n");
     	   System.out.print("sub r6, r4, r5\n");
     	  // System.out.print("str "+"r6, " +myStack.pop());
     	   myStack.push("r6");
     }else if (((Var)e.e).id.id.equals("add")){
    	   System.out.print("ldr  "+"r4, " +getFP(((Var)e.es.get(0)).id)+"\n");
    	   System.out.print("ldr  "+"r5, " +getFP(((Var)e.es.get(1)).id)+"\n");
    	   System.out.print("add r6, r4, r5\n");
    	  // System.out.print("str "+"r6, " +myStack.pop());
    	   myStack.push("r6");
        }else if (((Var)e.e).id.id.equals("call")){
        	// System.out.print("mov r9, #"+(current_index)*4+"\n");
        	// System.out.print("add sp, fp, r9\n");
        	// System.out.print("mov r9, #"+e.es.size()*4+"\n");
        	// System.out.print("add sp, sp, r9\n");
        	// System.out.print("mov r9, #"+2*4+"\n");
        	// System.out.print("add sp, sp, r9\n");     	

            if (((current_index) * 4) <=-255){
                System.out.print("mov r9, #-"+(current_index)*4+"\n");
            }
            else{
                // System.out.print("ldr r9, =#0x-"+Integer.toHexString((current_index)*4)+"\n"); //should be this spill3 reverse the sign to correct, mov also work?
                System.out.print("ldr r9, =#0x"+Integer.toHexString((current_index)*4)+"\n");
            }
        	System.out.print("add sp, fp, r9\n");
        	System.out.print("mov r9, #-"+e.es.size()*4+"\n");
        	System.out.print("add sp, sp, r9\n");
        	System.out.print("mov r9, #-"+2*4+"\n");
        	System.out.print("add sp, sp, r9\n");     	
        	 printInfix2(e.es);
        	 
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_int"))||(((Var)e.e).id.id.equals("_min_caml_min_caml_print_int"))){
        	System.out.print("ldr  "+"r0, " +getFP(((Var)e.es.get(0)).id)+"\n");
            System.out.print("bl min_caml_print_int\n");
            
        }else {
        	for (int i = 0; i < e.es.size() ; i++) {
        		e.es.get(i).accept(this);
        		if (!myStack.isEmpty()) {
                    // System.out.print("IM HERE\n");
        	    	// System.out.print("str "+myStack.pop()+", " + "[ sp , #-" + ((i+1)*4) +" ]\n");
        	    	System.out.print("str "+myStack.pop()+", " + "[ sp , #" + ((i+1)*4) +" ]\n");
        	    }
        	}
        	System.out.print("bl " + ((Var)e.e).id.id +"\n");
        	 if (!myStack.isEmpty()) {
				  System.out.print("mov r0, " + myStack.pop()+"\n");
			}
        	 
        	myStack.push("r0");
        }
    }

    public void visit(Tuple e){
        // System.out.print("Current expression is " + e.toString());
    }

    public void visit(LetTuple e){
        // System.out.print("Current expression is " + e.toString());
    }

    public void visit(Array e){
        // System.out.print("Current expression is " + e.toString());
    }

    public void visit(Get e){
        // System.out.print("Current expression is " + e.toString());
    }

    public void visit(Put e){
        // System.out.print("Current expression is " + e.toString());
    }
}
