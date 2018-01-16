.text
.global _start

_start:
stmfd sp!, {fp, lr}
mov fp, sp
mov r5, #1
str r5, [ fp, #-4 ]
mov r5, #1
str r5, [ fp, #-8 ]
ldr  r7, [ fp, #-4 ]
str r7, [ fp, #-12 ]
ldr  r7, [ fp, #-8 ]
str r7, [ fp, #-16 ]
ldr  r7, [ fp, #-16 ]
mov r12, r7
ldr  r7, [ fp, #-12 ]
cmp r12, r7
beq then0
bal else0
then0 :
mov r5, #1
str r5, [ fp, #-20 ]
ldr  r0, [ fp, #-20 ]
bl min_caml_print_int
bal exit0
else0: 
mov r5, #100
str r5, [ fp, #-24 ]
ldr  r0, [ fp, #-24 ]
bl min_caml_print_int
exit0:


