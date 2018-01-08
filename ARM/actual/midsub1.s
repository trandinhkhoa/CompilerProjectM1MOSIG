.text
.global _start
_start:
mov	r4, #42
mov	r0, r4
bl	min_caml_print_int
mov	r5, r0