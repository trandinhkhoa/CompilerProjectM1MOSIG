import java.util.List;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Closure_Element {
	
	public String label;
	public List<Id> free_variables;
	public List<Id> parameters;
	public Exp code;

	public Closure_Element(Exp e) {
		this.label = "_";
		this.free_variables = new LinkedList<Id>();
		this.parameters = new LinkedList<Id>();
		this.code = e;
	}
	
	public Closure_Element(LetRec lr) {
		this.label = lr.fd.id.id;
		this.free_variables = new LinkedList<Id>();
		this.parameters = new LinkedList<Id>();
		this.parameters.addAll(lr.fd.args);
		this.code = lr.fd.e;
	}
	
	public void set_Exp(Exp e) {
		this.code = e;
	}
	
	public void print() {
		System.out.println("Label: " + label);
		System.out.print("Free variables: ");
		if (free_variables.size()==0) {
			System.out.println("none");
		}else {
			for(int i = 0; i<free_variables.size()-1;i++) {
				System.out.print(""+free_variables.get(i).id+", ");
			}
			System.out.println(free_variables.get(free_variables.size()-1).id);;
		}
		System.out.print("Parameters: ");
		if (parameters.size()==0) {
			System.out.println("none");
		}else {
			for(int i = 0; i<parameters.size()-1;i++) {
				System.out.print(""+parameters.get(i).id+", ");
			}
			System.out.println(parameters.get(parameters.size()-1).id);;
		}
		System.out.println("Code: ");
		code.accept(new PrintASML());
	}
	
	public void printIn(FileWriter fw){
		try {
			
			fw.write("let " + label);
			Iterator<Id> it = parameters.iterator();
			while (it.hasNext()){
				fw.write(" "+it.next());
			}
			fw.write(" = ");
			
			
			code.accept(new PrintASMLFile(fw));
		}
		catch (IOException exception)
		{
			System.out.println ("Error during the writing : " + exception.getMessage());
		}
	}
	
	
	public void printASML() {
		System.out.print("let " + label);
		Iterator<Id> it = parameters.iterator();
		while (it.hasNext()){
			System.out.print(" "+it.next());
		}
		System.out.println(" = ");
		code.accept(new PrintASML());		
	}
	
	public void headerFile(FileWriter fw) {
		try {
			if(this.label.equals("_")) {
				fw.write("_start:\n");
			}else {
				fw.write(this.label+":\n");
			}
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
	}
	
	
	public void prologue() {
		String n = ""+parameters.size();
		/*System.out.println("stmfd  sp!, {fp, lr}   # save fp and lr on the stack\n" + 
				"add fp, sp, #4         # position fp on the address of old fp\n" + 
				"sub sp, #"+ n +" # allocate memory to store local variables");*/
		
		System.out.println("stmfd sp!, {fp, lr}");
		System.out.println("add fp, sp, #4");
	}
	
	public void epilogue() {
		/*System.out.println("sub sp, fp, #4\n" + 
				"ldmfd  sp!, {fp, lr}  \n" + 
				"bx lr  ");*/
		System.out.println("sub sp, sp, #4");
		System.out.println("lmdfd sp!, {fp,lr}");
		System.out.println("bx lr");
	}
	
	public void prologueFile(FileWriter fw) {
		String n = ""+parameters.size();
		try {
		/*fw.write("stmfd  sp!, {fp, lr}   # save fp and lr on the stack\n" + 
				"add fp, sp, #4         # position fp on the address of old fp\n" + 
				"sub sp, #"+ n +" # allocate memory to store local variables\n");*/
		fw.write("stmfd sp!, {fp, lr}\n");
		fw.write("add fp, sp, #4\n");
			
		}catch(Exception e) {
			System.err.println ("Error during the reading : " + e.getMessage());
		}
	}
	
	public void epilogueFile(FileWriter fw) {
		try {
		/*fw.write("sub sp, fp, #4\n" + 
				"ldmfd  sp!, {fp, lr}  \n" + 
				"bx lr  \n");*/
		fw.write("sub sp, sp, #4\n");
		fw.write("lmdfd sp!, {fp,lr}\n");
		fw.write("bx lr\n");
			
		}catch (Exception e) {
			System.err.println ("Error during the reading : " + e.getMessage());
		}
	}
	
	
}
