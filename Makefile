
all: 
	cd ./java && $(MAKE) clean
	cd ./java && $(MAKE)
	
clean:
	cd ./java && $(MAKE) clean
	
test:
	 ./scripts/mincaml-test-parser.sh
