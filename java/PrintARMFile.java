import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Visitor writing the ARM code of the associated expression into the given file.
 * 
 */
public class PrintARMFile implements Visitor {
	
	static String[] register_tab = new String[16];
    Stack<String> myStack;
    FileWriter fw_arm;	
    int current_index;
    List<Id> parameters;
    public int if_i;
	
    
    /** 
	 * Constructor for the PrintARMFile.
 	 * 
 	 * @param fw the FileWriter 
 	 * @param ci the current index of the stack
 	 * @param param the list of parameters
 	 * @param if_i the current number of if
 	 * 
 	 */
	public PrintARMFile(FileWriter fw,int ci,List<Id> param,int if_i) {
	     current_index = ci;
	     this.if_i=if_i;
	     parameters = new LinkedList<Id>();
	     parameters.addAll(param);
	     myStack = new Stack<String>();
	        fw_arm = fw;
			for (int i = 0 ; i <=15;i++) {
				register_tab[i]="";
			}
		}

    void myWriter(String s){
    	try {
            fw_arm.write(s);
    	}
		catch (IOException exception)
		{
			System.err.println ("Error during the reading : " + exception.getMessage());
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
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Unit expression
 	* 
 	 */
	@Override
	public void visit(Unit e) {
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Bool expression
 	* 
 	 */
	@Override
	public void visit(Bool e) {
    	if (e.b) {
    		myWriter("mov r10 , #1\n");
       }else {
    	   myWriter("mov r10, #0\n");
       }
       myStack.push("r10");
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Int expression
 	* 
 	 */
	@Override
	public void visit(Int e) {
    	if (((e.i) >=-255 )&&((e.i) <=255 )){
    		myWriter("mov r5, #"+e.i+"\n");
        }
        else{
        	myWriter("ldr r5, =#0x"+Integer.toHexString(e.i)+"\n");
        }
    	   myStack.push("r5");       
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Float expression
 	* 
 	 */
	@Override
	public void visit(Float e) {
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Not expression
 	* 
 	 */
	@Override
	public void visit(Not e) {
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Neg expression
 	* 
 	 */
	@Override
	public void visit(Neg e) {	
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Add expression
 	* 
 	 */
	@Override
	public void visit(Add e) {
    }

	/** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Sub expression
 	* 
 	 */
	@Override
	public void visit(Sub e) {
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input FNeg expression
 	* 
 	 */
	@Override
	public void visit(FNeg e){
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input FAdd expression
 	* 
 	 */
	@Override
	public void visit(FAdd e) {
    }

	/** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input FSub expression
 	* 
 	 */
	@Override
	public void visit(FSub e) {
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input FMul expression
 	* 
 	 */
	@Override
	public void visit(FMul e) {
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input FDiv expression
 	* 
 	 */
	@Override
	public void visit(FDiv e){
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Eq expression
 	* 
 	 */
	@Override
	public void visit(Eq e){
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input LE expression
 	* 
 	 */
	@Override
	public void visit(LE e){
    }

    
    void ifEpilogue(If e, int if_i) {
    	myWriter("then"+if_i+" :\n");
		e.e2.accept(this);
		myWriter("bal exit"+if_i+"\n");
		myWriter("else"+if_i+": \n"); 
		e.e3.accept(this);
		myWriter("exit"+if_i+":\n");
    }
    
    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
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
            myWriter("mov r12, " + myStack.pop() + "\n");
    		 ((Var)((Eq)e.e1).e2).accept(this);
    		 myWriter("cmp r12" + ", " + myStack.pop()+"\n");
    		 myWriter("beq " + "then"+if_i2+"\n");
    		 myWriter("bal " + "else"+if_i2+"\n");
    	}else if (e.e1.getClass() == LE.class) {
    		((Var)((LE)e.e1).e1).accept(this);
            myWriter("mov r12, " + myStack.pop() + "\n");
    		 ((Var)((LE)e.e1).e2).accept(this);
    		 myWriter("cmp r12" + ", " + myStack.pop()+"\n");
    		myWriter("ble " + "then"+if_i2+"\n");
    		myWriter("bal " + "else"+if_i2+"\n");   	    
        }else if (e.e1.getClass() == Bool.class){
        		((Bool)e.e1).accept(this);
        		myWriter("mov r12, " + myStack.pop() + "\n");
        		myWriter("cmp r12" + ", #1\n");
        		myWriter("ble " + "then"+if_i2+"\n");
        		myWriter("bal " + "else"+if_i2+"\n");
        	}

		ifEpilogue(e,if_i2);
       
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Let expression
 	* 
 	 */
	@Override
	public void visit(Let e) {
    	e.e1.accept(this);
    	if (!myStack.isEmpty()) {
    		myWriter("str "+myStack.pop()+", " + getFP(e.id)+"\n");
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
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Var expression
 	* 
 	 */
	@Override
	public void visit(Var e){
    	int index = get_index(parameters, e.id);
    	if(index!=-1) {
    		myWriter("ldr  r8, [ fp, #"+ ((1+index)*4 + 8)+" ]\n");
	    	myStack.push("r8");	
    	}else {
    		myWriter("ldr  r7, "+getFP(e.id)+"\n");
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
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input LetRec expression
 	* 
 	 */
	@Override
	public void visit(LetRec e){
    }

  
    
    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input App expression
 	* 
 	 */
	@Override
	public void visit(App e){
    	if (((Var)e.e).id.id.equals("sub")){
     	   myWriter("ldr  "+"r4, " +getFP(((Var)e.es.get(0)).id)+"\n");
     	   myWriter("ldr  "+"r5, " +getFP(((Var)e.es.get(1)).id)+"\n");
     	   myWriter("sub r6, r4, r5\n");
     	   myStack.push("r6");
     }else if (((Var)e.e).id.id.equals("add")){
    	   myWriter("ldr  "+"r4, " +getFP(((Var)e.es.get(0)).id)+"\n");
    	   myWriter("ldr  "+"r5, " +getFP(((Var)e.es.get(1)).id)+"\n");
    	   myWriter("add r6, r4, r5\n");
    	   myStack.push("r6");
        }else if (((Var)e.e).id.id.equals("call")){   	
        	 printInfix2(e.es);
        	 
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_int"))||(((Var)e.e).id.id.equals("_min_caml_min_caml_print_int"))){
        	myWriter("ldr  "+"r0, " +getFP(((Var)e.es.get(0)).id)+"\n");
            myWriter("bl min_caml_print_int\n");
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_char"))){
        	myWriter("ldr  "+"r0, " +getFP(((Var)e.es.get(0)).id)+"\n");
            myWriter("bl min_caml_print_char\n");
        }else if ((((Var)e.e).id.id.equals("_min_caml_print_newline"))){ 
        	myWriter("bl min_caml_print_newline\n");
        }else {


           if (((current_index) * 4) <= 255){
                myWriter("mov r9, #-"+(current_index)*4+"\n");
            }
            else{
                myWriter("ldr r9, =#0x"+Integer.toHexString((current_index)*4)+"\n");
            }
        	myWriter("add sp, fp, r9\n");
        	myWriter("mov r9, #-"+e.es.size()*4+"\n");
        	myWriter("add sp, sp, r9\n");
        	myWriter("mov r9, #-"+2*4+"\n");
        	myWriter("add sp, sp, r9\n");     	
        	for (int i = 0; i < e.es.size() ; i++) {
        		e.es.get(i).accept(this);
        		if (!myStack.isEmpty()) {
        	    	myWriter("str "+myStack.pop()+", " + "[ sp , #" + ((i+1)*4) +" ]\n");
        	    }
        	}

        	myWriter("bl " + ((Var)e.e).id.id +"\n");
        	 if (!myStack.isEmpty()) {
				  myWriter("mov r0, " + myStack.pop()+"\n");
			}
        	 
        	myStack.push("r0");
        }
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Tuple expression
 	* 
 	 */
	@Override
	public void visit(Tuple e){
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input LetTuple expression
 	* 
 	 */
	@Override
	public void visit(LetTuple e){
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Array expression
 	* 
 	 */
	@Override
	public void visit(Array e){
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Get expression
 	* 
 	 */
	@Override
	public void visit(Get e){
    }

    /** 
	 * Visitor writing the ARM code of the given expression with fw_arm.
 	 * 
 	 * @param e	input Put expression
 	* 
 	 */
	@Override
	public void visit(Put e){
    }
}
