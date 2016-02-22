package represent;

import java.util.List;

import com.google.common.collect.Lists;

public class Struct<T> {

    public List<T> elements;
    
    public Struct(T... element) {
        elements = Lists.newArrayList(element);
    }

    public T get(int i) {
        return elements.get(i);
    }
}
