package by.it.a_khmelev.lesson13;

import java.util.*;

public class GraphB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();
        scanner.close();
        
        // Парсинг графа
        Map<Integer, List<Integer>> graph = parseGraph(input);
        
        // Проверка на наличие циклов
        boolean hasCycle = hasCycle(graph);
        
        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }
    
    private static Map<Integer, List<Integer>> parseGraph(String input) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        
        // Если строка пустая, возвращаем пустой граф
        if (input.isEmpty()) {
            return graph;
        }
        
        // Разделяем по запятой для получения списка рёбер
        String[] edges = input.split(", ");
        
        for (String edge : edges) {
            edge = edge.trim();
            
            // Разделяем на вершину-источник и вершину-назначение
            String[] parts = edge.split(" -> ");
            
            if (parts.length == 2) {
                int from = Integer.parseInt(parts[0].trim());
                int to = Integer.parseInt(parts[1].trim());
                
                // Добавляем ребро в граф
                graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
                
                // Убедимся, что обе вершины есть в графе (даже если у них нет исходящих рёбер)
                graph.putIfAbsent(to, new ArrayList<>());
            }
        }
        
        return graph;
    }
    
    private static boolean hasCycle(Map<Integer, List<Integer>> graph) {
        // Используем алгоритм поиска в глубину с тремя цветами:
        // 0 - не посещена (WHITE)
        // 1 - в процессе посещения (GRAY)
        // 2 - полностью обработана (BLACK)
        
        Map<Integer, Integer> visited = new HashMap<>();
        
        // Инициализируем все вершины как не посещенные
        for (Integer vertex : graph.keySet()) {
            visited.put(vertex, 0);
        }
        
        // Проверяем каждую вершину
        for (Integer vertex : graph.keySet()) {
            if (visited.get(vertex) == 0) { // Не посещена
                if (dfsHasCycle(vertex, graph, visited)) {
                    return true; // Найден цикл
                }
            }
        }
        
        return false; // Циклов нет
    }
    
    private static boolean dfsHasCycle(Integer vertex, Map<Integer, List<Integer>> graph, 
                                      Map<Integer, Integer> visited) {
        // Помечаем вершину как обрабатываемую
        visited.put(vertex, 1);
        
        // Проверяем всех соседей
        if (graph.containsKey(vertex)) {
            for (Integer neighbor : graph.get(vertex)) {
                if (!graph.containsKey(neighbor)) {
                    continue; // Вершина может быть указана только как назначение
                }
                
                if (visited.get(neighbor) == 1) {
                    // Если сосед находится в процессе обработки, это обратное ребро - цикл
                    return true;
                }
                
                if (visited.get(neighbor) == 0) {
                    // Если сосед не посещен, рекурсивно проверяем его
                    if (dfsHasCycle(neighbor, graph, visited)) {
                        return true;
                    }
                }
            }
        }
        
        // Помечаем вершину как полностью обработанную
        visited.put(vertex, 2);
        return false;
    }
}