import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Visitor modifying the associated expression based on the definition of the reduction of nested let.
 *
 */
public class Nested_Let implements ObjVisitor<Exp> {


    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Unit expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Let e) {
        if (e.e1.getClass() == e.getClass()) {
            Id y = e.id;
            Let let1 = (Let)e.e1.accept(this);
            Let let2 = new Let(e.id, e.t, let1.e2, e.e2);
            Let newLet = new Let(let1.id, let1.t, let1.e1, let2);
            Let newLet2 = (Let)newLet.accept(this);                //Travel the tree again to check if there is nested Let left
            return newLet2;
        } else {
            Let newLet = new Let(e.id, e.t, e.e1, e.e2.accept(this));
            Let newLet2 = new Let(newLet.id, newLet.t, newLet.e1, newLet.e2);
            return newLet2;
        }
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Unit expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(LetTuple e) {
        return e;

    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Unit expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(LetRec e) {
        /* No modification for Let recursive */
        Exp e22 = e.e.accept(this);
        FunDef fd2 = new FunDef(e.fd.id, e.fd.type, e.fd.args, e.fd.e.accept(this));
        LetRec lr = new LetRec(fd2, e22);

        return lr;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Unit expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(App e) {
        App app;
        List<Exp> argList = printInfix2(e.es);
        app = new App(e.e.accept(this), argList);
        return app;
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
     * Visitor returning the new expression after reduction of nested let.
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
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Bool expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Bool e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
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
     * Visitor returning the new expression after reduction of nested let.
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
     * Visitor returning the new expression after reduction of nested let.
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
     * Visitor returning the new expression after reduction of nested let.
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
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Add expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Add e) {
        return new Add(e.e1.accept(this),e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Sub expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Sub e) {
        return new Sub(e.e1.accept(this),e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input FNeg expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FNeg e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input FAdd expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FAdd e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input FSub expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FSub e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input FMul expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FMul e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input FDiv expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(FDiv e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Eq expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Eq e) {
        return new Eq(e.e1.accept(this),e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input LE expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(LE e) {
        return new LE(e.e1.accept(this),e.e2.accept(this));
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input If expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(If e) {
        return new If(e.e1.accept(this),e.e2.accept(this),e.e3.accept(this));
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Var expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Var e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Tuple expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Tuple e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Array expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Array e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Get expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Get e) {
        return e;
    }

    /**
     * Visitor returning the new expression after reduction of nested let.
     *
     * @param e input Put expression
     *
     * @return the new expression.
     */
    @Override
    public Exp visit(Put e) {
        return e;
    }
}
