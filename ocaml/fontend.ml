open Syntax
open Printf

let rec exists x l_var  =
	match l_var with
	|(e,v)::s -> if e=x then (true,v) else exists x s
	|_ -> (false,"")


let find e l_var = 
	let (b,v) = (exists e l_var) in   
	if b
	then	v
	else	""
;;


let get_index =
  let counter = ref (0) in
  let x = counter in
  incr counter;
  "temp"^(string_of_int !x)
;;



let rec rec_norm exp =
    match exp with
	| Unit -> Unit
	| Int i -> Int i
	| Float f -> Float f
	| Bool b -> Bool b
	| Not e -> Not (rec_norm e)
	| Neg e -> Neg (rec_norm e)
	| Add (e1, e2) ->	let x1 = get_index in
				let x2 = get_index in
				Let ((x1,Type.Int), rec_norm e1,
				Let ((x2,Type.Int), rec_norm e2 , Add (Var x1, Var x2)))
	| Sub (e1, e2) ->	let x1 = get_index in
				let x2 = get_index in	
				Let ((x1,Type.Int), rec_norm e1,
				Let ((x2,Type.Int), rec_norm e2 , Sub (Var x1,Var x2)))
	| FNeg e -> FNeg (rec_norm e)
	| FAdd (e1, e2) -> 	let x1 = get_index in
				let x2 = get_index in	
				Let ((x1,Type.Float), rec_norm e1, 
				Let ((x2,Type.Float), rec_norm e2 , FAdd (Var x1,Var x2)))
	| FSub (e1, e2) -> 	let x1 = get_index in
				let x2 = get_index in	
				Let ((x1,Type.Float), rec_norm e1, 
				Let ((x2,Type.Float), rec_norm e2 , FSub (Var x1,Var x2)))
	| FMul (e1, e2) -> 	let x1 = get_index in
				let x2 = get_index in	
				Let ((x1,Type.Float), rec_norm e1, 
				Let ((x2,Type.Float), rec_norm e2 , FMul (Var x1,Var x2)))
	| FDiv (e1, e2) -> 	let x1 = get_index in
				let x2 = get_index in	
				Let ((x1,Type.Float), rec_norm e1, 
				Let ((x2,Type.Float), rec_norm e2 , FDiv (Var x1,Var x2)))
	| Eq (e1, e2) -> Eq (rec_norm e1 , rec_norm e2) 
	| LE (e1, e2) -> LE (rec_norm e1 , rec_norm e2)  
	| If (Not(Eq(b1,b2)), e2, e3) -> let x1 = get_index in
							let x2 = get_index in
							Let((x1,Type.Int), rec_norm b1,
							Let((x2,Type.Int), rec_norm b2, If(Eq(Var x1,Var x2),rec_norm e3, rec_norm e2)))
	| If (Not(LE(b1,b2)), e2, e3) -> let x1 = get_index in
							let x2 = get_index in
							Let((x1,Type.Int), rec_norm b1,
							Let((x2,Type.Int), rec_norm b2, If(LE(Var x1,Var x2),rec_norm e3, rec_norm e2)))
	| If (Eq(b1,b2), e2, e3) -> let x1 = get_index in
							let x2 = get_index in
							Let((x1,Type.Int), rec_norm b1,
							Let((x2,Type.Int), rec_norm b2, If(Eq(Var x1,Var x2),rec_norm e2, rec_norm e3)))
	| If (LE(b1,b2), e2, e3) -> let x1 = get_index in
							let x2 = get_index in
							Let((x1,Type.Int), rec_norm b1,
							Let((x2,Type.Int), rec_norm b2, If(LE(Var x1,Var x2),rec_norm e2, rec_norm e3)))
	| Get(e1, e2) -> Get (rec_norm e1 , rec_norm e2)
	| Put(e1, e2, e3) -> Put (rec_norm e1 , rec_norm e2 , rec_norm e3)	
	| Array(e1,e2) -> Array (rec_norm e1 , rec_norm e2) 
	| Tuple(l) -> Tuple (List.map rec_norm l ) 
	| LetTuple (l, e1, e2)-> LetTuple (l, rec_norm e1, rec_norm e2) (*LetTuple ( (List.map (fun x ->  let (e,t) = x in rec_norm e,t;) l),
					 rec_norm e1 , rec_norm e2))*)
	| App (e1, le2) -> App (rec_norm e1 , List.map rec_norm le2 ) 
	| Var id -> Var id
	| Let ((id,t), e1, e2) -> Let ((id,t), rec_norm e1 ,rec_norm e2)   	
	| LetRec (fd, e) -> (*let fd.arg = List.map rec_norm fd.arg in*)
						LetRec (fd,rec_norm e)
;;	

let norm f = 
  let inchan = open_in f in
  try
    let l = (Parser.exp Lexer.token (Lexing.from_channel inchan)) in
	 print_string (Syntax.to_string (rec_norm l) );
    close_in inchan
  with e -> (close_in inchan; raise e);;



let () = 
  let files = ref [] in
  Arg.parse
    [ ]
    (fun s -> files := !files @ [s])
    (Printf.sprintf "usage: %s filenames" Sys.argv.(0));
  List.iter
    (fun f -> ignore (norm f))
    !files

