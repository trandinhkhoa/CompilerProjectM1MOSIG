import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

class PrintASMLFile implements Visitor {
	
	FileWriter fw ;
	
	public PrintASMLFile(FileWriter f) {
		fw=f;
	}
	
    public void visit(Unit e) {
    	try {
        fw.write("");
    }
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Bool e) {
    	try {
        fw.write(""+e.b);
    }
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Int e) {
    	try {
        fw.write(String.valueOf(e.i));
    }
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Float e) {
    	try {
        String s = String.format("%.2f", e.f);
        fw.write(s);
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(Not e) {
    	try {
        fw.write("not ");
        e.e.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(Neg e) {
    	try {
        fw.write("neg ");
        e.e.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(Add e) {
    	try {
        fw.write("");
        e.e1.accept(this);
        fw.write(" + ");
        e.e2.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(Sub e) {
    	try {
        fw.write("");
        e.e1.accept(this);
        fw.write(" - ");
        e.e2.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(FNeg e){
    	try {
        fw.write("fneg ");
        e.e.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(FAdd e){
    	try {
        fw.write("");
        e.e1.accept(this);
        fw.write(" +. ");
        e.e2.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(FSub e){
    	try {
        fw.write("");
        e.e1.accept(this);
        fw.write(" -. ");
        e.e2.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(FMul e) {
    	try {
        fw.write("(");
        e.e1.accept(this);
        fw.write(" *. ");
        e.e2.accept(this);
        fw.write(")");}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(FDiv e){try {
        fw.write("(");
        e.e1.accept(this);
        fw.write(" /. ");
        e.e2.accept(this);
        fw.write(")");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Eq e){try {
        fw.write("(");
        e.e1.accept(this);
        fw.write(" = ");
        e.e2.accept(this);
        fw.write(")");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(LE e){try {
        fw.write("(");
        e.e1.accept(this);
        fw.write(" <= ");
        e.e2.accept(this);
        fw.write(")");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(If e){
    	try {
        fw.write("if ");
        e.e1.accept(this);
        fw.write(" then (");
        fw.write('\n');
        e.e2.accept(this);
        fw.write(") else (");
        fw.write('\n');
        e.e3.accept(this);
        fw.write(")");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Let e) {try {
        fw.write("let ");
        fw.write(e.id.id);
        fw.write(" = ");
        e.e1.accept(this);
        fw.write(" in ");
        fw.write('\n');
        e.e2.accept(this);
        fw.write("");
    }
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Var e){try {
        fw.write(e.id.id);}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }


    // print sequence of identifiers 
    public <E> void printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return;
        }try{
        Iterator<E> it = l.iterator();
        fw.write(""+it.next());
        while (it.hasNext()) {
            fw.write(op + it.next());
        }}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    // print sequence of Exp
    void printInfix2(List<Exp> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<Exp> it = l.iterator();
        it.next().accept(this);
        try {
        while (it.hasNext()) {
            fw.write(op);
            it.next().accept(this);
        }}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(LetRec e){
    	try {
        fw.write("let rec " + e.fd.id + " ");
        printInfix(e.fd.args, " ");
        fw.write(" = ");
        e.fd.e.accept(this);
        fw.write(" in ");
        fw.write('\n');
        e.e.accept(this);
        fw.write("");
    	}
		catch (IOException exception)
		{
			System.out.println ("Error during the reading : " + exception.getMessage());
		}
    }

    public void visit(App e){try {
        fw.write("");
        e.e.accept(this);
        fw.write(" ");
        printInfix2(e.es, " ");
        fw.write("");
    }
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Tuple e){try {
        fw.write("(");
        printInfix2(e.es, ", ");
        fw.write(")");
    }
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(LetTuple e){try {
        fw.write("let (");
        printInfix(e.ids, ", ");
        fw.write(") = ");
        e.e1.accept(this);
        fw.write(" in ");
        fw.write('\n');
        e.e2.accept(this);
        fw.write("");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Array e){try {
        fw.write("Array.create ");
        e.e1.accept(this);
        fw.write(" ");
        e.e2.accept(this);
        fw.write("");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Get e){try {
        e.e1.accept(this);
        fw.write(".(");
        e.e2.accept(this);
        fw.write(")");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }

    public void visit(Put e){try {
        fw.write("(");
        e.e1.accept(this);
        fw.write(".(");
        e.e2.accept(this);
        fw.write(") <- ");
        e.e3.accept(this);
        fw.write(")");}
	catch (IOException exception)
	{
		System.out.println ("Error during the reading : " + exception.getMessage());
	}
    }
}


