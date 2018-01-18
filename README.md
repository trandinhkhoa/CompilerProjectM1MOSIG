# CompilerProjectM1MOSIG

## Option for compilation

### Help Option

		-h : display the help page of the program.

### Basic Option

		-v : run the programm by displaying the name of the steps .

##### Additional Option (in addition to v)
		
		-all : display the details of all the steps.

### Selection Options
					
		-t : run the program with the type checking only. (LetRec is not tested)
		-p : run the program with the parsing only.
		-nt : run the program without type checking 
					
### Output Options	

		-o : after this option you have to give the output file you want to generate (ARM file by default) .
				
##### Additional Option (in addition to -o)
		
		-asml : the output file will be an ASML file instead of an ARM file.
		
### Other Options

		-eqt :  alternative equation type checking (LetRec is not working)

## How to use it

### Generate ARM code

		scripts/mincamlc -o [output file] input file
		
		
### Generate ASML Code

		scripts/mincamlc -asml input file -o [output file] 
		
### Limitation

#### Integer


		Because of the ldr and str operation we can only use integer that can be written in less than 32 bits.
		
#### Stack


		We are using the "spill everithing" allocation so for some really big programms 
		the size of the stack can be not enough.
		
		
#### Language

		Arrays and tuples are not allowed and LetRec code can be generated but not type checked. 
	        You can also use first order functions but high order functions and partial functions are not allowed. 
