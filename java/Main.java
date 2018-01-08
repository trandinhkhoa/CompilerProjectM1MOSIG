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
	  static String fileIn = "";
	  static String fileOut = "";
	  static boolean topt = false;
	  static boolean popt = false;
	  static boolean hopt = false;
	  static boolean oopt=  false;
	  static boolean asmlopt = false;
	  static boolean vopt = false;
	  
	
	  static void add_option(String s) {
		  if (s.equals("-o")){			oopt = true; options.add(s);
		  }else if (s.equals("-h")){	hopt = true; options.add(s);
		  }else if (s.equals("-v")){	vopt = true; options.add(s);
		  }else if (s.equals("-t")){	topt = true; options.add(s);
		  }else if (s.equals("-p")){	popt = true; options.add(s);
		  }else if (s.equals("-asml")){	asmlopt = true; options.add(s);
		  }
	  }
	  
	  static boolean checkOpt(String argv[]){
		  int nb_file = 0 ;
		  for (int i = 0 ; i<argv.length ; i++) {
					if (argv[i].charAt(0) != '-'){ nb_file++; }
		  }
		  return (nb_file <=2);	  
	  }
	  
        
	  static boolean parseCommand(String argv[]){
	  
		if (!checkOpt(argv)){
			return false;
		}
		boolean needIn = false;
		boolean needOut = false;
		
		for (int i = 0 ;i<argv.length;i++) {
				String s = argv[i];
				if (s.equals("-o")){		oopt = true; options.add(s); needOut = true;
				}else if (s.equals("-h")){	hopt = true; options.add(s);
				}else if (s.equals("-v")){	vopt = true; options.add(s); 
				}else if (s.equals("-t")){	topt = true; options.add(s); needIn = true;
				}else if (s.equals("-p")){	popt = true; options.add(s); needIn = true;
				}else if (s.equals("-asml")){	asmlopt = true; options.add(s); needIn = true;
				}else{
					if (needIn && needOut ){
						return false;
					}else if (needIn){
						fileIn = argv[i]; needIn = false;
					}else if (needOut){
						fileOut = argv[i]; needOut = false;
					}else {
						if (fileIn.equals("")){
							fileIn = s;
						}
					}
				}
		}
		
		if(hopt||(topt&&popt)){
			return false;
		}
		
		if (fileIn.equals("")){
			fileIn = argv[argv.length-1];
		}
		return true;
			  
	  }
	  
	  
	
  
  static public void main(String argv[]) {    
		options = new LinkedList<String>();
		  
		if(! parseCommand(argv) ) {
			printHelp();
		
		}else {
		  
			try {  	
			
			  Parser p = new Parser(new Lexer(new FileReader(fileIn)));
			  Exp expression = (Exp) p.parse().value;      
			  //System.out.println("------ Print here ------");
			  assert (expression != null);
			  
				    
			  if(vopt){
				  System.out.println("------ AST ------");
				  expression.accept(new PrintVisitor());
				  System.out.println();System.out.println();
			  }
			  
			  if(!popt){
				  if(vopt) {
				  	System.out.println("------ Type Check ------");
				  
				  }
					  
				  VisitorTypeCheck t = new VisitorTypeCheck();
				  Exp expression2 = expression.accept(new Copy());
				  expression2.accept(t);
				  
				  if(vopt){
					  if(t.errorSet){
						  System.out.println("Error from type check");
					  }else {
						  System.out.println("No error from type check");
					  }
					  System.out.println();
				  }
				  
				  if(!topt) {
					  K_Norm k = new K_Norm();
					  expression2 = expression2.accept(k);
					  
					  if(vopt) {
						  System.out.println("------ AST K-Normalization ------");
						  expression2.accept(new PrintVisitor());
						  System.out.println();System.out.println();
					  }
					  
					  int x = k.x;
					 
					  if(vopt) {
						  System.out.println("------ AST Alpha-Conversion ------");
					  }
					  
					  alpha_conversion alpha = new alpha_conversion(x);
					  expression2 = expression2.accept(alpha);
					 
					  if(vopt) {
						  expression2.accept(new PrintVisitor());
						  System.out.println();System.out.println();
					   
						  System.out.println("------ AST Nested-Let ------");
					  }
					  
					  
					  expression2 = expression2.accept(new Nested_Let());
					  
					  if(vopt) {
						  expression2.accept(new PrintVisitor());
						  System.out.println();System.out.println();
					  }
					  
					  Closure c = new Closure();
					  expression2.accept(c);
					  
					  
					  if(vopt) {
						System.out.println("------ AST Closure ------");
						for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
						  c.closure_list.get(i).print();
						  System.out.println();
						} System.out.println();
					  
					  
						System.out.println("------ AST ASML Gen ------");
					  }
					  
					  
					  for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
						  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new ASML_Gen(c.fun_List)));
						  
						  if(vopt) {
							  c.closure_list.get(i).printASML();
							  System.out.println();System.out.println();
						  }
					  }  
					  expression2 = expression2.accept(new Reg_Alloc());
					  
					  String s = fileOut;
					  
					  File f = new File(s);
					  FileWriter fw = new FileWriter(f) ;					  
					  if(vopt) {
						  System.out.println("------ AST Register Allocation ------");
					  }
					  
					  for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
						  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new Reg_Alloc()));
						  
						  if(vopt) { c.closure_list.get(i).printASML();}
						  
						  c.closure_list.get(i).printIn(fw);
						  
						  if(vopt){System.out.println();}
					  }       
					  fw.close();
					  
					  if(vopt) {
					 	 System.out.println();
					 	 
					  }
						
					  if (!asmlopt){
						  String s_arm = fileOut;
						  
						  File f_arm = new File(s_arm);
						  FileWriter fw_arm = new FileWriter(f_arm) ;
						  if (vopt) {
							  System.out.println("------ ARM Generation ------");
						  }
						  for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
							  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new ARM_Gen(fw_arm)));
							  
							  if(vopt){
							 	 System.out.println();System.out.println();
							  }
						  }       
						  fw_arm.close();     
					 } 
					/*
					  if(vopt){
						  System.out.println("------ Height of the initial AST ----");
						  int height = Height.computeHeight(expression);
						  System.out.println("using Height.computeHeight: " + height); 

						  ObjVisitor<Integer> v = new HeightVisitor();
						  height = expression.accept(v);
						  System.out.println("using HeightVisitor: " + height);
					  }
					 */
				  }
			  }
			  System.exit(0);
			} catch (Exception e) {
				System.exit(1);    	
			}
	  }
  }
}

