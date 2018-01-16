/**
 * Tpair class is used to store the type equations
 * lnode indicates the type on left hand side of the equation and
 * rnode indicates the type on right hand side of the equation
 */
public class Tpair<Key,Value> {
	public Key lnode;
    public Value rnode;

    public Tpair(Key lnode, Value rnode) {
        this.lnode = lnode;
        this.rnode = rnode;
    }
    public String toString() {
        return "(" + lnode.toString().split("@") + "," + rnode.toString().split("@") + ")";
    }
}
