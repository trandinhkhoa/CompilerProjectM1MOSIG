import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Visitor printing the ARM code of the associated expression on the standard output.
 * 
 */
public class PrintARM implements Visitor {
	
	static String[] register_tab = new String[16];
    Stack<String> myStack;
    int current_index;
    List<Id> parameters;
    int if_i;
	
    /** 
   	 * Constructor for the PrintARM.
	 * 
	 * @param ci the current index of the stack
	 * @param param the list of parameters
	 * @param if_i the current number of if
	 * 
	 */
	public PrintARM(int ci,List<Id> param,int if_i) {
	     this.if_i=if_i;
	     current_index = ci;
	     parameters = new LinkedList<Id>();
	     parameters.addAll(param);
	     myStack = new Stack<String>();
			for (int i = 0 ; i <=15;i++) {
				register_tab[i]="";
			}
		}
	
    String getFP(Id i) {
    	String s = i.id;
    	if (s.charAt(0)=='s') {
    		s = s.substring(1);
    		int sn = Integer.parseInt(s);
    		s = "[ fp, #-"+(4*sn)+" ]";
    	}
    	return s;
    }
    
     /** 
	 * This function shouldn't be used, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input Unit expression
 	* 
 	 */
	@Override
	public void visit(Unit e) {
    }

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input Bool expression
 	* 
 	 */
	@Override
	public void visit(Bool e) {
    	if (e.b) {
    		System.out.print("mov r10 , #1\n");
       }else {
    	   System.out.print("mov r10, #0\n");
       }
       myStack.push("r10");
    }

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input Int expression
 	* 
 	 */
	@Override
	public void visit(Int e) {
            
            if (((e.i) >=-255 )&&((e.i) <=255 )){
            	System.out.print("mov r5, #"+e.i+"\n");
            }
            else{
                System.out.print("ldr r5, =#0x"+Integer.toHexString(e.i)+"\n");
            }

            myStack.push("r5");            
    }

     /** 
	 * Function not implemented yet, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input Float expression
 	* 
 	 */
	@Override
	public void visit(Float e) {
    }

     /** 
	 * This function shouldn't be used, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input Not expression
 	* 
 	 */
	@Override
	public void visit(Not e) {
    }

     /** 
	 * This function shouldn't be used, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input Neg expression
 	* 
 	 */
	@Override
	public void visit(Neg e) {
    }

     /** 
	 * This function shouldn't be used, we cannot tell you what will happened if you use it now.on.
 	 * 
 	 * @param e	input Add expression
 	* 
 	 */
	@Override
	public void visit(Add e) {
    }

	 /** 
	 * This function shouldn't be used, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input Sub expression
 	* 
 	 */
	@Override
	public void visit(Sub e) {
    }

     /** 
	 * Function not implemented yet, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input FNeg expression
 	* 
 	 */
	@Override
	public void visit(FNeg e){
    }

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input FAdd expression
 	* 
 	 */
	@Override
	public void visit(FAdd e) {
    }

	 /** 
	 * Function not implemented yet, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input FSub expression
 	* 
 	 */
	@Override
	public void visit(FSub e) {
    }

     /** 
	 * Function not implemented yet, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input FMul expression
 	* 
 	 */
	@Override
	public void visit(FMul e) {
    }

     /** 
	 * Function not implemented yet, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input FDiv expression
 	* 
 	 */
	@Override
	public void visit(FDiv e){
    }

     /** 
	 * This function shouldn't be used, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input Eq expression
 	* 
 	 */
	@Override
	public void visit(Eq e){
    }

     /** 
	 * This function shouldn't be used, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input LE expression
 	* 
 	 */
	@Override
	public void visit(LE e){
    }

    
    void ifEpilogue(If e, int if_i) {
    	System.out.print("then"+if_i+" :\n");
		e.e2.accept(this);
		System.out.print("bal exit"+if_i+"\n");
		System.out.print("else"+if_i+": \n"); 
		e.e3.accept(this);
		System.out.print("exit"+if_i+":\n");
    }
    
     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input If expression
 	* 
 	 */
	@Override
	public void visit(If e){    	
    	int if_i2 = if_i;

		this.if_i++;
    	
    	if (e.e1.getClass() == Eq.class) {
    		((Var)((Eq)e.e1).e1).accept(this);
            System.out.print("mov r12, " + myStack.pop() + "\n");
    		 ((Var)((Eq)e.e1).e2).accept(this);
    		 System.out.print("cmp r12" + ", " + myStack.pop()+"\n");
    		 System.out.print("beq " + "then"+if_i2+"\n");
    		 System.out.print("bal " + "else"+if_i2+"\n");
    	}else if (e.e1.getClass() == LE.class) {
    		((Var)((LE)e.e1).e1).accept(this);
            System.out.print("mov r12, " + myStack.pop() + "\n");
    		 ((Var)((LE)e.e1).e2).accept(this);
    		 System.out.print("cmp r12" + ", " + myStack.pop()+"\n");
    		System.out.print("ble " + "then"+if_i2+"\n");
    		System.out.print("bal " + "else"+if_i2+"\n");	    
        }else if (e.e1.getClass() == Bool.class){
        		((Bool)e.e1).accept(this);
        		System.out.print("mov r12, " + myStack.pop() + "\n");
        		System.out.print("cmp r12" + ", #1\n");
        		System.out.print("ble " + "then"+if_i2+"\n");
        		System.out.print("bal " + "else"+if_i2+"\n");
        	}

		ifEpilogue(e,if_i2);
       
    }

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input Let expression
 	* 
 	 */
	@Override
	public void visit(Let e) {
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

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input Var expression
 	* 
 	 */
	@Override
	public void visit(Var e){
    	int index = get_index(parameters, e.id);
    	if(index!=-1) {
    		System.out.print("ldr  r8, [ fp, #"+ ((1+index)*4 + 8)+" ]\n");
	    	myStack.push("r8");	
    	}else {
    		System.out.print("ldr  r7, "+getFP(e.id)+"\n");
	    	myStack.push("r7");
    	}
    	
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

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input LetRec expression
 	* 
 	 */
	@Override
	public void visit(LetRec e){
    }

  
    
     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input App expression
 	* 
 	 */
	@Override
	public void visit(App e){
    	if (((Var)e.e).id.id.equals("sub")){
     	   System.out.print("ldr  "+"r4, " +getFP(((Var)e.es.get(0)).id)+"\n");
     	   System.out.print("ldr  "+"r5, " +getFP(((Var)e.es.get(1)).id)+"\n");
     	   System.out.print("sub r6, r4, r5\n");
     	   myStack.push("r6");
     }else if (((Var)e.e).id.id.equals("add")){
    	   System.out.print("ldr  "+"r4, " +getFP(((Var)e.es.get(0)).id)+"\n");
    	   System.out.print("ldr  "+"r5, " +getFP(((Var)e.es.get(1)).id)+"\n");
    	   System.out.print("add r6, r4, r5\n");
    	   myStack.push("r6");
        }else if (((Var)e.e).id.id.equals("call")){
        
        	printInfix2(e.es);
        	 
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_int"))||(((Var)e.e).id.id.equals("_min_caml_min_caml_print_int"))){
        	System.out.print("ldr  "+"r0, " +getFP(((Var)e.es.get(0)).id)+"\n");
            System.out.print("bl min_caml_print_int\n");
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_char"))){
        	System.out.print("ldr  "+"r0, " +getFP(((Var)e.es.get(0)).id)+"\n");
        	System.out.print("bl min_caml_print_char\n");
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_newline"))){ 
        	System.out.print("bl min_caml_print_newline\n");  
        }else {


            if (((current_index) * 4) <=255){
                System.out.print("mov r9, #-"+(current_index)*4+"\n");
            }
            else{
                System.out.print("ldr r9, =#0x"+Integer.toHexString((current_index)*4)+"\n");
            }
        	System.out.print("add sp, fp, r9\n");
        	System.out.print("mov r9, #-"+e.es.size()*4+"\n");
        	System.out.print("add sp, sp, r9\n");
        	System.out.print("mov r9, #-"+2*4+"\n");
        	System.out.print("add sp, sp, r9\n");     	
        	 printInfix2(e.es);


        	for (int i = 0; i < e.es.size() ; i++) {
        		e.es.get(i).accept(this);
        		if (!myStack.isEmpty()) {
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

     /** 
	 * Function not implemented yet, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input Tuple expression
 	* 
 	 */
	@Override
	public void visit(Tuple e){
    }

     /** 
	 * Function not implemented yet, we cannot tell you what will happened if you use it now.
 	 * 
 	 * @param e	input LetTuple expression
 	* 
 	 */
	@Override
	public void visit(LetTuple e){
    }

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input Array expression
 	* 
 	 */
	@Override
	public void visit(Array e){
    }

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input Get expression
 	* 
 	 */
	@Override
	public void visit(Get e){
    }

     /** 
	 * Visitor printing the ARM code of the given expression.
 	 * 
 	 * @param e	input Put expression
 	* 
 	 */
	@Override
	public void visit(Put e){
    }
}
