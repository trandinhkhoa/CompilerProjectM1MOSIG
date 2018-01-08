.text
.global _start
_start:
mov	r4, #1
mov	r5, r4
mov	r6, #5
mov	r7, r6
mov	r8, r5
add	r9, r8, r7
mov	r10, r9
mov	r11, #12
mov	r12, r11
mov	temp5, r10
add	temp7, temp5, r12
mov	r0, temp7
bl	min_caml_print_int
mov	temp9, r0