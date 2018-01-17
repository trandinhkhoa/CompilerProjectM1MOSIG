import java.util.*;

/**
 * alpha-Conversion assign different names to different variable. The purpose is to simplify the next steps.
 * For example, to apply alpha-conversion for Let expression (let x = e1 in e2), we first convert e1 in current environment, then create a new mapping from x to x', add the mapping to the environment, and apply alpha conversion for e2.
 * To apply alpha-conversion for LetRec expression (let rec f x1 ... xm = M in N), we first update the environment with the mapping from old new name for function name f to the environment. Then convert N with the updated environment. Then update the environment with mappings from old arguments' name to new ones. And convert N with the updated environment
 *
 */

public class Alpha_conversion implements ObjVisitor<Exp> {

    /**
    * Stack of environments; environments are updated and pushed onto the stack when going down the tree where declarations happen, then popped when going up from where declarations happen.
     */
    public Stack hashmapStack;

    /**
     * Index of renamed variable.
     */
    public static int x = -1;

    static int level = 0;

    /**
     * Constructor of the alpha_conversion class.
     *
     * Initialize index x of renamed variable with parameter x (continue incrementing from variable x of @see K_Norm.java ). Initialize the stack of environments.
     *
     * @param x index to generate new variable name
     */
    public Alpha_conversion(int x) {
        this.x = x;
        hashmapStack = new Stack();
    }

    /**
     * Generate new variable name.
     *
     * @return string containing new variable name.
     */
    public static String gen() {
        x++;
        return "temp" + x;
    }

    //display the current mapping from old to new names for debugging purpose
    void displayHashMap(HashMap<String, List> hashmap) {
        Set set = hashmap.entrySet();
        Iterator i = set.iterator();
        System.out.println("Display Hashmap:");
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }
    }

    /**
     * Visitor for expression with type Unit.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Unit e) {
        return e;
    }

    /**
     * Visitor for expression with type Bool.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Bool e) {
        return e;
    }

    /**
     * Visitor for expression with type Int.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Int e) {
        return e;
    }

    /**
     * Visitor for expression with type Float.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Float e) {
        return e;
    }

    /**
     * Visitor for expression with type Not.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Not e) {
        return e.e.accept(this);
    }

    /**
     * Visitor for expression with type Neg.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Neg e) {
        return e;
    }

    /**
     * Visitor for expression with type Add.
     *
     * To apply alpha-conversion for Add expression, apply alpha conversion for both operands
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Add e) {
        Add a = new Add(e.e1.accept(this), e.e2.accept(this));
        return a;
    }

    /**
     * Visitor for expression with type Sub.
     *
     * To apply alpha-conversion for Sub expression, apply alpha conversion for both operands
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Sub e) {
        Sub a = new Sub(e.e1.accept(this), e.e2.accept(this));
        return a;
    }

    /**
     * Visitor for expression with type FNeg.
     *
     * [NotYetImplemented] for Float extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(FNeg e) {
        Var v1 = new Var(new Id(gen()));
        Let l = new Let(v1.id, new TFloat(), e.e.accept(this), new FNeg(v1));
        return l;
    }

    /**
     * Visitor for expression with type FAdd.
     *
     * [NotYetImplemented] for Float extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(FAdd e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FAdd(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2, l2);
        return l1;
    }

    /**
     * Visitor for expression with type FSub.
     *
     * [NotYetImplemented] for Float extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(FSub e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FSub(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
        return l1;
    }

    /**
     * Visitor for expression with type FMul.
     *
     * [NotYetImplemented] for Float extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(FMul e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FMul(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
        return l1;
    }

    /**
     * Visitor for expression with type FDiv.
     *
     * [NotYetImplemented] for Float extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(FDiv e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FDiv(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
        return l1;
    }

    /**
     * Visitor for expression with type Eq (Equal).
     *
     * To apply alpha-conversion for Eq expression, apply alpha conversion for both operands
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Eq e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Eq(v1, v2));
        Let l1 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
        return l1;
    }

    /**
     * Visitor for expression with type LE (e1 &lt;= e2).
     *
     * To apply alpha-conversion for LE expression, apply alpha conversion for both operands
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(LE e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new LE(v1, v2));
        Let l1 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
        return l1;
    }

    /**
     * Visitor for expression with type If.
     *
     * To apply alpha-conversion for If expression (if exp then M1 else M2), apply alpha conversion for the the expression between If and Then, and for M1 and M2.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(If e) {
        Let l1 = (Let) e.e1.accept(this);
        Let l2 = (Let) l1.e2;
        Exp eq = l2.e2;
        If si = new If(eq, e.e2.accept(this), e.e3.accept(this));
        Let lt2 = new Let(l2.id, l2.t, l2.e1, si);
        Let lt1 = new Let(l1.id, l1.t, l1.e1, lt2);
        return lt1;

    }

    /**
     * Visitor for expression with type Let.
     *
     * To apply alpha-conversion for Let expression (let x = e1 in e2), we first convert e1 in current environment, then create a new mapping from x to x', add the mapping to the environment, and apply alpha conversion for e2.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Let e) {
        //this only to provide something to return after try catch
        Let returnL = new Let(e.id, e.t, e.e1, e.e2);
        try {
            // return the hashmap at the top of the stack
            HashMap<String, List> peekObject = new HashMap<String, List>();
            // g env e1
            Exp e11 = e.e1.accept(this);
            //if hashmap is empty -> peekObject is empty -> go to else
            if (!hashmapStack.empty()) {
                peekObject = (HashMap)hashmapStack.peek();
            }
            // if x in let x = e1 in e2 already existed, map to a new name
            // creating a new hashmap same as the top one of stack
            HashMap<String, List> newHashmap = new HashMap<String, List>();
            newHashmap = (HashMap)peekObject.clone();
            if (peekObject.containsKey(e.id.id)) {
                // creating a new name
                Var v = new Var(new Id(gen()));

                // add new name mapping to the new hashmap
                newHashmap.get(e.id.id).add(v.id);

                //push new hashmap to top of stack
                hashmapStack.push(newHashmap);

                //g (M.add x x' env) e2
                Let l = new Let(v.id, e.t, e11, e.e2.accept(this));
                // POP when finish the traveling the whole let tree. hashmap env' -> env
                hashmapStack.pop();
                return l;
                // if x in let x = e1 in e2 does not exist, add the mapping current name -> current name to hashmap -> push hashmap to stack
            } else {
                // add new name mapping to the new hashmap
                List<Id> newNameList = new ArrayList<Id>();
                newNameList.add(e.id);
                newHashmap.put(e.id.id, newNameList);

                //push new hashmap to top of stack
                hashmapStack.push(newHashmap);

                //g (M.add x x' env) e2
                Let l = new Let(e.id, e.t, e11, e.e2.accept(this));
                // POP when finish the traveling the whole let tree. hashmap env' -> env
                hashmapStack.pop();
                return l;
            }
        } catch (Exception error) {

            error.printStackTrace();

        }
        return returnL;
    }

    /**
     * Visitor for expression with type LetRec.
     *
     * To apply alpha-conversion for LetRec expression (let rec f x1 ... xm = M in N), we first update the environment with the mapping from old new name for function name f to the environment. Then convert N with the updated environment. Then update the environment with mappings from old arguments' name to new ones. And convert N with the updated environment
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(LetRec e) {
        //New function name
        String newFunctionName;
        // return the hashmap at the top of the stack
        HashMap<String, List> peekObject = new HashMap<String, List>();

        // creating a new hashmap same as the top one of stack
        HashMap<String, List> newHashmap = new HashMap<String, List>();
        if (!hashmapStack.empty()) {
            peekObject = (HashMap)hashmapStack.peek();
        }

        //if hashmap is empty -> peekObject is empty -> go to else
        newHashmap = (HashMap)peekObject.clone();

        //let env = M.add x (Id.genid x) env in
        // if x in let x = e1 in e2 already existed, map to a new name
        if (peekObject.containsKey(e.fd.id.id)) {
            // creating a new name
            Var v = new Var(new Id(gen()));

            //Replace old function name with new name = find x env
            newFunctionName = v.id.id;
            // add new name mapping to the new hashmap
            newHashmap.get(e.fd.id.id).add(v.id);

            //push new hashmap to top of stack
            hashmapStack.push(newHashmap);
            // if x in let x = e1 in e2 does not exist, add the mapping current name -> current name to hashmap -> push hashmap to stack
        } else {

            // add new name mapping to the new hashmap
            List<Id> newNameList = new ArrayList<Id>();
            newNameList.add(e.fd.id);
            newHashmap.put(e.fd.id.id, newNameList);

            //Replace old function name with new name = find x env
            newFunctionName = e.fd.id.id;

            //push new hashmap to top of stack
            hashmapStack.push(newHashmap);
        }

        // g env e2
        Exp e22 = e.e.accept(this);

        //map arg list to new names
        List<Id> newArgsList = new ArrayList<Id>();
        Iterator i = e.fd.args.iterator();
        while (i.hasNext()) {
            String str = i.next().toString();
            if (newHashmap.containsKey(str)) {//e.fd.id is not string
                // creating a new name
                Var v = new Var(new Id(gen()));

                //Replace old function arg list with new list
                newArgsList.add(v.id);

                // add new name mapping to the new hashmap
                newHashmap.get(str).add(v.id);
            } else {
                // add new name mapping to the new hashmap
                List<Id> newNameList = new ArrayList<Id>();
                newNameList.add(new Id(str));
                newHashmap.put(str, newNameList);

                //Replace old function arg list with new list
                newArgsList.add(new Id(str));
            }
        }

        //create new fundef
        FunDef fd2 = new FunDef(new Id(newFunctionName), e.fd.type, newArgsList, e.fd.e.accept(this));
        //create new letrec
        LetRec lr = new LetRec(fd2, e22);
        hashmapStack.pop();

        return lr;
    }

    /**
     * Visitor for expression with type Var.
     *
     * Give the variable new name and add the mapping from old to new name to the environment.
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Var e) {
        // System.out.println("Current exp is " + e.toString());
        HashMap<String, List> newHashmap = new HashMap<String, List>();
        if (!hashmapStack.empty()) {
            newHashmap = (HashMap)hashmapStack.peek();
        }
        if (newHashmap.containsKey(e.id.id)) {
            Var newE = new Var((Id)newHashmap.get(e.id.id).get(newHashmap.get(e.id.id).size() - 1));
            return newE;
        } else {
            List<Id> newNameList = new ArrayList<Id>();
            newNameList.add(e.id);
            newHashmap.put(e.id.id, newNameList);

            return e;
        }
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

    // public Exp rec_app(Exp e, Exp app) {
    //     Let l = (Let)e;
    //     if (l.id.id.equals("\0")) {
    //         System.out.println(l.id.id);
    //         return app;
    //     } else {
    //         return (Exp) new Let(l.id, l.t, l.e1, rec_app(l.e2, app));
    //     }
    // }

    /**
     * Visitor for expression with type App.
     *
     * @param e input expression to be convert
     *
     * To apply alpha-conversion to App, apply alpha conversion to the app's name and its list of argument
     *
     *  @return The alpha-converted expression.
     */
    public Exp visit(App e) {
        App app = new App(e.e.accept(this), printInfix2(e.es));
        return app;
    }

    /**
     * Visitor for expression with type Tuple.
     *
     * [NotYetImplemented] for Tuple extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Tuple e) {
        Tuple t = new Tuple(printInfix2(e.es));
        return t;
    }

    /**
     * Visitor for expression with type LetTuple.
     *
     * [NotYetImplemented] for Tuple extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(LetTuple e) {
        LetTuple lt = new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
        return lt;
    }

    /**
     * Visitor for expression with type Array.
     *
     * [NotYetImplemented] for Array extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Array e) {
        Array a = new Array(e.e1.accept(this), e.e2.accept(this));
        return a;
    }

    /**
     * Visitor for expression with type Get.
     *
     * [NotYetImplemented] for Array extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Get e) {
        Get g = new Get(e.e1.accept(this), e.e2.accept(this));
        return g;
    }

    /**
     * Visitor for expression with type Put.
     *
     * [NotYetImplemented] for Array extension
     *
     * @param e input expression to be convert
     *
     * @return The alpha-converted expression.
     */
    public Exp visit(Put e) {
        Put p = new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
        return p;
    }
}
