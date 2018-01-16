import java.util.*;

public class VisitorTypeCheck implements ObjVisitor<Type>{
	
	HashMap<String,Type> delVar= new HashMap<>(); // to store inferred variables' type
	boolean errorSet = false;
	
		
	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Unit e) {
		return new TUnit();	
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Bool e) {
		return new TBool();	
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Int e) {	
		return new TInt(); 
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Float e) {
		return new TFloat(); 
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Not e) {
		if(e.e.accept(this).getClass()==TUnresolvedType.class){  //if the type of variable is not unknown
			String str = ((Var) e.e).id.id;	
			delVar.put(str, new TBool());	
		}
		if (TBool.class==e.e.accept(this).getClass()){	// if the variable type is bool
			return new TBool();
		}
		else{
			//System.err.println("Type check error in Integer Not "+e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}	
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Neg e) {
		
		if(TInt.class==e.e.accept(this).getClass()){
			return new TInt();
		}else if(e.e.accept(this).getClass()==TUnresolvedType.class){
			String str = ((Var) e.e).id.id;
			System.out.println("Str" + ((Var) e.e).id.id);
			delVar.put(str, new TInt());
			return new TUnresolvedType();
		}
		else{
			//System.err.println("Type check error in Integer Negation" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}			
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Add e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		if(type1.getClass() == TUnresolvedType.class) { //if operand1 is unknown type
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TInt());
		}
		if(type2.getClass() == TUnresolvedType.class){	//if operand2 is unknown type 
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TInt());
		}
		
		if(type1.getClass()==type2.getClass() && type1.getClass()==TInt.class){	//if both operands are of type Int
			return new TInt();
		}
		else{
			//System.err.println("Type check error in Integer Addition" + e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Sub e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		if(type1.getClass() == TUnresolvedType.class) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TInt());
		}
		if(type2.getClass() == TUnresolvedType.class){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TInt());
		}
		
		if(type1.getClass()==type2.getClass() && type1.getClass()==TInt.class){
			return new TInt();
		}
		else{
			//System.err.println("Type check error in Integer Subtraction" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(FNeg e) {
		if(e.e.accept(this).getClass()==TUnresolvedType.class){
			String str = ((Var) e.e).id.id;
			System.out.println("Str" + ((Var) e.e).id.id);
			delVar.put(str, new TFloat());
		}
		if(TInt.class==e.e.accept(this).getClass()){
			return new TFloat();
		}
		else{
			//System.err.println("Type check error in Float Negation" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(FAdd e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		if(type1.getClass() == TUnresolvedType.class) { //if operands are unknown type
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TFloat());
		}
		if(type2.getClass() == TUnresolvedType.class){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TFloat());
		}
		
		if(type1.getClass()==type2.getClass() && type1.getClass()==TFloat.class){
			return new TFloat();
		}
		else{
			//System.err.println("Type check error in Float Addition" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(FSub e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		if(type1.getClass() == TUnresolvedType.class) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TFloat());
		}
		if(type2.getClass() == TUnresolvedType.class){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TFloat());
		}
		
		if(type1.getClass()==type2.getClass() && type1.getClass()==TFloat.class){
			return new TFloat();
		}
		else{
			//System.err.println("Type check error in Float Subtraction" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(FMul e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		if(type1.getClass() == TUnresolvedType.class) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TFloat());
		}
		if(type2.getClass() == TUnresolvedType.class){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TFloat());
		}
		
		Type type = e.e1.accept(this);
		if(type.getClass()==type2.getClass() && type.getClass()==TFloat.class){
			return new TFloat();
		}
		else{
			//System.err.println("Type check error in Float Multiplication" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(FDiv e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		if(type1.getClass() == TUnresolvedType.class) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TFloat());
		}
		if(type2.getClass() == TUnresolvedType.class){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TFloat());
		}
		
		Type type = e.e1.accept(this);
		if(type.getClass()==type2.getClass() && type.getClass()==TFloat.class){
			return new TFloat();
		}
		else{
			//System.err.println("Type check error in Float Division" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Eq e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		//if one of the operand types is defined
		if(type1.getClass() == TUnresolvedType.class && (type2.getClass() == TInt.class)) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TInt());
		}
		if(type1.getClass() == TUnresolvedType.class && (type2.getClass() == TFloat.class)) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TFloat());
		}
		if(type2.getClass() == TUnresolvedType.class && (type1.getClass() == TInt.class)){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TInt());
		}
		if(type2.getClass() == TUnresolvedType.class && (type1.getClass() == TFloat.class)){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TFloat());
		}
		if(e.e1.accept(this).getClass()==type2.getClass()){
			return new TBool();
		}
		else{
			//System.err.println("Type check error in Equality" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(LE e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		
		//if one of the operand types is defined
		if(type1.getClass() == TUnresolvedType.class && (type2.getClass() == TInt.class)) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TInt());
		}
		if(type1.getClass() == TUnresolvedType.class && (type2.getClass() == TFloat.class)) { 
			String str1 = ((Var) e.e1).id.id;
			delVar.put(str1, new TFloat());
		}
		if(type2.getClass() == TUnresolvedType.class && (type1.getClass() == TInt.class)){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TInt());
		}
		if(type2.getClass() == TUnresolvedType.class && (type1.getClass() == TFloat.class)){
			String str2 = ((Var) e.e1).id.id;
			delVar.put(str2, new TFloat());
		}
		if(e.e1.accept(this).getClass()==type2.getClass()){
			return new TBool();
		}
		else{
			//System.err.println("Type check error in Lesser than equal" +e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(If e) {
		Type type_e1 =e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		Type type3 = e.e3.accept(this);
		if(type_e1.getClass()==TBool.class){
			if(type2.getClass()==TUnit.class){
				if(type3.getClass()==TUnit.class){
					if(type2.getClass()==type3.getClass()){
						return new TUnit();
					}
					else{
						//System.err.println("Type check error in If" + e);
						errorSet=true;
						System.out.println("Error from type check");System.exit(1);
						return null;
					}
				}
				else{
					//System.err.println("Type check error in If" + e);
					errorSet=true;
					System.out.println("Error from type check");System.exit(1);
					return null;
				}
			}else if(type2.getClass()==TInt.class){
				if(type2.getClass()==type3.getClass()){
					return new TInt();
					
				}
				else{
					//System.err.println("Type check error in If" + e);
					errorSet=true;
					System.out.println("Error from type check");System.exit(1);
					return null;
				}
			}
			else{
				//System.err.println("Type check error in If" + e);
				errorSet=true;
				System.out.println("Error from type check");System.exit(1);
				return null;
			}
		}
		else{
			//System.err.println("Type check error in If4" + e);
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Let e) {
		if(delVar.containsKey(e.id.id)){
			//System.err.println("Definition already present "+e.id);
		}
		Type t = e.e1.accept(this);
		if((t!=null)&&(t.getClass() == TVar.class)){
			if(e.t != t){
				//System.err.println("Type do not match " + e.id);
			}
		}
		delVar.put(e.id.id, t);	
		return e.e2.accept(this);
		
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Var e) {
		if((delVar.containsKey(e.id.id))){
			return delVar.get(e.id.id);
		}
		errorSet=true;
		System.out.println("Error from type check");System.exit(1);
		return null;
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(LetRec e) {
		/*if(delVar.containsKey(e.fd.id.id)){
			//System.err.println("Definition already present "+e.fd.id);
		}
		Type t = e.fd.e.accept(this);
		if(e.fd.type.getClass() == t.getClass()){
			if(TVar.class != e.fd.type.getClass()){
				//System.err.println("Type do not match " + e.fd.id);
			}
		}
		delVar.put(e.fd.id.id, t);
		return t;*/
		
		/*e.fd.e.accept(this);
		for (int i = 0; i < e.fd.args.size(); i++) {
			Var v = new Var(e.fd.args.get(i));
			v.accept(this);
		}
		return e.e.accept(this);*/
		
		return null;
	}
	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(App e) {
		if ((((Var)e.e).id.id.equals("print_int"))||(((Var)e.e).id.id.equals("print_char")))  {
			if ((e.es.size()==1)&&(e.es.get(0).accept(this).getClass()==TInt.class)) {
				return new TUnit();
			}else {
				errorSet=true;
				System.out.println("Error from type check");System.exit(1);
				return null;
			}
		}else if ((((Var)e.e).id.id.equals("print_newline")))  {
			if ((e.es.size()==1)&&(e.es.get(0).accept(this).getClass()==TUnit.class)) {
				return new TUnit();
			}else {
				errorSet=true;
				System.out.println("Error from type check");System.exit(1);
				return null;
			}
			
	}else {
			for (int i = 0; i< e.es.size(); i++) {
				e.es.get(i).accept(this);
			}
			return new TUnit();
		}
	}

	 void printInfix2(List<Exp> l) {
	        Iterator<Exp> it = l.iterator();
	        while (it.hasNext()) {
	        	  it.next().accept(this);
	        }
	    }
	
	/**
 	 * Visitor not yet implemented.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Tuple e) {
		printInfix2(e.es);
		return null;
	}

	/**
 	 * Visitor not yet implemented.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(LetTuple e) {
		return null;
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Array e) {
		Type type1 = e.e1.accept(this);
		if((type1!=null)&&(TInt.class != type1.getClass())){
			return null;
		}
		else{
			Type e_t = e.e2.accept(this);
			if((e_t!=null)&&(e_t.getClass() == TInt.class)){
				return new TInt();
			}
			else{
				return new TFloat();
			}
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Get e) {
		Type type1 = e.e1.accept(this);
		
		if((type1!=null)&&(type1.getClass() != TArray.class)){	//if its not array
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
		}
		Type type2 = e.e2.accept(this);
		
		if((type2!=null)&&(TInt.class != type2.getClass())){	//if the index is not int
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
		}
		if((type1!=null)&&(type1.getClass()==TInt.class)){
			return new TInt();
		}
		else{
			return new TFloat();
		}
	}

	/**
 	 * Visitor returning the type of the evaluated expression e.
 	 * 
 	 * @param e	input expression
 	 * 
 	 * @return the type of expression e.
 	 */
	@Override
	public Type visit(Put e) {
		Type type1 = e.e1.accept(this);
		Type type2 = e.e2.accept(this);
		Type type3 = e.e3.accept(this);
		
		if((type1!=null)&&(type1.getClass() != TArray.class)){	//if its not array
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
		}
		if((type2!=null)&&(TInt.class != type2.getClass())){	//if the index is not int
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
		}
		if ((type1!=null)&&(type3!=null)&&(type1.getClass()==TArray.class && type3.getClass()==TUnresolvedType.class)){	//if e3 is undefined type
			if (TVar.class == type3.getClass()){
				String str = ((Var)e.e3).id.id;
				if (delVar.containsKey(str)){
					delVar.remove(str);
					delVar.put(str, new TInt());
					return new TUnit();
				}
				else{
					errorSet=true;
					System.out.println("Error from type check");System.exit(1);
					return null;
				}
			}
			else{
				errorSet=true;
				System.out.println("Error from type check");System.exit(1);
				return null;
			}
		}
		else{
			errorSet=true;
			System.out.println("Error from type check");System.exit(1);
			return null;
		}
	}
}
