.text
.global _start
_start:
mov	r4, #2
mov	r5, r4
mov	r6, #1
mov	r7, r6
add	r8, r7, r5
mov	r9, r8
mov	r0, r9
bl	min_caml_print_int
mov	r10, r0