let m = 3 in 
	let dummy = Array.create 0 0. in
		let mat	= Array.create m dummy in
		mat.(i) <- Array.create n 0.;
	print_int dummy
print_int m


