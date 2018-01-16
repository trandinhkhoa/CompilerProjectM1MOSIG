let rec make_sub x y =
  let rec sub y = x - y in
  sub y in
print_int (make_sub 3 7)
