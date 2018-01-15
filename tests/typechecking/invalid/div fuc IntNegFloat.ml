let rec make_div x =
  let rec div y = x / y in
  div in
print_int ((make_div 3) (-7.2))
