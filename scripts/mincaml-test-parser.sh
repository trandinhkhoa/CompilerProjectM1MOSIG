#! /bin/sh
cd "$(dirname "$0")"/.. || exit 1

Bold=$(tput bold)
Normal=$(tput sgr0)
Uline=$(tput smul)
NUline=$(tput rmul)


Gray='\033[1;30m'
Red='\033[1;31m'
LRed='\033[0;31m'
Green='\033[1;32m'
LGreen='\033[0;32m'
Orange='\033[0;33m'
Yellow='\033[1;33m'
Blue='\033[0;34m'
LBlue='\033[1;34m'
Purple='\033[0;35m'
LPurple='\033[1;35m'
Cyan='\033[0;36m'
LCyan='\033[1;36m'

NC='\033[0m' 

# TODO change this to point to your mincamlc executable if it's different, or add
# it to your PATH. Use the appropriate option to run the parser as soon
# as it is implemented
MINCAMLC=java/mincamlc

# run all test cases in syntax/valid and make sure they are parsed without error
# run all test cases in syntax/invalid and make sure the parser returns an error

# TODO extends this script to run test in subdirectories
# 

echo "------------- ${Bold}${Blue}W${Yellow}e${LRed}l${Cyan}c${LGreen}o${Orange}m${Purple}e ${Blue}t${Red}o ${Orange}U${LBlue}l${LPurple}t${Green}i${Cyan}m${Gray}a${Blue}t${Yellow}e${LCyan}T${LRed}e${Cyan}a${Orange}m ${Purple}C${Blue}o${Red}m${Green}p${Gray}i${LRed}l${Orange}e${Cyan}r ${Red}T${Green}e${LRed}s${LGreen}t${LRed}s ${Normal}-------------" 


echo "${Yellow}${Bold}${Uline}Given Syntax Test:${NC}${Normal}${NUline}"
echo "${Cyan}${Bold}Valid Tests:${NC}${Normal}"
for test_case in tests/syntax/valid/*.ml
do
    echo -n "${test_case##*/}"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "${Green}|-> OK${NC}"
    else 
        echo "${Red}|-> KO${NC}"
    fi
done

echo "${Cyan}${Bold}Invalid Tests:${NC}${Normal}"
for test_case in tests/syntax/invalid/*.ml
do
    echo -n "${test_case##*/}"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "${Red}|-> KO${NC}"
    else 
        echo "${Green}|-> OK${NC}"
    fi
done

echo "${Yellow}${Bold}${Uline}Type Checking Test:${NC}${Normal}${NUline}"
echo "${Cyan}${Bold}Valid Tests:${NC}${Normal}"
for test_case in tests/typechecking/valid/*.ml
do
    echo -n "${test_case##*/}"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then 
	echo "${Green}|-> OK${NC}"
    else 
	echo "${Red}|-> KO${NC}"
    fi
done

echo "${Cyan}${Bold}Invalid Tests:${NC}${Normal}"
for test_case in tests/typechecking/invalid/*.ml
do
    echo -n "${test_case##*/}"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "${Red}|-> KO${NC}"
    else 
        echo "${Green}|-> OK${NC}"
    fi
done

echo "${Yellow}${Bold}${Uline}Code Generation Test:${NC}${Normal}${NUline}"
echo "${Cyan}${Bold}Valid Tests:${NC}${Normal}"
#    result = $(diff tests/typechecking/ExpectedOutput/name.txt "java -cp java-cup-11b-runtime.jar:. Main test_case.ml")
#    [ $result -eq 0 ]

for test_case in tests/CodeGen/valid/*.ml
do
    echo -n "${test_case##*/}"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then 
	echo "${Green}|-> OK${NC}"
    else 
	echo "${Red}|-> KO${NC}"
    fi
done

echo "${Cyan}${Bold}Invalid Tests:${NC}${Normal}"
for test_case in tests/CodeGen/invalid/*.ml
do
    echo -n "${test_case##*/}"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "${Red}|-> KO${NC}"
    else 
        echo "${Green}|-> OK${NC}"
    fi
done
