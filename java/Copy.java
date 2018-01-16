import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class Copy create a copy of the Abstract Syntax Tree (AST).
 *
 */

public class Copy implements ObjVisitor<Exp> {

    /**
     * Visitor for expression with type Unit.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Unit e) {
        return new Unit();
    }

    /**
     * Visitor for expression with type Bool.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Bool e) {
        return new Bool(e.b);
    }

    /**
     * Visitor for expression with type Int.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Int e) {
        return new Int(e.i);
    }

    /**
     * Visitor for expression with type Float.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Float e) {
        return new Float(e.f);
    }

    /**
     * Visitor for expression with type Not.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Not e) {
        return new Not(e.e.accept(this));
    }

    /**
     * Visitor for expression with type Neg.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Neg e) {
        return new Neg(e.e.accept(this));
    }

    /**
     * Visitor for expression with type Add.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Add e) {
        return new Add(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Sub.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Sub e) {
        return new Sub(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type FNeg.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(FNeg e) {
        return new FNeg(e.e.accept(this));
    }

    /**
     * Visitor for expression with type FAdd.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(FAdd e) {
        return new FAdd(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type FSub.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(FSub e) {
        return new FSub(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type FMul.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(FMul e) {
        return new FMul(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type FDiv.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(FDiv e) {
        return new FDiv(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Eq (e1 = e2).
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Eq e) {
        return new Eq(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Eq (e1 &lt;= e2)
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(LE e) {
        return new LE(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Eq (if exp then e1 else e2).
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(If e) {
        return new If(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));

    }

    /**
     * Visitor for expression with type Eq (let x = e1 in e2).
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Let e) {
        return new Let(new Id(e.id.id), e.t, e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Var.
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Var e) {
        return new Var(new Id(e.id.id));
    }


    // print sequence of identifiers
    static <E> Exp printInfix(List<E> l, String op) {
        if (l.isEmpty()) {
            return new Unit();
        }
        Iterator<E> it = l.iterator();
        it.next();
        while (it.hasNext()) {
            it.next();
        }
        return new Unit();
    }

    // print sequence of identifiers
    List<Id> printId(List<Id> l) {
        List<Id> new_list = new LinkedList<Id>();
        if (l.isEmpty()) {
            return new_list;
        }
        Iterator<Id> it = l.iterator();
        while (it.hasNext()) {
            new_list.add(new Id(it.next().id));
        }
        return new_list;
    }


    // print sequence of Exp
    List<Exp> printInfix2(List<Exp> l) {
        List<Exp> new_list = new LinkedList<Exp>();
        new_list.clear();
        if (!l.isEmpty()) {
            Iterator<Exp> it = l.iterator();
            while (it.hasNext()) {
                new_list.add(it.next().accept(this));
            }
        }
        return new_list;
    }

    /**
     * Visitor for expression with type LetRec
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(LetRec e) {
        FunDef fd = new FunDef(new Id(e.fd.id.id), e.fd.type, printId(e.fd.args), e.fd.e.accept(this));
        return new LetRec(fd, e.e.accept(this));
    }

    /**
     * Visitor for expression with type rec_app
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp rec_app(Exp e, Exp app) {
        Let l = (Let)e;
        if (l.id.id.equals("\0")) {
            System.out.println(l.id.id);
            return app;
        } else {
            return (Exp) new Let(l.id, l.t, l.e1, rec_app(l.e2, app));
        }
    }

    /**
     * Visitor for expression with type App
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(App e) {
        return new App(e.e.accept(this), printInfix2(e.es));
    }

    /**
     * Visitor for expression with type Tuple
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Tuple e) {
        return new Tuple(printInfix2(e.es));
    }

    /**
     * Visitor for expression with type LetTuple
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(LetTuple e) {
        return new LetTuple(printId(e.ids), e.ts, e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Array
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Array e) {
        return new Array(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Get
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Get e) {
        return new Get(e.e1.accept(this), e.e2.accept(this));
    }

    /**
     * Visitor for expression with type Put
     *
     * @param e input expression
     *
     * @return The input expression.
     */
    public Exp visit(Put e) {
        return new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
    }
}
