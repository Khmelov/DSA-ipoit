package by.it.a_khmelev.lesson14;

import java.util.HashMap;
import java.util.Scanner;

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
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, Integer> siteToIndex = new HashMap<>();
        int siteCount = 0;

        // Считываем все связи
        String line;
        String[] connections = new String[1000];
        int connectionCount = 0;

        while (!(line = scanner.nextLine()).equals("end")) {
            connections[connectionCount++] = line;
            String[] sites = line.split("\\+");

            for (String site : sites) {
                if (!siteToIndex.containsKey(site)) {
                    siteToIndex.put(site, siteCount++);
                }
            }
        }

        // Создаем DSU
        DSU dsu = new DSU(siteCount);

        // Обрабатываем связи
        for (int i = 0; i < connectionCount; i++) {
            String[] sites = connections[i].split("\\+");
            int index1 = siteToIndex.get(sites[0]);
            int index2 = siteToIndex.get(sites[1]);
            dsu.union(index1, index2);
        }

        // Собираем размеры кластеров
        boolean[] processed = new boolean[siteCount];
        int[] clusterSizes = new int[siteCount];
        int clusterCount = 0;

        for (int i = 0; i < siteCount; i++) {
            int root = dsu.find(i);
            if (!processed[root]) {
                processed[root] = true;
                clusterSizes[clusterCount++] = dsu.getSize(root);
            }
        }

        // Сортируем размеры кластеров по УБЫВАНИЮ
        for (int i = 0; i < clusterCount - 1; i++) {
            for (int j = 0; j < clusterCount - i - 1; j++) {
                if (clusterSizes[j] < clusterSizes[j + 1]) {  // Изменено на < для убывания
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }
            }
        }

        // Выводим результат
        for (int i = 0; i < clusterCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
    }
}