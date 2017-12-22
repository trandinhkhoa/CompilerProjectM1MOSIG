import java_cup.runtime.*;
import sun.awt.FwDispatcher;

import java.io.*;
import java.util.*;

public class Main {
	
		static String getNames() {
			String s = "\n\t\tBuchra Aboubakr\n"+
						"\t\tKhoa Tran\n"+ 
					   "\t\tPierric Mazodier \n"+       
					   "\t\tSébastien Riou \n"+ 
					   "\t\tSwathi Sree \n";
			return s;
			 
		}

		static String getLogo() {
			String s = ""+ 
		" _    _ _ _                     _    _______                   \n"+
		"| |  | | | | (_)               | |  |__   __|               \n"   +
		"| |  | | | |_ _ _ __ ___   __ _| |_ ___| | ___  __ _ _ __ ___  \n"+
		"| |  | | | __| | '_ ` _ \\ / _` | __/ _ \\ |/ _ \\/ _` | '_ ` _ \\ \n"+
		"| |__| | | |_| | | | | | | (_| | ||  __/ |  __/ (_| | | | | | |\n"+
		" \\____/|_|\\__|_|_| |_| |_|\\__,_|\\__\\___|_|\\___|\\__,_|_| |_| |_|\n";
                                             return s;                  
                                                               
		}
	
		static void printHelp() {
			System.out.println(""+
					getLogo()
					+ "\n\nWelcome to the UltimateTeam Mincaml Compiler !\n"
					+"option1:\n"
					+ "\t -t : type check only\n" + 
					"\t -p : parse only\n" + 
					"option2:\n"+
					"\t -h : display help\n" + 
					"\t -o : output file\n" + 
					"\t -asml : output ASML\n"+
					"\t -v : display version\n" +
					"Warning: \n\tOnly [-option1] [-option2]* [file.ml] is allowed\n"
					+getNames()
					);
		}
		
	  static List<String> options;
	
	  static void add_option(String s) {
		  if (s.equals("-o")||s.equals("-h")||s.equals("-v")||s.equals("-t")||s.equals("-p")||s.equals("-asml")) {
			  options.add(s);
		  }
	  }
  
  static public void main(String argv[]) {    
	  options = new LinkedList<String>();
	  if(argv.length!=1) {
		  /*if (argv.length>3) {
			  System.out.println("Only [-option1] [file.ml] or [-option1][-v][file.ml] are allowed");
		  }else*/ if (argv.length>1) {
			  for (int i = 0 ;i<argv.length;i++) {
				 add_option(argv[i]);
			  }
			  if(options.contains("-t")&&options.contains("-p")) {
				  printHelp();
				 // System.exit(-1);
			  }
		  }else {
			  for (int i = 0 ;i<argv.length;i++) {
				  add_option(argv[i]);
			  }
		  }
	  }
	if(options.contains("-h")) {
		printHelp();
		
	}else {
	  
    try {  	
    	
      Parser p = new Parser(new Lexer(new FileReader(argv[argv.length-1])));
      Exp expression = (Exp) p.parse().value;      
      //System.out.println("------ Print here ------");
      assert (expression != null);
      
      
      if(options.contains("-v")) {
	      System.out.println("------ AST ------");
	      expression.accept(new PrintVisitor());
	      System.out.println();
	      System.out.println();}
	  if(options.contains("-p")) {}else{
		  if(options.contains("-v")) {System.out.println("------ Type Check ------");}
      VisitorTypeCheck t = new VisitorTypeCheck();

      Exp expression2 = expression.accept(new Copy());
      expression2.accept(t);
      if(options.contains("-v")) {
	      if(t.errorSet){
	    	  System.out.println("Error from type check");
	      }else {
	    	  System.out.println("No error from type check");
	      }
	      System.out.println();
      }
      if(options.contains("-t")) {}else {
      K_Norm k = new K_Norm();
      expression2 = expression2.accept(k);
      
      if(options.contains("-v")) {
	      System.out.println("------ AST K-Normalization ------");
	      expression2.accept(new PrintVisitor());
	      System.out.println();
	      System.out.println();}
      
      int x = k.x;
     
      if(options.contains("-v")) {
    	  System.out.println("------ AST Alpha-Conversion ------");
      }
      alpha_conversion alpha = new alpha_conversion(x);
      expression2 = expression2.accept(alpha);
     
      if(options.contains("-v")) {
	      expression2.accept(new PrintVisitor());
	      System.out.println();
	      System.out.println();
            
	      System.out.println("------ AST Nested-Let ------");
      }
      expression2 = expression2.accept(new Nested_Let());
      
      if(options.contains("-v")) {
	      expression2.accept(new PrintVisitor());
	      System.out.println();System.out.println();
      }
      Closure c = new Closure();
      expression2.accept(c);
      
      
      if(options.contains("-v")) {
	      System.out.println("------ AST Closure ------");
	      for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
	    	  c.closure_list.get(i).print();
	          System.out.println();
	      } System.out.println();
      
      
	      System.out.println("------ AST ASML Gen ------");
      }
      for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
    	  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new ASML_Gen(c.fun_List)));
    	  
    	  if(options.contains("-v")) {
	    	  c.closure_list.get(i).printASML();
	          System.out.println();
    	  }
          System.out.println();
      }  
      expression2 = expression2.accept(new Reg_Alloc());
      
      String[] tab = argv[argv.length-1].split("/", argv[argv.length-1].length());
      
      String s = "asml/"+ tab[tab.length-1].replace(".ml", ".asml");
      
      File f = new File(s);
      FileWriter fw = new FileWriter(f) ;
      if(options.contains("-v")) {
    	  System.out.println("------ AST Register Allocation ------");
      }
      for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
    	  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new Reg_Alloc()));
    	  if(options.contains("-v")) { c.closure_list.get(i).printASML();}
    	  if(options.contains("-asml")){c.closure_list.get(i).printIn(fw);}
          System.out.println();
      }       
      fw.close();
      if(options.contains("-v")) {
      System.out.println();

	      // String s_arm = "/home/khoa/Study/armExamples/"+ tab[tab.length-1].replace(".ml", ".s");
      String s_arm = "ARM/" + tab[tab.length-1].replace(".ml", ".s");
      File f_arm = new File(s_arm);
      FileWriter fw_arm = new FileWriter(f_arm) ;

      System.out.println("------ ARM Generation ------");
      for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
    	  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new ARM_Gen(fw_arm)));
          System.out.println();
          System.out.println();
      }       
      fw_arm.close();      

	      System.out.println("------ Height of the initial AST ----");
	      int height = Height.computeHeight(expression);
	      System.out.println("using Height.computeHeight: " + height); 
	
	      ObjVisitor<Integer> v = new HeightVisitor();
	      height = expression.accept(v);
	      System.out.println("using HeightVisitor: " + height);

	    	System.exit(0);
      }
      }}
    } catch (Exception e) {
    	
    	//printHelp();
    	System.exit(1);
    	//e.printStackTrace();
    	
    }
  }
  }
}

