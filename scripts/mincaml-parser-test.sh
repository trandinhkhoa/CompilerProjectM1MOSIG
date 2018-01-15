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

MINCAMLC=java/mincamlc

echo "${Yellow}${Bold}${Uline}Syntax Test:${NC}${Normal}${NUline}"
echo "${Cyan}${Bold}Valid Tests:${NC}${Normal}"
for test_case in tests/syntax/valid/*.ml
do
    name=${test_case##*/}
    echo -n "${name%.*}"
    if $MINCAMLC -p "$test_case" 2> /dev/null 1> /dev/null
    then 
	echo "${Green}|-> OK${NC}"
    else 
	echo "${Red}|-> KO${NC}"
    fi
done

echo "${Cyan}${Bold}Invalid Tests:${NC}${Normal}"
for test_case in tests/syntax/invalid/*.ml
do
    name=${test_case##*/}
    echo -n "${name%.*}"
    if $MINCAMLC -p "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "${Red}|-> KO${NC}"
    else 
        echo "${Green}|-> OK${NC}"
    fi
done
