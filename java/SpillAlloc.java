import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Visitor modifying the associated expression based on the register allocation spilling everything.
 * what we are doing here is that we are push every value and variable into the stack and we are
 * loading these values into some register when we want to use them.
 *
 */
public class SpillAlloc implements ObjVisitor<Exp> {

    static HashMap<  String , Integer > register_Map = new HashMap<>();
    int index;

    /**
    * Empty constructor for SpillAlloc.
    *
    */
    public SpillAlloc() {
        register_Map.clear();
        index = 1;
    }

    int get_free_register() {
        return index++;
    }

    int get_var_register(String s) {
        Integer i = register_Map.get(s);
        if (i != null) {
            return i;
        } else {
            return -1;
        }
    }


    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Unit expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Unit e) {
        return e;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Unit expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Bool e) {
        return e;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Int expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Int e) {
        return e;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Float expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Float e) {
        return e;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Not expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Not e) {
        return new Not(e.e.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Neg expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Neg e) {
        return new Neg(e.e.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Add expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Add e) {
        return new Add(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Sub expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Sub e) {
        return new Sub(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input FNeg expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FNeg e) {
        return new FNeg(e.e.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input FAdd expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FAdd e) {
        return new FAdd(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input FSub expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FSub e) {
        return new FSub(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input FMul expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FMul e) {
        return new FMul(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input FDiv expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FDiv e) {
        return new FDiv(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Eq expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Eq e) {
        return new Eq(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input LE expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(LE e) {
        return new LE(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input If expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(If e) {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));

    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Let expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Let e) {
        int i = get_free_register();
        if (i != -1) {
            register_Map.put(e.id.id, i);
            Let l = new Let(new Id("s" + i), e.t, e.e1.accept(this), e.e2.accept(this));
            return l;
        } else {
            System.err.println("No remaining register");
            Let l = new Let(e.id, e.t, e.e1.accept(this), e.e2.accept(this));
            return l;
        }
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Var expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Var e) {
        int i = get_var_register(e.id.id);
        if (i != -1) {
            return new Var(new Id("s" + i));
        } else {
            return e;
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
            new_list.add(it.next().accept(this));
        }
        return new_list;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input LetRec expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(LetRec e) {

        FunDef fd2 = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        LetRec lr = new LetRec(fd2, e.e.accept(this));
        return lr;
    }



    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input App expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(App e) {
        return new App(e.e, printInfix2(e.es));

    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Tuple expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Tuple e) {
        Tuple t = new Tuple(printInfix2(e.es));
        return t;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input LetTuple expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(LetTuple e) {
        LetTuple lt = new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
        return lt;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Array expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Array e) {
        Array a = new Array(e.e1.accept(this), e.e2.accept(this));
        return a;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Get expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Get e) {
        Get g = new Get(e.e1.accept(this), e.e2.accept(this));
        return g;
    }

    /**
     * Visitor returning the new expression after register allocation.
     *
     * @param e input Put expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Put e) {
        Put p = new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
        return p;
    }
}

