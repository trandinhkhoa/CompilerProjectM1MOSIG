.text
.global _start
_start:
mov	r4, #42
mov	r5, r4
mov	r0, r5
bl	min_caml_print_int
mov	r6, r0