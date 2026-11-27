package by.it.group510972.bahutskaya.lesson14;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Сайты со ссылками друг на друга попадают в одну группу.
public class TaskB {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> index = new HashMap<>();
        java.util.ArrayList<int[]> edges = new java.util.ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("end") || line.isEmpty()) {
                if (line.equals("end")) {
                    break;
                }
                continue;
            }
            String[] parts = line.split("\\+");
            int left = id(index, parts[0]);
            int right = id(index, parts[1]);
            edges.add(new int[]{left, right});
        }
        TaskA.Dsu dsu = new TaskA.Dsu(index.size());
        // Сайты из одной строки объединяем
        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }
        TaskA.printSizes(dsu.sizes());
    }

    private static int id(Map<String, Integer> index, String site) {
        Integer value = index.get(site);
        if (value == null) {
            value = index.size();
            index.put(site, value);
        }
        return value;
    }
}
