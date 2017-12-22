# CompilerProjectM1MOSIG

## Commands to use for testing the project for the mid-Submission 
(Because they are a bit different to the ones on the website)

          git clone git@github.com:trandinhkhoa/CompilerProjectM1MOSIG.git
          cd CompilerProjectM1MOSIG
          make
          echo "let x = 42 in print_int x" > test1.ml
          scripts/mincamlc -t test1.ml
          echo $?
          scripts/mincamlc -asml test1.ml
          tools/asml asml/test1.asml
          scripts/mincamlc -o test1.ml
          make test
 ## The asml generated files are in the asml directory
 ## The ARM generated files are in the ARM directory




(Commands of the Website:

          git clone [repo_url]
          cd [repo]
          make # compile your program
          echo "let x = 42 in print_int x" > test1.ml
          scripts/mincamlc -t test1.ml 
          echo $? # should be 0
          scripts/mincamlc -asml test1.ml -o test1.asml
          tools/asml test1.asml  # displays 42
          scripts/mincamlc -o test1.s test1.ml
          make test # should run your tests
)
