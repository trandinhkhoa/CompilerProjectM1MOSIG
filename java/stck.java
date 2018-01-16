import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;


public class stck<T>{

	  private Stack<Map<String, T>> st;

	    public stck() {
	        this.st = new Stack<>();
	    }

	    public void push() {
	        st.push(new TreeMap<String, T>());
	    }

	    public void pop() {
	        st.pop();
	    }

	    public void put(String name, T value) {
	        Map<String, T> tmp = st.peek();
	        tmp.put(name, value);
	    }

	    public void putAll(Collection<? extends Map.Entry<String, T>> all) {
	        Map<String, T> top = st.peek();
	        for (Map.Entry<String, T> e : all) {
	            top.put(e.getKey(), e.getValue());
	        }
	    }

	    public void putAll(Map<String, ? extends T> map) {
	        st.peek().putAll(map);
	    }

	    private Map<String, T> getStackElement(int index) {

	        if (index == 0)
	            return st.peek();


	        Map<String, T> x = st.pop();
	        try {
	            return getStackElement(index - 1);
	        } finally {
	            st.push(x);
	        }
	    }

	    public T get(String name) {
	        Map<String, T> result;
	        int i = 0;

	        while (i < st.size()) {
	            result = getStackElement(i);
	            for (String key : result.keySet()) {
	                if (key.equals(name)) {
	                    return result.get(key);
	                }
	            }
	            i++;
	        }
	        return null;
	    }
}
