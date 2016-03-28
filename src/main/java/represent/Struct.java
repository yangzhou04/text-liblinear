package represent;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class Struct<T> {

    private Map<String, T> namedElements;
    
    public Struct() {
    	namedElements = Maps.newHashMap();
    }
    
    public Struct(Entry<String, T>... namedElements) {
        this.namedElements = Maps.newHashMap();
        for (Entry<String, T> namedElement : namedElements)
        	this.namedElements.put(namedElement.getKey(), namedElement.getValue());
    }

    public T get(String name) {
    	return namedElements.get(name);
    }
    
    public boolean containsKey(String name) {
    	return namedElements.containsKey(name);
    }
    
    public Struct<T> put(String name, T value) {
    	namedElements.put(name, value);
    	return this;
    }
}
