
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
