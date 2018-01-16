#! /bin/sh
cd "$(dirname "$0")"/.. || exit 1

MINCAMLC=java/mincamlc

Red='\033[1;31m'
Green='\033[1;32m'
Yellow='\033[1;33m'

Bold=$(tput bold)
Normal=$(tput sgr0)
Uline=$(tput smul)
NUline=$(tput rmul)
NC='\033[0m'

echo "${Yellow}${Bold}${Uline}Code Gen Test:${NC}${Normal}${NUline}"

for test_case in tests/gen-code/*.ml
do
fname=$(basename "$test_case")
extension="${fname##*.}"
name="${fname%.*}"
echo -n $name": "
#Generate .expected
ocaml $test_case > "./ARM/expected/"$name.expected"" 2> /dev/null

cd "../CompilerProjectM1MOSIG"
#Generate .s file
$MINCAMLC -o ARM/actual/"$name.s" "$test_case" 2> /dev/null 1> /dev/null

#Generate .o file
cd "./ARM"
arm-none-eabi-as -o actual/"$name.o" actual/"$name.s" libmincaml.S 2> /dev/null 1> /dev/null

#Generate .arm
arm-none-eabi-ld -o actual/"$name.arm" actual/"$name.o" 2> /dev/null 1> /dev/null
#Generate .actual
qemu-arm actual/"$name.arm" > actual/"$name.actual"

if diff actual/"$name.actual" expected/"$name.expected" 2> /dev/null 1> /dev/null
then 
	echo "${Green}|-> OK${NC}"
else
	echo "${Red}|-> KO${NC}"
fi

rm actual/*.actual
rm expected/*.expected
rm actual/*.o
rm actual/*.arm
rm actual/*.s

cd "../"

done
