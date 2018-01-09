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
	
	
}
