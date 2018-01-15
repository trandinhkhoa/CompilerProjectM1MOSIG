let x = 123 in 
let y = 456 in
let z = -789 in
print_int
  ((if z < 0 then y else x) +
   (if x > 0 then z else y) +
   (if y < 0 then x else z))
