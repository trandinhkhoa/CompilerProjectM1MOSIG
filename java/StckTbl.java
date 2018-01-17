import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Class StckTbl used to store and retrieve types
 *
 */
public class StckTbl<Type> {

    private Stack<Map<String, Type>> stck;

    public StckTbl() {
        this.stck = new Stack<>();
    }
    
    /**
     * pop() pops the top element
     *
     */
    public void pop() {
    	stck.pop();
    }
    
    /**
     * put() puts the type and name to the Map
     *
     */
    public void put(String name, Type value) {
        Map<String, Type> tmp = stck.peek();
        tmp.put(name, value);
    }
    public void push() {
    	stck.push(new TreeMap<String, Type>());
    }

    /**
     * getElm gets the type and name present at the index
     *
     *@param index
     *
     *@return the type and name of the element at index
     */
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

    /**
     * get gets the type of the variable of name
     *
     *@param name of variable
     *
     *@return the type of name
     */
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
    
    /**
     * putAll adds whole map into the stack
     *
     *@param map
     */
    public void putAll(Map<String, ? extends Type> map) {
        stck.peek().putAll(map);
    }
}
