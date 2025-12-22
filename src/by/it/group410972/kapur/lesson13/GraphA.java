package by.it.group410972.kapur.lesson13;

import java.util.*;

public class GraphA {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();

    Map<String, Set<String>> graph = new HashMap<>();
    Map<String, Integer> inDegree = new HashMap<>();

    // Разбор входной строки
    String[] edges = input.split(",");
    for (String edge : edges) {
      String[] parts = edge.trim().split("->");
      String from = parts[0].trim();
      String to = parts[1].trim();

      graph.putIfAbsent(from, new TreeSet<>());
      graph.putIfAbsent(to, new TreeSet<>());

      // добавляем ребро, избегая дубликатов
      if (graph.get(from).add(to)) {
        inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
      }

      inDegree.putIfAbsent(from, inDegree.getOrDefault(from, 0));
    }

    // Очередь с лексикографическим порядком
    PriorityQueue<String> queue = new PriorityQueue<>();
    for (String v : inDegree.keySet()) {
      if (inDegree.get(v) == 0) {
        queue.add(v);
      }
    }

    List<String> result = new ArrayList<>();

    while (!queue.isEmpty()) {
      String v = queue.poll();
      result.add(v);

      for (String neighbor : graph.get(v)) {
        inDegree.put(neighbor, inDegree.get(neighbor) - 1);
        if (inDegree.get(neighbor) == 0) {
          queue.add(neighbor);
        }
      }
    }

    // Вывод
    System.out.println(String.join(" ", result));
  }
}
