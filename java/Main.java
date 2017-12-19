import java_cup.runtime.*;
import java.io.*;
import java.util.*;

public class Main {
  
  static public void main(String argv[]) {    
    try {    	
      Parser p = new Parser(new Lexer(new FileReader(argv[0])));
      Exp expression = (Exp) p.parse().value;      
      System.out.println("------ Print here ------");
      assert (expression != null);

      System.out.println("------ AST ------");
      expression.accept(new PrintVisitor());
      System.out.println();
      System.out.println();
      
      System.out.println("------ Type Check ------");
      VisitorTypeCheck t = new VisitorTypeCheck();
      expression.accept(t);
      if(t.errorSet){
    	  System.out.println("Error from type check");
      }
      System.out.println();
      
      Exp expression2 = expression.accept(new Copy());
      K_Norm k = new K_Norm();
      expression2 = expression2.accept(k);
      
      System.out.println("------ AST K-Normalization ------");
      expression2.accept(new PrintVisitor());
      System.out.println();
      System.out.println();
      
      int x = k.x;
     
      
      System.out.println("------ AST Alpha-Conversion ------");
      alpha_conversion alpha = new alpha_conversion(x);
      expression2 = expression2.accept(alpha);
      expression2.accept(new PrintVisitor());
      System.out.println();
      System.out.println();
      
      System.out.println("------ AST Nested-Let ------");
      expression2 = expression2.accept(new Nested_Let());
      expression2.accept(new PrintVisitor());
      System.out.println();
      
      Closure c = new Closure();
      expression2.accept(c);
      /*
      System.out.println("------ AST nested Let-Exp ------");
      nestedLet nest = new nestedLet();
      expression2.accept(nest);
      expression2.accept(new PrintVisitor());
      System.out.println();
      */
      
      
      
      
      
      System.out.println("------ AST Closure ------");
      for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
    	  c.closure_list.get(i).print();
          System.out.println();
          System.out.println();
      } System.out.println();
      
      
      System.out.println("------ AST ASML Gen ------");
      for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
    	  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new ASML_Gen(c.fun_List)));
    	  c.closure_list.get(i).printASML();
          System.out.println();
          System.out.println();
      }       
      System.out.println();
      System.out.println();

      expression2 = expression2.accept(new Reg_Alloc());
      
      System.out.println("------ AST Register Allocation ------");
      for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
    	  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new Reg_Alloc()));
    	  c.closure_list.get(i).printASML();
          System.out.println();
          System.out.println();
      }       
      System.out.println();
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

