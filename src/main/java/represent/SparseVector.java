package represent;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SparseVector {

    private List<Entry<Integer, Double>> container;
    private Set<Integer> keySet;
    private int dim;
    
    public SparseVector(int dim) {
        this.dim = dim;
        container = Lists.newLinkedList();
        keySet = Sets.newHashSet();
    }
    
    public void set(int i, double val) {
        
    }
    
    public double get(int i) {
        return 0;
    }
}
