let rec make_mult x =
  let rec mult y = x * y in
  mult in
print_int ((make_mult 3) (-7.2))
