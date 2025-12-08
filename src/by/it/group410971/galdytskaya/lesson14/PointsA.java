package by.it.group410971.galdytskaya.lesson14;

import java.util.*;

public class PointsA {
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

        int find(int a) {
            if (parent[a] != a) {
                parent[a] = find(parent[a]);
            }
            return parent[a];
        }

        void union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA != rootB) {
                if (size[rootA] < size[rootB]) {
                    int temp = rootA;
                    rootA = rootB;
                    rootB = temp;
                }
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }

        List<Integer> getClusterSizes() {
            List<Integer> res = new ArrayList<>();
            for (int i = 0; i < parent.length; i++) {
                if (parent[i] == i && size[i] > 0) {
                    res.add(size[i]);
                }
            }
            res.sort(Collections.reverseOrder());
            return res;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double D = scanner.nextDouble();
        int N = scanner.nextInt();
        int[][] points = new int[N][3];
        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (distance(points[i], points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        List<Integer> clusters = dsu.getClusterSizes();
        for (int i = 0; i < clusters.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusters.get(i));
        }
        System.out.println();
    }

    private static double distance(int[] p1, int[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        double dz = p1[2] - p2[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
