package by.it.a_khmelev.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    static class DSU {
        private int[] parent;
        private int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
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
        int N = scanner.nextInt();

        // Количество шагов без начального состояния: 2^N - 1
        int totalSteps = (1 << N) - 1;

        if (totalSteps == 0) {
            System.out.println();
            return;
        }

        // Массив для максимальных высот на каждом шаге
        int[] maxHeights = new int[totalSteps];

        // Симулируем, начиная с первого хода (не сохраняем начальное состояние)
        int[] heights = {N, 0, 0}; // Начальное состояние
        int[] stepCounter = {0};

        // Запускаем рекурсию
        solveHanoi(N, 0, 1, 2, heights, maxHeights, stepCounter);

        // Создаем DSU
        DSU dsu = new DSU(totalSteps);

        // Объединяем шаги с одинаковой максимальной высотой
        int[] firstStepWithHeight = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            firstStepWithHeight[i] = -1;
        }

        for (int i = 0; i < totalSteps; i++) {
            int h = maxHeights[i];
            if (firstStepWithHeight[h] == -1) {
                firstStepWithHeight[h] = i;
            } else {
                dsu.union(firstStepWithHeight[h], i);
            }
        }

        // Собираем размеры групп
        int[] groupSizes = new int[N + 1];
        boolean[] processed = new boolean[totalSteps];

        for (int i = 0; i < totalSteps; i++) {
            int root = dsu.find(i);
            if (!processed[root]) {
                processed[root] = true;
                int height = maxHeights[root];
                groupSizes[height] = dsu.getSize(root);
            }
        }

        // Собираем ненулевые размеры
        int count = 0;
        for (int i = 1; i <= N; i++) {
            if (groupSizes[i] > 0) {
                count++;
            }
        }

        int[] sizes = new int[count];
        int index = 0;
        for (int i = 1; i <= N; i++) {
            if (groupSizes[i] > 0) {
                sizes[index++] = groupSizes[i];
            }
        }

        // Сортируем
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (sizes[j] > sizes[j + 1]) {
                    int temp = sizes[j];
                    sizes[j] = sizes[j + 1];
                    sizes[j + 1] = temp;
                }
            }
        }

        // Выводим
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes[i]);
        }
    }

    static void solveHanoi(int n, int from, int to, int aux,
                           int[] heights, int[] maxHeights, int[] stepCounter) {
        if (n == 0) return;

        // Перемещаем n-1 диск
        solveHanoi(n - 1, from, aux, to, heights, maxHeights, stepCounter);

        // Перемещаем один диск (это и есть шаг)
        heights[from]--;
        heights[to]++;

        // Сохраняем максимальную высоту для этого шага
        int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));
        maxHeights[stepCounter[0]] = maxHeight;
        stepCounter[0]++;

        // Перемещаем n-1 диск обратно
        solveHanoi(n - 1, aux, to, from, heights, maxHeights, stepCounter);
    }
}