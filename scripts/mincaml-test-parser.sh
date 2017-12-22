#! /bin/sh
cd "$(dirname "$0")"/.. || exit 1

# TODO change this to point to your mincamlc executable if it's different, or add
# it to your PATH. Use the appropriate option to run the parser as soon
# as it is implemented
MINCAMLC=java/mincamlc

# run all test cases in syntax/valid and make sure they are parsed without error
# run all test cases in syntax/invalid and make sure the parser returns an error

# TODO extends this script to run test in subdirectories
# 

echo "*************************Syntax Tests*************************"

echo "1. Valid Tests:"
for test_case in tests/syntax/valid/*.ml
do
    echo "testing parser on: $test_case"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "OK"
    else 
        echo "KO"
    fi
done

echo "2. InValid Tests:"
for test_case in tests/syntax/invalid/*.ml
do
    echo "testing parser on: $test_case"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "KO"
    else 
        echo "OK"
    fi
done

echo "*************************Type Checking Tests*************************"

echo "1. Valid Tests:"
for test_case in tests/typechecking/valid/*.ml
do
#        name = $(basename $test_case) | sed #${test_case%.*}

    echo "testing parser on: $test_case"
#    result = $(diff tests/typechecking/ExpectedOutput/name.txt "java -cp java-cup-11b-runtime.jar:. Main test_case.ml")
#    [ $result -eq 0 ]
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "OK"
    else 
        echo "KO"
    fi
done

echo "2. InValid Tests:"
for test_case in tests/typechecking/invalid/*.ml
do
    echo "testing parser on: $test_case"
    if $MINCAMLC "$test_case" 2> /dev/null 1> /dev/null
    then
        echo "KO"
    else 
        echo "OK"
    fi
done
