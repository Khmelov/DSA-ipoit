package by.it.group410972.ivanovich.lesson14;

import java.util.*;

public class PointsA {

    static class Point3D {
        int x, y, z;

        Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        double distanceTo(Point3D other) {
            long dx = this.x - other.x;
            long dy = this.y - other.y;
            long dz = this.z - other.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

    static class DSU {
        int[] parent;
        int[] rank;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
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

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
                rank[rootX]++;
            }
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение D и N
        int D = scanner.nextInt();
        int N = scanner.nextInt();

        // Чтение точек
        Point3D[] points = new Point3D[N];
        for (int i = 0; i < N; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point3D(x, y, z);
        }

        // Создание DSU
        DSU dsu = new DSU(N);

        // Объединение точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < D) {
                    dsu.union(i, j);
                }
            }
        }

        // Сбор размеров кластеров
        boolean[] visited = new boolean[N];
        List<Integer> sizes = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                sizes.add(dsu.getSize(i));
            }
        }

        // Сортировка по убыванию (как в тесте)
        Collections.sort(sizes);
        // В тесте ожидается сортировка по убыванию: "60 2 1"
        // Но сортируем по возрастанию и выводим в обратном порядке

        // Вывод результата в порядке убывания
        for (int i = sizes.size() - 1; i >= 0; i--) {
            if (i < sizes.size() - 1) {
                System.out.print(" ");
            }
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}