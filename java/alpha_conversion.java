import java.util.*;


class alpha_conversion implements ObjVisitor<Exp> {

    // HashMap<String, List> hashmap = new HashMap<String, List>();
    Stack hashmapStack;
    // HashMap<String,String> hm = new HashMap<String,String>();

    static int x = -1;
    static int level = 0;
    static String gen() {
        x++;
        return "temp" + x;
    }

    public alpha_conversion(int x) {
        this.x = x;
        hashmapStack = new Stack();
    }

    public void displayHashMap(HashMap<String, List> hashmap) {
        Set set = hashmap.entrySet();
        Iterator i = set.iterator();
        System.out.println("Display Hashmap:");
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());
        }
    }

    public Exp visit(Unit e) {
        // System.out.println("Current exp is " + e.toString());
        return e;
    }

    public Exp visit(Bool e) {
        // System.out.println("Current exp is " + e.toString());
        return e;
    }

    public Exp visit(Int e) {
        // System.out.println("Current exp is " + e.toString());
        return e;
    }

    public Exp visit(Float e) {
        // System.out.println("Current exp is " + e.toString());
        return e;
    }

    public Exp visit(Not e) {
        // System.out.println("Current exp is " + e.toString());
        return e.e.accept(this);
    }

    public Exp visit(Neg e) {
        // System.out.println("Current exp is " + e.toString());
        return e;
    }

    public Exp visit(Add e) {
        Add a = new Add(e.e1.accept(this), e.e2.accept(this));
        return a;
    }

    public Exp visit(Sub e) {
        Sub a = new Sub(e.e1.accept(this), e.e2.accept(this));
        return a;
    }

    public Exp visit(FNeg e) {
        Var v1 = new Var(new Id(gen()));
        Let l = new Let(v1.id, new TFloat(), e.e.accept(this), new FNeg(v1));
        return l;
    }

    public Exp visit(FAdd e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FAdd(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2, l2);
        return l1;
    }

    public Exp visit(FSub e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FSub(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
        return l1;
    }

    public Exp visit(FMul e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FMul(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
        return l1;
    }

    public Exp visit(FDiv e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TFloat(), e.e1.accept(this), new FDiv(v1, v2));
        Let l1 = new Let(v2.id, new TFloat(), e.e2.accept(this), l2);
        return l1;
    }

    public Exp visit(Eq e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new Eq(v1, v2));
        Let l1 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
        return l1;
    }

    public Exp visit(LE e) {
        Var v1 = new Var(new Id(gen()));
        Var v2 = new Var(new Id(gen()));
        Let l2 = new Let(v1.id, new TInt(), e.e1.accept(this), new LE(v1, v2));
        Let l1 = new Let(v2.id, new TInt(), e.e2.accept(this), l2);
        return l1;
    }

    public Exp visit(If e) {
        Let l1 = (Let) e.e1.accept(this);
        Let l2 = (Let) l1.e2;
        Exp eq = l2.e2;
        If si = new If(eq, e.e2.accept(this), e.e3.accept(this));
        Let lt2 = new Let(l2.id, l2.t, l2.e1, si);
        Let lt1 = new Let(l1.id, l1.t, l1.e1, lt2);
        return lt1;

    }

    public Exp visit(Let e) {
        // System.out.println("Current exp is " + e.toString());
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
                // System.out.println("LET " + e.id.id + " DOES exist");
                // creating a new name
                //let x' = Id.genid x
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
                // System.out.println("LET " + e.id.id + " DOES NOT exist");

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

    public Exp visit(LetRec e) {
        // System.out.println("--------------------------------------------");
        // System.out.println("Current Exp is" + e);
        // System.out.println("\tFunDef is " + e.fd.id + " \tArg List is " + e.fd.args + " and e is " + e.e);
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
            // System.out.println("LETREC " + e.fd.id.id + " DOES exist");
            // creating a new name
            //let x' = Id.genid x
            Var v = new Var(new Id(gen()));

            //Replace old function name with new name = find x env
            newFunctionName = v.id.id;
            // add new name mapping to the new hashmap
            newHashmap.get(e.fd.id.id).add(v.id);

            //push new hashmap to top of stack
            hashmapStack.push(newHashmap);
            // if x in let x = e1 in e2 does not exist, add the mapping current name -> current name to hashmap -> push hashmap to stack
        } else {
            // System.out.println("LETREC " + e.fd.id.id + " DOES NOT exist");

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
                // System.out.println("LETREC ARGS " + str + " DOES exist");
                // creating a new name
                //let x' = Id.genid x
                Var v = new Var(new Id(gen()));

                //Replace old function arg list with new list 
                newArgsList.add(v.id);

                // add new name mapping to the new hashmap
                newHashmap.get(str).add(v.id);
            } else {
                // System.out.println("LETREC ARGS " + str + " DOES NOT exist");
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

        // System.out.println("--------------------------------------------");
        return lr;
    }

    public Exp visit(Var e) {
        // System.out.println("Current exp is " + e.toString());
        HashMap<String, List> newHashmap = new HashMap<String, List>();
        if (!hashmapStack.empty()) {
            newHashmap = (HashMap)hashmapStack.peek();
        }
        if (newHashmap.containsKey(e.id.id)) {
            // System.out.println("VAR " + e.id.id + " DOES exist");
            // System.out.println("Replaced by " + hm.get(e.id.id).get(hm.get(e.id.id).size() - 1));
            Var newE = new Var((Id)newHashmap.get(e.id.id).get(newHashmap.get(e.id.id).size() - 1));
            return newE;
        } else {
            // System.out.println("VAR " + e.id.id + " DOES NOT exist");
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

    public Exp rec_app(Exp e, Exp app) {
        Let l = (Let)e;
        if (l.id.id.equals("\0")) {
            System.out.println(l.id.id);
            return app;
        } else {
            return (Exp) new Let(l.id, l.t, l.e1, rec_app(l.e2, app));
        }
    }

    public Exp visit(App e) {
        // System.out.println("Current expression is " + e.toString() + "\t App is " + ((Var)e.e).id + "\tArgument list is " + e.es.get(0));
        App app = new App(e.e.accept(this), printInfix2(e.es));
        return app;
        /*List<Exp> l = printInfix2(e.es);
        Iterator it = l.iterator();
        List<Exp> l2 = new LinkedList<Exp>();
        Let new_l = new Let(new Id("\0"),new TInt(),new Unit(),new Unit());
        while (it.hasNext()) {
            Exp exp = (Exp) it.next();
            if(exp.getClass()==Let.class){

                System.out.println("YO");
                Let lt = (Let) exp;
                l2.add(new Var(lt.id));
                new_l = new Let(lt.id,lt.t,lt.e1,new_l);
            }else {
                l2.add(exp);
            }
        }
        Exp app;
        app = new App(e.e.accept(this),l2);
        if (!new_l.id.id.equals("\0")) {
            new_l = (Let) rec_app(new_l,app);
            app = new_l;
        }
        return app;*/
    }

    public Exp visit(Tuple e) {
        Tuple t = new Tuple(printInfix2(e.es));
        return t;
    }

    public Exp visit(LetTuple e) {
        LetTuple lt = new LetTuple(e.ids, e.ts, e.e1.accept(this), e.e2.accept(this));
        return lt;
    }

    public Exp visit(Array e) {
        Array a = new Array(e.e1.accept(this), e.e2.accept(this));
        return a;
    }

    public Exp visit(Get e) {
        Get g = new Get(e.e1.accept(this), e.e2.accept(this));
        return g;
    }

    public Exp visit(Put e) {
        Put p = new Put(e.e1.accept(this), e.e2.accept(this), e.e3.accept(this));
        return p;
    }
}
