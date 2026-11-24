package by.it.group510971.rudko.lesson13;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

//Компоненты сильной связности орграфа (алгоритм Тарьяна).
//Тарьян выдает компоненты от стоков к истокам, поэтому список переворачиваем.
public class GraphC {

    private final Map<String, List<String>> graph;
    private final Map<String, Integer> index = new HashMap<>();
    private final Map<String, Integer> low = new HashMap<>();
    private final Set<String> onStack = new HashSet<>();
    private final Stack<String> stack = new Stack<>();
    private final List<TreeSet<String>> components = new ArrayList<>();
    private int counter = 0;

    GraphC(Map<String, List<String>> graph) {
        this.graph = graph;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = GraphReader.read(new Scanner(System.in));
        StringBuilder out = new StringBuilder();
        for (String component : new GraphC(graph).components()) {
            out.append(component).append('\n');
        }
        System.out.print(out);
    }

    //компоненты в порядке от истока к стоку, вершины внутри компоненты - по алфавиту
    List<String> components() {
        for (String v : graph.keySet()) {
            if (!index.containsKey(v)) strongConnect(v);
        }
        Collections.reverse(components);
        List<String> result = new ArrayList<>();
        for (TreeSet<String> c : components) {
            result.add(String.join("", c));
        }
        return result;
    }

    private void strongConnect(String v) {
        index.put(v, counter);
        low.put(v, counter);
        counter++;
        stack.push(v);
        onStack.add(v);
        for (String w : graph.get(v)) {
            if (!index.containsKey(w)) {
                strongConnect(w);
                low.put(v, Math.min(low.get(v), low.get(w)));
            } else if (onStack.contains(w)) {
                low.put(v, Math.min(low.get(v), index.get(w)));
            }
        }
        if (low.get(v).equals(index.get(v))) {
            TreeSet<String> component = new TreeSet<>();
            String w;
            do {
                w = stack.pop();
                onStack.remove(w);
                component.add(w);
            } while (!w.equals(v));
            components.add(component);
        }
    }
}
