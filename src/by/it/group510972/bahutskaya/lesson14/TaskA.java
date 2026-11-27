package by.it.group510972.bahutskaya.lesson14;

import java.util.Scanner;

// Близкие точки собираем в группы и печатаем их размеры.
public class TaskA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distance = scanner.nextInt();
        int n = scanner.nextInt();
        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }
        Dsu dsu = new Dsu(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Если точки близко, объединяем их группы
                if (close(points[i], points[j], distance)) {
                    dsu.union(i, j);
                }
            }
        }
        printSizes(dsu.sizes());
    }

    private static boolean close(int[] left, int[] right, int distance) {
        return Math.hypot(Math.hypot(left[0] - right[0], left[1] - right[1]), left[2] - right[2]) < distance;
    }

    static void printSizes(int[] sizes) {
        java.util.Arrays.sort(sizes);
        for (int i = sizes.length - 1; i >= 0; i--) {
            if (i < sizes.length - 1) {
                System.out.print(' ');
            }
            System.out.print(sizes[i]);
        }
        System.out.println();
    }

    static final class Dsu {
        private final int[] parent;
        private final int[] size;

        Dsu(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int value) {
            if (parent[value] != value) {
                parent[value] = find(parent[value]);
            }
            return parent[value];
        }

        void union(int left, int right) {
            int a = find(left);
            int b = find(right);
            if (a == b) {
                return;
            }
            if (size[a] < size[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        int[] sizes() {
            int count = 0;
            for (int i = 0; i < parent.length; i++) {
                if (find(i) == i) {
                    count++;
                }
            }
            int[] result = new int[count];
            int index = 0;
            for (int i = 0; i < parent.length; i++) {
                if (find(i) == i) {
                    result[index++] = size[i];
                }
            }
            return result;
        }
    }
}
