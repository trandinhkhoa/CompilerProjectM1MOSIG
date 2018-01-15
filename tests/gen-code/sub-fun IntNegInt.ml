let rec make_sub x =
  let rec sub y = x - y in
  sub in
print_int ((make_sub 3) (-7))
