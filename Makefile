
test:
	./scripts/mincaml-parser-test.sh	 	
	./scripts/mincaml-typechecking-test.sh	 
	./scripts/genarmexeTest.sh

all: 
	cd ./java && $(MAKE) clean
	cd ./java && $(MAKE)
	
clean:
	cd ./java && $(MAKE) clean
	

