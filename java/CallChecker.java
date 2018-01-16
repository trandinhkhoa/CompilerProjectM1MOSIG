import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CallChecker {

	private static Exp e;
	static Map<String, Type> PREDEFS;
	List<Tpair<Type, Type>> eqs;
	public boolean errorSet;

	public CallChecker(Exp expression){
		this.e = expression;
		Checker typeChecker = new Checker(expression, PREDEFS);
		eqs = typeChecker.getEquations();
		displayEqs();
		errorSet=typeChecker.getSolution();
	}
    
    static {
    	
    	Type UNIT = new TUnit();
        Type INT = new TInt();
        Type FLOAT = new TFloat();
        
    	Map<String, Type> predefs  = new HashMap<>();
        predefs.put("print_newline",new TFun(UNIT, UNIT));
        predefs.put("print_int", new TFun(INT, UNIT));
        predefs.put("float_of_int", new TFun(INT, FLOAT));
        predefs.put("int_of_float", new TFun(FLOAT, INT));
        
        PREDEFS = Collections.unmodifiableMap(predefs);
    }
    
    public void displayEqs(){
    	System.out.println("Equations generated out of GenEquations function of format : (Type,Type)\n");
    	String value1 = null;
    	String value2 = null;
    	for (Tpair<Type, Type> tpair : eqs) {
    		String lValue = tpair.lnode.toString();
    		String rValue = tpair.rnode.toString();
    		if(lValue.contains("?")){
    			value1 = tpair.lnode.toString();
    		}
    		else{
    			int index1 = tpair.lnode.toString().indexOf('@');
   			 	value1 = tpair.lnode.toString().substring(0, index1).replaceAll("@", "");
    		}
    		if(rValue.contains("?")){
    			value2 = tpair.rnode.toString();
    		}
    		else{
   			 	int index2 = tpair.rnode.toString().indexOf('@');
   			 	value2 = tpair.rnode.toString().substring(0, index2).replaceAll("@", "");	
    		}
    		System.out.println("\n(" + value1 + "," + value2 +")\n");
		}
    }
}
