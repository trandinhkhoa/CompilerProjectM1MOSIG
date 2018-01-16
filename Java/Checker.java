import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
        solver.display(solver.mp);
    }
    public Exp getProgram() {
        return program;
    }

    public List<Tpair<Type, Type>> getEquations() {
        return equations;
    }

    public boolean getSolution() {
        return solution;
    }

    
}
