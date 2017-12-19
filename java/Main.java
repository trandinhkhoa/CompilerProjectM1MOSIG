import java_cup.runtime.*;
import java.io.*;
import java.util.*;

public class Main {
  
  static public void main(String argv[]) {    
    try {    	
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;      
      assert (expression != null);

      System.out.println("------ AST ------");
      expression.accept(new PrintVisitor());
      System.out.println();
      
      System.out.println("------ Type Check ------");
      VisitorTypeCheck t = new VisitorTypeCheck();
      expression.accept(t);
      if(t.errorSet){
    	  System.out.println("Error from type check");
      }
      System.out.println();
      
      Exp expression2 = expression.accept(new Copy());
      expression2 = expression2.accept(new K_Norm());
      
      System.out.println("------ AST K-Normalization ------");
      expression2.accept(new PrintVisitor());
      System.out.println();
      
      expression2 = expression2.accept(new Reg_Alloc());
      
      System.out.println("------ AST Register Allocation ------");
      expression2.accept(new PrintVisitor());
      System.out.println();

      System.out.println("------ Height of the AST ----");
      int height = Height.computeHeight(expression);
      System.out.println("using Height.computeHeight: " + height);

      ObjVisitor<Integer> v = new HeightVisitor();
      height = expression.accept(v);
      System.out.println("using HeightVisitor: " + height);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

