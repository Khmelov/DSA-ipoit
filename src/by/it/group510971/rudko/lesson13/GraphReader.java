package by.it.group510971.rudko.lesson13;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

//Чтение орграфа вида "0 -> 2, 1 -> 3" (или "1->2,2->3"); разделители - запятая или перевод строки.
//Каждая вершина присутствует в карте, даже если у нее нет исходящих дуг.
class GraphReader {

    static Map<String, List<String>> read(Scanner scanner) {
        StringBuilder text = new StringBuilder();
        while (scanner.hasNextLine()) {
            text.append(scanner.nextLine()).append(',');
        }
        Map<String, List<String>> graph = new TreeMap<>();
        for (String part : text.toString().split("[,\\n]")) {
            String edge = part.trim();
            if (edge.isEmpty()) continue;
            String[] ends = edge.split("->");
            String from = ends[0].trim();
            String to = ends[1].trim();
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            graph.computeIfAbsent(to, k -> new ArrayList<>());
        }
        return graph;
    }
}
