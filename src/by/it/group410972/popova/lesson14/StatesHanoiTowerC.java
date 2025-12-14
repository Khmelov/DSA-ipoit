package by.it.group410972.popova.lesson14;

import java.util.Scanner;

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

        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            parent[b] = a;
            size[a] += size[b];
        }
    }

    static int stepCount = 0;
    static int[] maxHeights;

    static void hanoi(int n, int from, int to, int aux, int[] rods) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to, rods);

        rods[to]++;
        rods[from]--;

        int max = Math.max(rods[0], Math.max(rods[1], rods[2]));
        maxHeights[stepCount++] = max;

        hanoi(n - 1, aux, to, from, rods);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        int totalSteps = (int) Math.pow(2, N) - 1;
        maxHeights = new int[totalSteps];

        int[] rods = new int[3];
        rods[0] = N; // все диски на A
        rods[1] = 0;
        rods[2] = 0;

        hanoi(N, 0, 1, 2, rods);

        DSU dsu = new DSU(totalSteps);
        for (int i = 0; i < totalSteps; i++) {
            for (int j = i + 1; j < totalSteps; j++) {
                if (maxHeights[i] == maxHeights[j]) {
                    dsu.union(i, j);
                }
            }
        }

        int[] clusterSizes = new int[totalSteps];
        boolean[] seen = new boolean[totalSteps];
        int clusterCount = 0;

        for (int i = 0; i < totalSteps; i++) {
            int root = dsu.find(i);
            if (!seen[root]) {
                seen[root] = true;
                clusterSizes[clusterCount++] = dsu.size[root];
            }
        }

        for (int i = 0; i < clusterCount - 1; i++) {
            for (int j = i + 1; j < clusterCount; j++) {
                if (clusterSizes[i] > clusterSizes[j]) {
                    int tmp = clusterSizes[i];
                    clusterSizes[i] = clusterSizes[j];
                    clusterSizes[j] = tmp;
                }
            }
        }

        for (int i = 0; i < clusterCount; i++) {
            System.out.print(clusterSizes[i]);
            if (i < clusterCount - 1) System.out.print(" ");
        }
    }
}
