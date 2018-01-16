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
					+ "\n\nWelcome to the UltimateTeam Mincaml Compiler Help !\n"
					+ "\t -t : type checking only\n" + 
					"\t -p : parsing only\n" + 
					"\t -h : display help\n" + 
					"\t -v : display version\n"+
					"\t -all : in addition to v, display all the steps\n"+
					"\t -o : output file\n" + 
					"\t -asml : output ASML\n"+
					"\t -nt : no type checking \n"+
					"\t -eqt : alternative equation type checking \n"+
					"\n\t creators :\n"+getNames()
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
	  static boolean ntopt = false;
	  static boolean vallopt = false;
	  static boolean eqtopt = false;
	  
	  static boolean checkOpt(String argv[]){
		  int nb_file = 0 ;
		  for (int i = 0 ; i<argv.length ; i++) {
					if (argv[i].charAt(0) != '-'){ nb_file++; }
		  }
		  return (nb_file <=2);	  
	  }
	  
        
	  static boolean parseCommand(String argv[]){
	  
		if (!checkOpt(argv)){
			System.err.println("Too many arguments.");
			return false;
		}
		boolean needIn = false;
		boolean needOut = false;
		
		for (int i = 0 ;i<argv.length;i++) {
				String s = argv[i];
				if (s.equals("-o")){		oopt = true; options.add(s); needOut = true;
				}else if (s.equals("-h")){	hopt = true; options.add(s);
				}else if (s.equals("-v")){	vopt = true; options.add(s); 
				}else if (s.equals("-all")){	vallopt = true; options.add(s); 
				}else if (s.equals("-eqt")){	eqtopt = true; options.add(s); 
				}else if (s.equals("-t")){	topt = true; options.add(s); 
				}else if (s.equals("-nt")){	ntopt = true; options.add(s);
				}else if (s.equals("-p")){	popt = true; options.add(s); 
				}else if (s.equals("-asml")){	asmlopt = true; options.add(s); 
				}else{
				    if (needOut){
							fileOut = argv[i]; needOut = false;
					}else if (needIn){
						fileIn = argv[i]; needIn = false;
					}else {
						if (fileIn.equals("")){
							fileIn = s;
						}else {
							System.err.println("Too many arguments.");
							return false;
						}
					}
				}
		}
		
		if(hopt||(topt&&popt)||needIn||needOut||(asmlopt&&!oopt)){
			if(topt&&popt) {
				System.err.println("You have to choose between type only and parser only.");
			}else if (needIn || needOut) {
				System.err.println("You forgot an argument.");
			}
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
			  assert (expression != null);
			  
				    
			  if(vopt&&vallopt){
				  System.out.println("------ AST ------");
				  expression.accept(new PrintVisitor());
				  System.out.println();System.out.println();
			  }else if (vopt){
				  System.out.println("Parser done.");
				   
			  }
			  
			  if(!popt){
				  Exp expression2 = expression.accept(new Copy());
				  if (!ntopt) {
					  if(vopt&&vallopt) {
					  	System.out.println("------ Type Check ------");
					  
					  }
					
					  if (eqtopt) {
						  CallChecker t = new CallChecker(expression);
						  if(vopt&&vallopt){
							  if(t.errorSet){
								  System.out.println("Error from type check");
								  
							  }else {
								  System.out.println("No error from type check");
							  }
							  System.out.println();
						  }else if (vopt){
							  System.out.println("Type check done.");   
						  }
					  }else {
						  VisitorTypeCheck t = new VisitorTypeCheck();
						  expression2.accept(t);
						  if(vopt&&vallopt){
							  if(t.errorSet){
								  System.out.println("Error from type check");
							  }else {
								  System.out.println("No error from type check");
							  }
							  System.out.println();
						  }else if (vopt){
							  System.out.println("Type check done.");   
						  }
				      }

				  }
				  
				  if(!topt) {
					  K_Norm k = new K_Norm();
					  expression2 = expression2.accept(k);
					  
					  if(vopt&&vallopt) {
						  System.out.println("------ AST K-Normalization ------");
						  expression2.accept(new PrintVisitor());
						  System.out.println();System.out.println();
					  }
					  
					  int x = k.x;
					 
					  if(vopt&&vallopt) {
						  System.out.println("------ AST Alpha-Conversion ------");
					  }
					  
					  alpha_conversion alpha = new alpha_conversion(x);
					  expression2 = expression2.accept(alpha);
					 
					  if(vopt&&vallopt) {
						  expression2.accept(new PrintVisitor());
						  System.out.println();System.out.println();
					   
						  System.out.println("------ AST Nested-Let ------");
					  }
					  
					  
					  expression2 = expression2.accept(new Nested_Let());
					  
					  if(vopt&&vallopt) {
						  expression2.accept(new PrintVisitor());
						  System.out.println();System.out.println();
					  }
					  
					  Closure c = new Closure();
					  expression2.accept(c);
					  
					  
					  if(vopt&&vallopt) {
						System.out.println("------ AST Closure ------");
						for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
							if ((c.closure_list.get(i) == null)||(c.closure_list.get(i).code == null)) {
								c.closure_list.remove(i);
							}else {
						  c.closure_list.get(i).print();
						  System.out.println();}
						} System.out.println();
					  
					  
						System.out.println("------ AST ASML Gen ------");
					  }else if (vopt){
						  System.out.println("AST modified.");
						   
					  }
					  
					  
					  for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
						  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(new ASML_Gen(c.fun_List)));
						  
						  if(vopt&&vallopt) {
							  c.closure_list.get(i).printASML();
							  System.out.println();System.out.println();
						  }
					  }  					  
					  
					  if(vopt&&vallopt) {
						  System.out.println("------ AST Register Allocation ------");
					  }
					  List<Integer> index_list = new ArrayList<Integer>();
					  for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
						  SpillAlloc sa = new SpillAlloc();
						  c.closure_list.get(i).set_Exp(c.closure_list.get(i).code.accept(sa));
						  index_list.add(sa.index);
						  if(vopt&&vallopt) { 
							  	c.closure_list.get(i).printASML();
						  		System.out.println();
						  	}
					  }

					  if (asmlopt) {
						String s = fileOut;
						File f = new File(s);  
						FileWriter fw = new FileWriter(f) ;
						  for (int i = c.closure_list.size()-1 ; i >=0 ; i--){  
								c.closure_list.get(i).printIn(fw);
								fw.write("\n\n");
						  }
							fw.close();
					  }
					 
					  
					  if(vopt&&vallopt) {
					 	 System.out.println();
					 	 
					  }else if (vopt) {
						  System.out.println("Asml generated.");
						   
					  }
						
					  if (!asmlopt){
						  String s_arm = fileOut;
						  File f_arm = new File(s_arm);
						  FileWriter fw_arm = new FileWriter(f_arm) ;
						  if (vopt&&vallopt) {
							  System.out.println("------ ARM Generation ------");
						  }
						  fw_arm.write(".text\n.global _start\n\n");
						  
						  int if_i0 = 0;
						  
						  int if_i = 0;
						  
						  for (int i = c.closure_list.size()-1 ; i >=0 ; i--){
							  if (vopt&&vallopt) {
								  PrintARM arm_g = new PrintARM(index_list.get(i),c.closure_list.get(i).parameters,if_i0);
								  c.closure_list.get(i).prologue();
								  c.closure_list.get(i).code.accept(arm_g);
								  if (!arm_g.myStack.isEmpty()) {
									  System.out.println("mov r0, " + arm_g.myStack.pop());
									  arm_g.myStack.push("r0");
								  }
								  if(i!=0) {
									  c.closure_list.get(i).epilogue();;
								  }
								  System.out.println();System.out.println();
							  }
							  c.closure_list.get(i).headerFile(fw_arm);
							  PrintARMFile arm_gf = new PrintARMFile(fw_arm,index_list.get(i),c.closure_list.get(i).parameters,if_i);
                			  c.closure_list.get(i).prologueFile(fw_arm);;
							  c.closure_list.get(i).code.accept(arm_gf);
							  if (!arm_gf.myStack.isEmpty()) {
								  fw_arm.write("mov r0, " + arm_gf.myStack.pop()+"\n");
								  arm_gf.myStack.push("r0");
							  }
							  if(i!=0) {
								  c.closure_list.get(i).epilogueFile(fw_arm);;
							  }
							  
							  if_i = arm_gf.if_i;
							  
							  fw_arm.write("\n\n");
							  
							  
						  }       
						  fw_arm.close();  
					 } 
					
					  if(vopt&&vallopt){
						  System.out.println("------ Height of the initial AST ----");
						  int height = Height.computeHeight(expression);
						  System.out.println("using Height.computeHeight: " + height); 

						  ObjVisitor<Integer> v = new HeightVisitor();
						  height = expression.accept(v);
						  System.out.println("using HeightVisitor: " + height);
					  }else if(vopt){
						  System.out.println("Finished.");
					  }
				  }
			  }
			  System.exit(0);
			} catch (FileNotFoundException e) {
				System.err.print("Sorry, but the file doesn't exist ...");
				System.exit(1);   
			}catch (NotYetImplementedException e) {
				System.err.println(e.getMessage());
			} catch (Exception e) {
				System.out.println("NotImplemented");
				System.exit(1);    	
			}
	  }
  }
}

