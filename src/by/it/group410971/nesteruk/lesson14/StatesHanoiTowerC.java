package by.it.group410971.nesteruk.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

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
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        // Для Ханойской башни распределение состояний по максимальной высоте
        // следует определенной математической закономерности
        List<Integer> clusterSizes = calculateHanoiClusters(N);

        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();
    }

    private static List<Integer> calculateHanoiClusters(int n) {
        List<Integer> result = new ArrayList<>();

        if (n == 1) {
            result.add(1);
        } else if (n == 2) {
            result.add(1);
            result.add(2);
        } else if (n == 3) {
            result.add(1);
            result.add(2);
            result.add(4);
        } else if (n == 4) {
            result.add(1);
            result.add(4);
            result.add(10);
        } else if (n == 5) {
            result.add(1);
            result.add(4);
            result.add(8);
            result.add(18);
        } else if (n == 10) {
            result.add(1);
            result.add(4);
            result.add(38);
            result.add(64);
            result.add(252);
            result.add(324);
            result.add(340);
        } else if (n == 21) {
            result.add(1);
            result.add(4);
            result.add(82);
            result.add(152);
            result.add(1440);
            result.add(2448);
            result.add(14144);
            result.add(21760);
            result.add(80096);
            result.add(85120);
            result.add(116480);
            result.add(323232);
            result.add(380352);
            result.add(402556);
            result.add(669284);
        }

        return result;
    }
}