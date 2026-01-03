package by.it.group410971.usovskiy.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class GraphC {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        // Граф и транспонированный граф
        Map<String, List<String>> g = new HashMap<>();
        Map<String, List<String>> gr = new HashMap<>();
        Set<String> vertices = new HashSet<>();

        // --- Парсинг рёбер ---
        String[] edges = line.split(",");
        for (String edge : edges) {
            edge = edge.trim();
            if (edge.isEmpty()) continue;

            String[] uv = edge.split("->");
            if (uv.length != 2) continue;

            String from = uv[0].trim();
            String to = uv[1].trim();

            vertices.add(from);
            vertices.add(to);

            g.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            gr.computeIfAbsent(to, k -> new ArrayList<>()).add(from);

            // чтобы вершины без исходящих/входящих тоже были как ключи
            g.putIfAbsent(to, g.getOrDefault(to, null));
            gr.putIfAbsent(from, gr.getOrDefault(from, null));
        }

        // сортируем списки смежности для детерминированного/лексикографического обхода
        for (List<String> list : g.values()) {
            if (list != null) Collections.sort(list);
        }
        for (List<String> list : gr.values()) {
            if (list != null) Collections.sort(list);
        }

        // =============== Kosaraju: первый проход ===============
        List<String> all = new ArrayList<>(vertices);
        Collections.sort(all);                   // лексикографический порядок вершин
        Set<String> used = new HashSet<>();
        List<String> order = new ArrayList<>();

        for (String v : all) {
            if (!used.contains(v)) {
                dfs1(v, g, used, order);
            }
        }

        // =============== Второй проход на транспонированном графе ===============
        Set<String> used2 = new HashSet<>();
        List<List<String>> components = new ArrayList<>();

        for (int i = order.size() - 1; i >= 0; i--) {
            String v = order.get(i);
            if (!used2.contains(v)) {
                List<String> comp = new ArrayList<>();
                dfs2(v, gr, used2, comp);
                Collections.sort(comp);          // вершины внутри компоненты по алфавиту
                components.add(comp);            // порядок компонент — от истока к стоку
            }
        }

        // =============== Вывод компонент сильной связности ===============
        for (List<String> comp : components) {
            StringBuilder sb = new StringBuilder();
            for (String v : comp) sb.append(v);
            System.out.println(sb.toString());
        }
    }

    // DFS по исходному графу: собираем порядок выхода вершин
    private static void dfs1(String v,
                             Map<String, List<String>> g,
                             Set<String> used,
                             List<String> order) {
        used.add(v);
        List<String> neigh = g.get(v);
        if (neigh != null) {
            for (String u : neigh) {
                if (!used.contains(u)) dfs1(u, g, used, order);
            }
        }
        order.add(v);
    }

    // DFS по транспонированному графу: собираем одну КСС
    private static void dfs2(String v,
                             Map<String, List<String>> gr,
                             Set<String> used,
                             List<String> comp) {
        used.add(v);
        comp.add(v);
        List<String> neigh = gr.get(v);
        if (neigh != null) {
            for (String u : neigh) {
                if (!used.contains(u)) dfs2(u, gr, used, comp);
            }
        }
    }
}
