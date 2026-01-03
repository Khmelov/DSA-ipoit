package by.it.group410971.usovskiy.lesson14;

import java.util.Scanner;

public class PointsA {

    // ===== DSU (Disjoint Set Union) с эвристикой по размеру =====
    private static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size   = new int[n];
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
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;

            // подвешиваем меньший кластер к большему
            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int D = sc.nextInt();          // distance из теста
        if (!sc.hasNextInt()) return;
        int n = sc.nextInt();          // число точек

        int[] x = new int[n];
        int[] y = new int[n];
        int[] z = new int[n];

        for (int i = 0; i < n; i++) {
            x[i] = sc.nextInt();
            y[i] = sc.nextInt();
            z[i] = sc.nextInt();
        }

        DSU dsu = new DSU(n);
        long D2 = 1L * D * D;          // сравниваем по квадрату расстояния

        // объединяем точки, если sqrt(dx^2+dy^2+dz^2) < D
        // эквивалентно dx^2+dy^2+dz^2 < D^2 (D >= 0)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = x[i] - x[j];
                long dy = y[i] - y[j];
                long dz = z[i] - z[j];
                long dist2 = dx * dx + dy * dy + dz * dz;

                if (dist2 < D2) {
                    dsu.union(i, j);
                }
            }
        }

        // считаем размеры компонент
        int[] rawSizes = new int[n];
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            rawSizes[root]++;
        }

        // собираем только положительные размеры
        int comps = 0;
        for (int sz : rawSizes) {
            if (sz > 0) comps++;
        }
        int[] sizes = new int[comps];
        int k = 0;
        for (int sz : rawSizes) {
            if (sz > 0) sizes[k++] = sz;
        }

        // сортировка размеров по убыванию
        for (int i = 0; i < sizes.length - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < sizes.length; j++) {
                if (sizes[j] > sizes[maxIdx]) {
                    maxIdx = j;
                }
            }
            int tmp = sizes[i];
            sizes[i] = sizes[maxIdx];
            sizes[maxIdx] = tmp;
        }

         StringBuilder out = new StringBuilder();
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) out.append(' ');
            out.append(sizes[i]);
        }
        System.out.println(out);
    }
}
