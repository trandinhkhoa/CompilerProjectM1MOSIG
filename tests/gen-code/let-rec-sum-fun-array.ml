let rec sum v1 v2 = let arr = Array.create 1 1 in 
arr.(0)<-v1; 
arr.(1)<-v2;
arr.(0)+arr.(1) in 
print_int(sum 5 6)
