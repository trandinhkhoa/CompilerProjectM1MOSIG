import java.util.List;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class Closure_Element create a closure element for each function definition
 *
 */
public class Closure_Element {

    /**
     * Label of the function definition.
     */
    public String label;

    /**
     * List of free variable found in the function definition
     * */
    public List<Id> free_variables;

    /**
     * List of parameters of the function
     * */
    public List<Id> parameters;

    /**
     * Body of the function
     * */
    public Exp code;

    /**
     * Constructor of the Closure_Element class.
     *
     * It is an empty constructor to create a placeholder object for the main of the program at the beginning of the closure_list (@see Closure.java)
     */
    public Closure_Element() {
        //empty constructor
    }

    /**
     * Constructor of the Closure_Element class.
     *
     * @param e input expression to be converted into a closure element
     */
    public Closure_Element(Exp e) {
        this.label = "_";
        this.free_variables = new LinkedList<Id>();
        this.parameters = new LinkedList<Id>();
        this.code = e;
    }

    /**
     * Constructor of the Closure_Element class for expression with type LetRec.
     *
     * Initialize the label, the list of free variable, the list of parameters and the code.
     *
     * @param e input expression to be converted into a closure element
     */
    public Closure_Element(LetRec lr) {
        this.label = lr.fd.id.id;
        this.free_variables = new LinkedList<Id>();
        this.parameters = new LinkedList<Id>();
        this.parameters.addAll(lr.fd.args);
        this.code = lr.fd.e;
    }

    /**
     * Set the body (code) of the function.
     *
     * @param e input expression to make closure from
     *
     * @return Return nothing
     */
    public void set_Exp(Exp e) {
        this.code = e;
    }

    /**
     * Print the closure element to the console.
     *
     * @return Return nothing
     */
    public void print() {
        if (label != null) {
            System.out.println("Label: " + label);
            System.out.print("Free variables: ");
            if (free_variables.size() == 0) {
                System.out.println("none");
            } else {
                for (int i = 0; i < free_variables.size() - 1; i++) {
                    System.out.print("" + free_variables.get(i).id + ", ");
                }
                System.out.println(free_variables.get(free_variables.size() - 1).id);;
            }
            System.out.print("Parameters: ");
            if (parameters.size() == 0) {
                System.out.println("none");
            } else {
                for (int i = 0; i < parameters.size() - 1; i++) {
                    System.out.print("" + parameters.get(i).id + ", ");
                }
                System.out.println(parameters.get(parameters.size() - 1).id);;
            }
            System.out.println("Code: ");
            code.accept(new PrintASML());
        }
    }


    /**
     * Print the the list of parameters to file .
     *
     * @return Return nothing
     */
    public void printIn(FileWriter fw) {
        try {

            fw.write("let " + label);
            Iterator<Id> it = parameters.iterator();
            while (it.hasNext()) {
                fw.write(" " + it.next());
            }
            fw.write(" = ");


            code.accept(new PrintASMLFile(fw));
        } catch (IOException exception) {
            System.out.println("Error during the writing : " + exception.getMessage());
        }
    }


    /**
     * Print the the ASML code for the function.
     *
     * @return Return nothing
     */
    public void printASML() {
        System.out.print("let " + label);
        Iterator<Id> it = parameters.iterator();
        while (it.hasNext()) {
            System.out.print(" " + it.next());
        }
        System.out.println(" = ");
        code.accept(new PrintASML());
    }

    /**
     * Print the the label of the function in ARM.
     *
     * @param fw FileWriter object
     *
     * @return Return nothing
     */
    public void headerFile(FileWriter fw) {
        try {
            if (this.label.equals("_")) {
                fw.write("_start:\n");
            } else {
                fw.write(this.label + ":\n");
            }
        } catch (IOException exception) {
            System.out.println("Error during the reading : " + exception.getMessage());
        }
    }


    /**
     * Print to console the the prologue of the function in ARM.
     *
     * @return Return nothing
     */
    public void prologue() {
        System.out.println("stmfd sp!, {fp, lr}");
        System.out.println("mov fp, sp");
    }

    /**
     * Print to console the the epilogue of the function in ARM.
     *
     * @return Return nothing
     */
    public void epilogue() {
        System.out.println("ldmfd sp!, {fp,lr}");
        System.out.println("mov sp, fp");
        System.out.println("bx lr");
    }

    /**
     * Print to a file the prologue of the function in ARM.
     *
     * @param fw FileWriter object
     *
     * @return Return nothing
     */
    public void prologueFile(FileWriter fw) {
        try {
            fw.write("stmfd sp!, {fp, lr}\n");
            fw.write("mov fp, sp\n");

        } catch (Exception e) {
            System.err.println("Error during the reading : " + e.getMessage());
        }
    }

    /**
     * Print to a file the the epilogue of the function in ARM.
     *
     * @param fw FileWriter object
     *
     * @return Return nothing
     */
    public void epilogueFile(FileWriter fw) {
        try {
            fw.write("ldmfd sp!, {fp,lr}\n");
            fw.write("mov sp, fp\n");
            fw.write("bx lr\n");

        } catch (Exception e) {
            System.err.println("Error during the reading : " + e.getMessage());
        }
    }


}
