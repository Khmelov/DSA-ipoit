package by.it.group410972.zayats.lesson14;

import java.util.Scanner;

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

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]); // Path compression
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

    static class Point {
        int x, y, z;
        Point(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            points[i] = new Point(sc.nextInt(), sc.nextInt(), sc.nextInt());
        }

        DSU dsu = new DSU(N);

        // Объединяем точки, расстояние между которыми < D
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (distance(points[i], points[j]) < D - 1e-9) {
                    dsu.union(i, j);
                }
            }
        }

        // Собираем размеры кластеров
        int[] sizes = new int[N]; // Максимум N кластеров
        for (int i = 0; i < N; i++) {
            if (dsu.find(i) == i) sizes[i] = dsu.size[i];
        }

        // Считаем количество ненулевых кластеров
        int count = 0;
        for (int s : sizes) if (s > 0) count++;

        // Записываем в массив и сортируем по убыванию
        int[] result = new int[count];
        int idx = 0;
        for (int s : sizes) if (s > 0) result[idx++] = s;

        // Сортировка по убыванию
        for (int i = 0; i < result.length - 1; i++) {
            for (int j = i + 1; j < result.length; j++) {
                if (result[i] < result[j]) {
                    int tmp = result[i];
                    result[i] = result[j];
                    result[j] = tmp;
                }
            }
        }

        // Вывод
        for (int i = 0; i < result.length; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
    }

    private static double distance(Point a, Point b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        long dz = a.z - b.z;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
