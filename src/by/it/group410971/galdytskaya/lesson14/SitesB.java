package by.it.group410971.galdytskaya.lesson14;

import java.util.*;

public class SitesB {

    private static class DSU {
        int[] parent, rank, size;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a != b) {
                if (rank[a] < rank[b]) {
                    parent[a] = b;
                    size[b] += size[a];
                } else {
                    parent[b] = a;
                    size[a] += size[b];
                    if (rank[a] == rank[b]) {
                        rank[a]++;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Integer> siteToId = new HashMap<>();
        int idCounter = 0;
        List<String[]> pairs = new ArrayList<>();

        // собираю все сайты и пары
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if ("end".equals(line)) {
                break;
            }
            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                pairs.add(parts);
                String site1 = parts[0].trim();
                String site2 = parts[1].trim();
                siteToId.putIfAbsent(site1, idCounter++);
                siteToId.putIfAbsent(site2, idCounter++);
            }
        }

        // создаю DSU
        DSU dsu = new DSU(idCounter);

        // объединяю пары
        for (String[] pair : pairs) {
            String site1 = pair[0].trim();
            String site2 = pair[1].trim();
            int id1 = siteToId.get(site1);
            int id2 = siteToId.get(site2);
            dsu.union(id1, id2);
        }

        // собираю размеры кластеров из DSU.size
        int[] sizes = new int[idCounter];
        int sizeCount = 0;
        for (int i = 0; i < idCounter; i++) {
            if (dsu.find(i) == i) { // только корни
                sizes[sizeCount++] = dsu.size[i];
            }
        }

        quickSortDesc(sizes, 0, sizeCount - 1);

        for (int i = 0; i < sizeCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes[i]);
        }
        System.out.println();
    }

    private static void quickSortDesc(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partitionDesc(arr, low, high);
            quickSortDesc(arr, low, pi - 1);
            quickSortDesc(arr, pi + 1, high);
        }
    }

    private static int partitionDesc(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] >= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}
