package by.it.group410972.kapur.lesson13;

import java.util.*;

public class GraphC {

  private static void dfs1(String v, Map<String, Set<String>> graph, Set<String> visited, Deque<String> stack) {
    visited.add(v);
    for (String u : graph.get(v)) {
      if (!visited.contains(u)) dfs1(u, graph, visited, stack);
    }
    stack.push(v);
  }

  private static void dfs2(String v, Map<String, Set<String>> graphT, Set<String> visited, List<String> component) {
    visited.add(v);
    component.add(v);
    for (String u : graphT.get(v)) {
      if (!visited.contains(u)) dfs2(u, graphT, visited, component);
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();

    Map<String, Set<String>> graph = new HashMap<>();
    Map<String, Set<String>> graphT = new HashMap<>();
    Set<String> nodes = new TreeSet<>();

    for (String e : input.split(",")) {
      String[] p = e.trim().split("->");
      String from = p[0].trim();
      String to = p[1].trim();
      nodes.add(from);
      nodes.add(to);

      graph.putIfAbsent(from, new TreeSet<>());
      graph.putIfAbsent(to, new TreeSet<>());
      graph.get(from).add(to);

      graphT.putIfAbsent(from, new TreeSet<>());
      graphT.putIfAbsent(to, new TreeSet<>());
      graphT.get(to).add(from); // транспонирование
    }

    // Косарайю: первый проход DFS для топологического порядка
    Set<String> visited = new HashSet<>();
    Deque<String> stack = new ArrayDeque<>();
    for (String v : nodes) {
      if (!visited.contains(v)) dfs1(v, graph, visited, stack);
    }

    // второй проход DFS на транспонированном графе
    visited.clear();
    List<List<String>> allComponents = new ArrayList<>();
    while (!stack.isEmpty()) {
      String v = stack.pop();
      if (!visited.contains(v)) {
        List<String> component = new ArrayList<>();
        dfs2(v, graphT, visited, component);
        Collections.sort(component); // лексикографический порядок внутри компоненты
        allComponents.add(component);
      }
    }

    // сортировка компонент по истокам: компоненты без входящих ребер в исходном графе идут первыми
    allComponents.sort((a, b) -> {
      boolean aIn = hasIncoming(a, graph, nodes);
      boolean bIn = hasIncoming(b, graph, nodes);

      if (aIn != bIn)
        return aIn ? 1 : -1; // истоки первыми

      boolean aOut = hasOutgoing(a, graph);
      boolean bOut = hasOutgoing(b, graph);

      if (aOut != bOut)
        return aOut ? -1 : 1; // стоки последними

      if (a.size() != b.size())
        return Integer.compare(b.size(), a.size());

      return String.join("", a).compareTo(String.join("", b));
    });




    // вывод
    for (List<String> comp : allComponents) {
      System.out.println(String.join("", comp));
    }
  }

  private static boolean hasIncoming(List<String> comp,
                                     Map<String, Set<String>> graph,
                                     Set<String> nodes) {
    for (String node : comp) {
      for (String other : nodes) {
        if (!comp.contains(other) && graph.get(other).contains(node))
          return true;
      }
    }
    return false;
  }

  private static boolean hasOutgoing(List<String> comp,
                                     Map<String, Set<String>> graph) {
    for (String node : comp) {
      for (String to : graph.get(node)) {
        if (!comp.contains(to))
          return true;
      }
    }
    return false;
  }


}
