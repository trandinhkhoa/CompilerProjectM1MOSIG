#! /bin/sh
cd "$(dirname "$0")"/.. || exit 1
MINCAMLC=java/mincamlc

sfile=$1
fname=$(basename "../CompilerProjectM1MOSIG/ARM/actual/$sfile")
extension="${fname##*.}"
name="${fname%.*}"

if [ $# -gt 0 ]; then
    ocaml $name.ml > $name.expected 2> /dev/null
	#Generate .o file
	arm-none-eabi-as -o $name.o $name.s libmincaml.S 

	#Generate .arm
	arm-none-eabi-ld -o $name.arm $name.o 2> /dev/null 1> /dev/null
	#Generate .actual
	qemu-arm $name.arm > $name.actual 

	if diff $name.actual $name.expected 2> /dev/null 1> /dev/null
	then 
		echo "OK"
	else
		echo "KO"
	fi

else
    echo "Argument Missing: Please Enter .s file as an argument"
fi
