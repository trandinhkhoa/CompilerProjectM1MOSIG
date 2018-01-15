.text
.global _start

_start:
mov fp, sp
mov r5, #42
str r5, [ fp, #-4 ]
ldr  r7, [ fp, #-4 ]
str r7, [ fp, #-8 ]
ldr r9, =#0xc
add sp, fp, r9
mov r9, #-4
add sp, sp, r9
mov r9, #-8
add sp, sp, r9
ldr  r0, [ fp, #-8 ]
bl min_caml_print_int


