import java.util.Iterator;
import java.util.List;

class PrintASML implements Visitor {
    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Unit expression
 	* 
 	 */
    @Override
    public void visit(Unit e) {
        System.out.print("()");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Bool expression
 	* 
 	 */
    @Override
    public void visit(Bool e) {
        System.out.print(e.b);
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Int expression
 	* 
 	 */
    @Override
    public void visit(Int e) {
        System.out.print(e.i);
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Float expression
 	* 
 	 */
    @Override
    public void visit(Float e) {
        String s = String.format("%.2f", e.f);
        System.out.print(s);
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Not expression
 	* 
 	 */
    @Override
    public void visit(Not e) {
        System.out.print("not ");
        e.e.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Neg expression
 	* 
 	 */
    @Override
    public void visit(Neg e) {
        System.out.print("neg ");
        e.e.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Add expression
 	* 
 	 */
    @Override
    public void visit(Add e) {
        System.out.print("");
        e.e1.accept(this);
        System.out.print(" + ");
        e.e2.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Sub expression
 	* 
 	 */
    @Override
    public void visit(Sub e) {
        System.out.print("");
        e.e1.accept(this);
        System.out.print(" - ");
        e.e2.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input FNeg expression
 	* 
 	 */
    @Override
    public void visit(FNeg e){
        System.out.print("fneg ");
        e.e.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input FAdd expression
 	* 
 	 */
    @Override
    public void visit(FAdd e){
        System.out.print("");
        e.e1.accept(this);
        System.out.print(" +. ");
        e.e2.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input FSub expression
 	* 
 	 */
    @Override
    public void visit(FSub e){
        System.out.print("");
        e.e1.accept(this);
        System.out.print(" -. ");
        e.e2.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input FMul expression
 	* 
 	 */
    @Override
    public void visit(FMul e) {
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" *. ");
        e.e2.accept(this);
        System.out.print(")");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input FDiv expression
 	* 
 	 */
    @Override
    public void visit(FDiv e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" /. ");
        e.e2.accept(this);
        System.out.print(")");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Eq expression
 	* 
 	 */
    @Override
    public void visit(Eq e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" = ");
        e.e2.accept(this);
        System.out.print(")");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input LE expression
 	* 
 	 */
    @Override
    public void visit(LE e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(" <= ");
        e.e2.accept(this);
        System.out.print(")");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input If expression
 	* 
 	 */
    @Override
    public void visit(If e){
        System.out.print("if ");
        e.e1.accept(this);
        System.out.print(" then (");
        System.out.println("");
        e.e2.accept(this);
        System.out.print("\n) else (");
        System.out.println("");
        e.e3.accept(this);
        System.out.println("\n)");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Let expression
 	* 
 	 */
    @Override
    public void visit(Let e) {
        System.out.print("let ");
        System.out.print(e.id);
        System.out.print(" = ");
        e.e1.accept(this);
        System.out.print(" in ");
        System.out.println("");
        e.e2.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Var expression
 	* 
 	 */
    @Override
    public void visit(Var e){
        System.out.print(e.id);
    }


    // print sequence of identifiers 
    static <E> void printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<E> it = l.iterator();
        System.out.print(it.next());
        while (it.hasNext()) {
            System.out.print(op + it.next());
        }
    }

    // print sequence of Exp
    void printInfix2(List<Exp> l, String op) {
        if (l.isEmpty()) {
            return;
        }
        Iterator<Exp> it = l.iterator();
        it.next().accept(this);
        while (it.hasNext()) {
            System.out.print(op);
            it.next().accept(this);
        }
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input LetRec expression
 	* 
 	 */
    @Override
    public void visit(LetRec e){
        System.out.print("let rec " + e.fd.id + " ");
        printInfix(e.fd.args, " ");
        System.out.print(" = ");
        e.fd.e.accept(this);
        System.out.print(" in ");
        System.out.println("");
        e.e.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input App expression
 	* 
 	 */
    @Override
    public void visit(App e){
        System.out.print("");
        e.e.accept(this);
        System.out.print(" ");
        printInfix2(e.es, " ");
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Tuple expression
 	* 
 	 */
    @Override
    public void visit(Tuple e){
        System.out.print("(");
        printInfix2(e.es, ", ");
        System.out.print(")");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input LetTuple expression
 	* 
 	 */
    @Override
    public void visit(LetTuple e){
        System.out.print("let (");
        printInfix(e.ids, ", ");
        System.out.print(") = ");
        e.e1.accept(this);
        System.out.print(" in ");
        System.out.println("");
        e.e2.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Array expression
 	* 
 	 */
    @Override
    public void visit(Array e){
        System.out.print("Array.create ");
        e.e1.accept(this);
        System.out.print(" ");
        e.e2.accept(this);
        System.out.print("");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Get expression
 	* 
 	 */
    @Override
    public void visit(Get e){
        e.e1.accept(this);
        System.out.print(".(");
        e.e2.accept(this);
        System.out.print(")");
    }

    /** 
	 * Visitor printing the given expression.
 	 * 
 	 * @param e	input Put expression
 	* 
 	 */
    @Override
    public void visit(Put e){
        System.out.print("(");
        e.e1.accept(this);
        System.out.print(".(");
        e.e2.accept(this);
        System.out.print(") <- ");
        e.e3.accept(this);
        System.out.print(")");
    }
}


