import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Code for ASML code generation
 *
 */
public class ASML_Gen implements ObjVisitor<Exp> {

    /**
     * List of user defined function
     */
    public List<Id> fun_List = new LinkedList<Id>();

    /**
     * Constructor of the ASML_Gen class.
     *
     * @param fun_list List of user defined function
     */
    public ASML_Gen(List<Id> fun_List) {
        this.fun_List = fun_List;
    }

    /**
     * Checks if a function name is contained in fun_List.
     *
     * @param i Function name
     *
     * @return True if the function is contained in fun_List ; False otherwise (meaning the function is a library function).
     */
    public boolean contains(Id i) {
        Iterator<Id> it = fun_List.iterator();
        while (it.hasNext()) {
            if (it.next().id.equals(i.id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * [NotYetImplemented] Calling the visitor for each element of the tuple.
     *
     * @param l List of element of tuple
     *
     * @return True if the function is contained in fun_List ; False otherwise (meaning the function is a library function).
     */
    public List<Exp> rec_list(List<Exp> l) {
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
     * Visitor generating ASML code from the expression of type Unit
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Unit e) {
        return e;
    }

    /**
     * Visitor generating ASML code from the expression of type Bool
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Bool e) {
        return e;
    }

    /**
     * Visitor generating ASML code from the expression of type Int
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Int e) {
        return e;
    }

    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type Float
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Float e) {
        return e;
    }

    /**
     * Visitor generating ASML code from the expression of type Var
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Var e) {
        return e;
    }

    /**
     * Visitor generating ASML code from the expression of type Not
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Not e) {
        return new Not(e.e.accept(this));
    }

    /**
     * Visitor generating ASML code from the expression of type Neg
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Neg e) {
        Var v = new Var(new Id("neg"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e.accept(this));
        return new App(v, l);
    }

    /**
     * Visitor generating ASML code from the expression of type Add
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Add e) {
        Var v = new Var(new Id("add"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e1.accept(this));
        l.add(e.e2.accept(this));
        return new App(v, l);
    }


    /**
     * Visitor generating ASML code from the expression of type Sub
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Sub e) {
        Var v = new Var(new Id("sub"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e1.accept(this));
        l.add(e.e2.accept(this));
        return new App(v, l);
    }

    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type FNeg
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(FNeg e) {
        Var v = new Var(new Id("fneg"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e.accept(this));
        return new App(v, l);
    }


    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type FAdd
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(FAdd e) {
        Var v = new Var(new Id("fadd"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e1.accept(this));
        l.add(e.e2.accept(this));
        return new App(v, l);
    }

    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type FSub
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(FSub e) {
        Var v = new Var(new Id("fsub"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e1.accept(this));
        l.add(e.e2.accept(this));
        return new App(v, l);
    }

    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type FMul
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(FMul e) {
        Var v = new Var(new Id("fmul"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e1.accept(this));
        l.add(e.e2.accept(this));
        return new App(v, l);
    }

    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type FDiv
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(FDiv e) {
        Var v = new Var(new Id("fdiv"));
        List<Exp> l = new LinkedList<Exp>();
        l.add(e.e1.accept(this));
        l.add(e.e2.accept(this));
        return new App(v, l);
    }


    /**
     * Visitor generating ASML code from the expression of type Eq (e1 = e2)
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Eq e) {
        return new Eq(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor generating ASML code from the expression of type LE (e1 &lt;= e2)
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(LE e) {
        return new LE(e.e1.accept(this), e.e2.accept(this));
    }


    /**
     * Visitor generating ASML code from the expression of type If (if e then e1 else e2)
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(If e) {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type Array
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Array e) {
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type Tuple
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Tuple e) {
        return new Tuple(rec_list(e.es));
    }

    /**
     * Visitor generating ASML code from the expression of type Let
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(Let e) {
        return new Let(e.id, e.t, e.e1.accept(this), e.e2.accept(this));
    }


    /**
     * [NotYetImplemented] Visitor generating ASML code from the expression of type LetTuple
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(LetTuple e) {
        return new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
    }


    /**
     * Visitor generating ASML code from the expression of type LetRec
     *
     * Change "apply_direct" to "call"; and change library function (e.g print_int) to ASML library function (e.g call min_caml_print_int)
     *
     * @param e input expression
     *
     * @return ASML code from the input expression.
     */
    public Exp visit(LetRec e) {
        FunDef fd = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        return new LetRec(fd, e.e.accept(this));
    }
    public Exp visit(App e) {
        Var v = (Var) e.e;
        if (v.id.id.equals("apply_direct")) {
            v = new Var(new Id("call"));
            Tuple t = (Tuple)e.es.get(0);
            //a : name of function
            Var a = (Var) t.es.remove(0);
            App app = new App(a, t.es);
            //le :  list of all arguments
            List<Exp> le = new LinkedList<Exp>();
            le.add(app.accept(this));
            le.addAll(e.es.subList(1, e.es.size()));
            return new App(v, le);

        } else if (!contains(v.id)) {

            Var vCall = new Var(new Id("call"));
            Var vNew = new Var(new Id("_min_caml_" + v.id.id));
            App app = new App(vNew, e.es);
            List<Exp> le = new LinkedList<Exp>();
            // Add newle created app as an argument for vCall
            le.add(app);
            return new App(vCall, le);
        }
        return new App(v, rec_list(e.es));
    }
    public Exp visit(Get e) {
        return e;
    }
    public Exp visit(Put e) {
        return e;
    }
}
