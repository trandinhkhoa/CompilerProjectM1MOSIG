#! /bin/sh
cd "$(dirname "$0")"/.. || exit 1
MINCAMLC=java/mincamlc

ocaml tests/CodeGen/valid/midsub3.ml > ARM/expected/midsub4.txt
$MINCAMLC tests/CodeGen/valid/midsub4.ml 2> /dev/null 1> /dev/null
#Generate .s file
$MINCAMLC -o ARM/actual/midsub4.s tests/CodeGen/valid/midsub4.ml 2> /dev/null 1> /dev/null
#Generate .o file
cd "$HOME/GitHubProjects/CompilerProjectM1MOSIG/ARM/"
arm-none-eabi-as -o actual/midsub4.o actual/midsub4.s libmincaml.S 2> /dev/null 1> /dev/null
#Generate .arm
arm-none-eabi-ld -o actual/midsub4.arm actual/midsub4.o 2> /dev/null 1> /dev/null
#Generate .txt
qemu-arm actual/midsub4.arm > actual/midsub4.txt 

if diff actual/midsub4.txt expected/midsub4.txt 2> /dev/null 1> /dev/null
then 
	echo "match"
else
	echo "not match"
fi
