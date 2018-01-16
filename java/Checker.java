import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CallChecker class creates ad calls EquationGenerator and obtain
 * generated equations and calls EquationSolver and obtains
 * its solved equations
 * 
 */
public class Checker {
	Exp program;
    EquationGenerator gen;
    List<Tpair<Type, Type>> input;
    List<Tpair<Type, Type>> equations;
    EquationSolver solver;
    boolean solution;
    
    public Checker(Exp pgm, Map<String, Type> predefs) {
        program = pgm;
        gen = new EquationGenerator();
        input = gen.genEquations(pgm, predefs);
        equations = new ArrayList<>(input);
        solver = new EquationSolver();
        solution = solver.getSolution(equations);
        //solver.display(solver.mp);
    }
    
    /**
     * getProgram returns the program
     * 
     */
    public Exp getProgram() {
        return program;
    }
    
    /**
     * getEquations returns the list of generated
     * equations
     * 
     */
    public List<Tpair<Type, Type>> getEquations() {
        return equations;
    }

    /**
     * getEquations returns the list of solved
     * equations
     * 
     */
    public boolean getSolution() {
        return solution;
    }

    
}
