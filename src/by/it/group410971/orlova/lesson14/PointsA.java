package by.it.a_khmelev.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение расстояния D и числа точек N
        double D = scanner.nextDouble();
        int N = scanner.nextInt();

        // Читаем D^2 для сравнения без квадратного корня
        double DSquared = D * D;

        // Массив для хранения точек
        double[][] points = new double[N][3];

        // Чтение координат точек
        for (int i = 0; i < N; i++) {
            points[i][0] = scanner.nextDouble(); // X
            points[i][1] = scanner.nextDouble(); // Y
            points[i][2] = scanner.nextDouble(); // Z
        }

        scanner.close();

        // Создаем DSU
        DSU dsu = new DSU(N);

        // Объединяем точки, если квадрат расстояния между ними меньше D^2
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (distanceSquared(points[i], points[j]) < DSquared) {
                    dsu.union(i, j);
                }
            }
        }

        // Получаем размеры кластеров
        List<Integer> clusterSizes = dsu.getClusterSizes();

        // Сортируем размеры кластеров по УБЫВАНИЮ
        clusterSizes.sort((a, b) -> b - a);

        // Выводим результат
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            if (i < clusterSizes.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    // Вычисляем квадрат расстояния (оптимизация - избегаем sqrt)
    private static double distanceSquared(double[] p1, double[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        double dz = p1[2] - p2[2];
        return dx * dx + dy * dy + dz * dz;
    }

    static class DSU {
        private int[] parent;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];

            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            // Эвристика по размеру
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        public List<Integer> getClusterSizes() {
            // Создаем мапу для подсчета размеров кластеров
            Map<Integer, Integer> sizeMap = new HashMap<>();

            for (int i = 0; i < parent.length; i++) {
                int root = find(i);
                sizeMap.put(root, sizeMap.getOrDefault(root, 0) + 1);
            }

            return new ArrayList<>(sizeMap.values());
        }
    }
}