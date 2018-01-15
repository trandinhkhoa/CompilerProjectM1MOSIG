let x = truncate 123 in
let y = truncate 456 in
let z = truncate (-789) in
print_int
  ((if z < 0 then y else x) +
   (if x > 0 then z else y) +
   (if y < 0 then x else z))
