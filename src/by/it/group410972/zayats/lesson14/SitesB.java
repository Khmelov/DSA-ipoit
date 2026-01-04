package by.it.group410972.zayats.lesson14;

import java.util.*;

public class SitesB {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int x, int y) {
            int px = find(x);
            int py = find(y);
            if (px == py) return;

            // Union by size
            if (size[px] < size[py]) {
                parent[px] = py;
                size[py] += size[px];
            } else {
                parent[py] = px;
                size[px] += size[py];
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> siteId = new HashMap<>();
        List<String> sites = new ArrayList<>();
        List<int[]> pairs = new ArrayList<>();
        int id = 0;

        // Чтение ввода
        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] parts = line.split("\\+");
            for (String site : parts) {
                if (!siteId.containsKey(site)) {
                    siteId.put(site, id++);
                    sites.add(site);
                }
            }
            pairs.add(new int[]{siteId.get(parts[0]), siteId.get(parts[1])});
        }

        // Создаем DSU
        DSU dsu = new DSU(sites.size());

        // Объединяем пары сайтов
        for (int[] p : pairs) {
            dsu.union(p[0], p[1]);
        }

        // Подсчет размеров кластеров
        Map<Integer, Integer> clusterSizes = new HashMap<>();
        for (int i = 0; i < sites.size(); i++) {
            int root = dsu.find(i);
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Сортировка по убыванию
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());
        sizes.sort(Collections.reverseOrder());

        // Вывод
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}
