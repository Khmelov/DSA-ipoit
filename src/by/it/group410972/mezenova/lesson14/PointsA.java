package by.it.group410972.mezenova.lesson14;

import java.util.*;

public class PointsA {

    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double distanceTo(Point other) {
            long dx = this.x - other.x;
            long dy = this.y - other.y;
            long dz = this.z - other.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

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

        int D = scanner.nextInt();
        int N = scanner.nextInt();

        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point(x, y, z);
        }

        DSU dsu = new DSU(N);

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (points[i].distanceTo(points[j]) < D) {
                    dsu.union(i, j);
                }
            }
        }

        boolean[] visited = new boolean[N];
        List<Integer> clusterSizes = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                clusterSizes.add(dsu.getSize(i));
            }
        }

        Collections.sort(clusterSizes, Collections.reverseOrder());


        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}