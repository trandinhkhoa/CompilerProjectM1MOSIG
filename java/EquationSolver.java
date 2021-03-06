import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EquationSolver class checks the type equations
 * for each expressions. This indicates whether the
 * program is correctly typed or not
 */

public class EquationSolver {

	Map<String, Type> mp = new HashMap<String, Type>();
	
	/**
	 * getSolution generates the final type equation when the program is
	 * correctly typed else it indicates the presence of error
	 * 
	 * @param List of type equations
	 * 
	 * @return true - if the program has error
	 * 		   false - if the program has no error
	 */
	public boolean getSolution(List<Tpair<Type, Type>> equations) {
		int typeCrt = 0;
		
		for (Tpair<Type, Type> tpair : equations) {
			Type lcheck = tpair.lnode;
			Type rcheck = tpair.rnode;
			for (Tpair<Type, Type> tpair2 : equations) {
				if(lcheck.getClass() == TVar.class && lcheck==tpair2.rnode && rcheck.getClass() == TVar.class){
					String slcheck = (String) lcheck.toString();
					if(tpair.lnode.equals(tpair2.rnode)){
						if(tpair.rnode.toString().substring(0, tpair.rnode.toString().indexOf('@')).replaceAll("@", "").equals(tpair2.lnode.toString().substring(0, tpair2.lnode.toString().indexOf('@')).replaceAll("@", ""))){
							//lcheck= tpair.rnode;
							mp.put(slcheck,tpair.rnode);
							//typeCrt++;
							//equations.remove(tpair);
							//equations.remove(tpair2);
						}
						else{
							typeCrt=-1;
						}
					}
				}
				if(rcheck.getClass()==TVar.class ){
					if(rcheck.equals(tpair2.lnode)){
						mp.put(rcheck.toString(),tpair2.rnode);
					}
					if(lcheck.getClass()!=TVar.class && rcheck.equals(tpair2.rnode)&& !(lcheck.toString().substring(0, lcheck.toString().indexOf('@')).replaceAll("@", "").equals(tpair2.lnode.toString().substring(0, tpair2.lnode.toString().indexOf('@')).replaceAll("@", "")))){
						typeCrt=-1;
					}
				}
				if(lcheck.getClass()==TVar.class){
					if(lcheck.equals(tpair2.rnode)){
						mp.put(lcheck.toString(),tpair2.lnode);
					}
					if(lcheck.getClass()!=TVar.class && lcheck.equals(tpair2.lnode)&& !(rcheck.toString().substring(0, rcheck.toString().indexOf('@')).replaceAll("@", "").equals(tpair2.rnode.toString().substring(0, tpair2.rnode.toString().indexOf('@')).replaceAll("@", "")))){
						typeCrt=-1;
					}
					if(lcheck.equals(tpair2.lnode) && tpair2.rnode.getClass() != TVar.class && tpair2.rnode.getClass() != TFun.class && rcheck.getClass()!=TVar.class){
						if(!rcheck.toString().substring(0, rcheck.toString().indexOf('@')).replaceAll("@", "").equals(tpair2.rnode.toString().substring(0, tpair2.rnode.toString().indexOf('@')).replaceAll("@", ""))){
							typeCrt=-1;
						}
					}
				}
				
			}
			if(lcheck.getClass() != TVar.class && rcheck.getClass() != TVar.class ){
				if(!(tpair.lnode.toString().substring(0, tpair.lnode.toString().indexOf('@')).replaceAll("@", "").equals(tpair.rnode.toString().substring(0, tpair.rnode.toString().indexOf('@')).replaceAll("@", "")))){
					typeCrt=-1;
				}
			}
		}
		if(typeCrt!=-1)
			display(mp);
		if(typeCrt<0)
			return true;
		else
			return false;
	}

	/**
	 * display is used to display the correct solutions of the solved
	 * 
	 * @param type equations
	 */
	public void display(Map<String, Type> equations) {
		int len = equations.size();
		//for(int i =1 ; i<=len ; i++ )
		System.out.println("Solver \n" + equations.keySet() + equations.values());
	}
}
