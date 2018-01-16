import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Closure Conversion separate the function definition from the rest of the code.
 *
 * It: flatten the nest let rec; for each function, assign a label that will be used later on in the generated assembly; identify the parameters of the function; replace the call "f x1 ... xn" with a new instruction "apply_direct(l, (x1 ... xn))" where l is the label for f. After closure, the field closure_list contain all defined function in the program
 *
 *
 */

public class Closure implements ObjVisitor<Exp> {

    List<Closure_Element> closure_list;
    Stack<Id> s = new Stack<Id>();
    List<Id> fun_List = new LinkedList<Id>();
    Hashtable<String, List<Id>> ht = new Hashtable<String, List<Id>>();
    boolean main_done = false;
    boolean continueFlag = true;
    //library function (predefined) list
    List<String> libraryFuncList = new ArrayList<String>();

    /**
     * Constructor of the alpha_conversion class.
     *
     * Initilize the closure_list, containing functions in the program, and define what functions are library functions in libraryFuncList
     */
    public Closure() {
        closure_list = new LinkedList<Closure_Element>();
        closure_list.add(new Closure_Element());
        //initialize pre-defined function list
        libraryFuncList.add("print_int");
        libraryFuncList.add("print_char");
        libraryFuncList.add("print_newline");
    }

    /**
     * Visitor for expression with type Unit.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Unit e) {
        if ((s.isEmpty()) && (!main_done)) {
            main_done = true;
            closure_list.add(new Closure_Element(e));
        }
        return e;
    }

    /**
     * Visitor for expression with type Bool.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Bool e) {
        return e;
    }

    /**
     * Visitor for expression with type Int.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Int e) {
        return e;
    }

    /**
     * [NotYetImplemented] Visitor for expression with type Float.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Float e) {
        return e;
    }

    /**
     * Visitor for expression with type Not.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Not e) {
        return new Not(e.e.accept(this));
    }

    /**
     * Visitor for expression with type Neg.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Neg e) {
        return new Neg(e.e.accept(this));
    }

    /**
     * Visitor for expression with type Add.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Add e) {
        return new Add(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Sub.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Sub e) {
        return new Sub(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type FNeg.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(FNeg e) {
        return new FNeg(e.e.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type FAdd.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(FAdd e) {
        return new FAdd(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type FSub.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(FSub e) {
        return new FSub(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type FMul.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(FMul e) {
        return new FMul(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type FDiv.
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(FDiv e) {
        return new FDiv(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Eq (e1 = e2).
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Eq e) {
        return new Eq(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Eq (e1 &lt;= e2)
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(LE e) {
        return new LE(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Eq (if exp then e1 else e2)
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(If e) {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));

    }

    /**
     * Visitor for expression with type Let (let x = e1 in e2)
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Let e) {
        Exp newE1 = e.e1;
        Exp newE2 = e.e2;
        if ((s.isEmpty()) && (!main_done)) {
            main_done = true;
            newE1 = e.e1.accept(this);
            newE2 = e.e2.accept(this);
            closure_list.set(0, new Closure_Element(new Let(e.id, e.t, newE1, newE2)));
            continueFlag = false;
            main_done = false;
        }
        if (!s.isEmpty()) {
            Id i = s.peek();
            List<Id> li = ht.get(i.id);
            li.add(e.id);
            ht.put(i.id, li);
        }
        if ((main_done) || (continueFlag)) {
            newE1 = e.e1.accept(this);
            newE2 = e.e2.accept(this);
        }
        return new Let(e.id, e.t, newE1, newE2);
    }

    /**
     * Visitor for expression with type Var
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Var e) {
        return e;
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
            new_list.add(it.next().accept(this));
        }
        return new_list;
    }

    /**
     * Visitor for expression with type LetRec
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(LetRec e) {
        List<Id> fv = new LinkedList<Id>();
        if (!s.isEmpty()) {
            Id i = s.peek();
            fv = ht.get(i.id);
        }

        s.push(e.fd.id);
        ht.remove(e.fd.id.id);
        ht.put(e.fd.id.id, new LinkedList<Id>());
        ht.get(e.fd.id.id).addAll(e.fd.args);
        Id id = new Id("_" + e.fd.id.id);
        fun_List.add(id);
        FunDef fd2 = new FunDef(id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        Id i = s.pop();
        LetRec lr = new LetRec(fd2, e.e.accept(this));
        Closure_Element ce = new Closure_Element(lr);
        ce.free_variables = fv;
        closure_list.add(ce);

        return lr.e;
    }

    /**
     * Visitor for expression with type App
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(App e) {
        boolean here = false;
        if ((s.isEmpty()) && (!main_done)) {
            main_done = true;
            here = true;
        }
        Var name = (Var) e.e;
        Var v = new Var(new Id("_" + name.id.id));
        List<Exp> lis = new LinkedList<Exp>();
        lis.add(v);
        lis.addAll(printInfix2(e.es));
        Tuple t = new Tuple(lis);
        List<Exp> l = new LinkedList<Exp>();
        l.add(t);
        if (libraryFuncList.contains(((Var)e.e).id.id)) {
            return e;
        } else {
            App a = new App(new Var(new Id("apply_direct")), l);
            if (here) {
                closure_list.add(new Closure_Element(a));
            }
            return a;
        }
    }

    /**
     * [NotYetImplemented] Visitor for expression with type Tuple
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Tuple e) {
        return new Tuple(printInfix2(e.es));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type LetTuple
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(LetTuple e) {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type Array
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Array e) {
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type Get
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Get e) {
        return new Get(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor for expression with type Put
     *
     * @param e input expression to be apply closure
     *
     * @return The expression after closure is applied.
     */
    public Exp visit(Put e) {
        return new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }
}
