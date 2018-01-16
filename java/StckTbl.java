import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;


public class StckTbl<Type> {

    private Stack<Map<String, Type>> stck;

    public StckTbl() {
        this.stck = new Stack<>();
    }

    public void pop() {
    	stck.pop();
    }

    public void put(String name, Type value) {
        Map<String, Type> tmp = stck.peek();
        tmp.put(name, value);
    }
    public void push() {
    	stck.push(new TreeMap<String, Type>());
    }

    private Map<String, Type> getElm(int index) {

        if (index == 0) {
            return stck.peek();
        }
        Map<String, Type> x = stck.pop();
        try {
            return getElm(index - 1);
        } finally {
            stck.push(x);
        }
    }

    public Type get(String name) {
        Map<String, Type> res;
        int i = 0;

        while (i < stck.size()) {
        	res = getElm(i);
            for (String key : res.keySet()) {
                if (key.equals(name)) {
                    return res.get(key);
                }
            }
            i++;
        }
        return null;
    }
    public void putAll(Map<String, ? extends Type> map) {
        stck.peek().putAll(map);
    }
}
