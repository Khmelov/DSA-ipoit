package by.it.group410971.galdytskaya.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    // Максимальное N можно оценить по условию (2^N - 1 шагов).
    // Для безопасности возьмем N до 20: 2^20 - 1 = 1_048_575 шагов.
    // Если в тестах N меньше, всё равно хватит.
    private static final int MAX_N = 20;
    private static final int MAX_STEPS = (1 << MAX_N) - 1;

    // DSU
    private static int[] parent;
    private static int[] size;

    // Для хранения max высоты на каждом шаге
    private static int[] maxHeightPerStep;

    // Текущее количество шагов
    private static int stepCount;

    // Стержни: массивы дисков (храним просто количество дисков на стержне)
    private static int hA;
    private static int hB;
    private static int hC;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.close();

        // Общее количество шагов в задаче Ханойских башен
        int totalSteps = (1 << N) - 1;

        parent = new int[totalSteps];
        size = new int[totalSteps];
        maxHeightPerStep = new int[totalSteps];

        // Инициализация DSU
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Начальное состояние: все диски на A
        hA = N;
        hB = 0;
        hC = 0;
        stepCount = 0;

        // Запускаем рекурсивное решение Ханойских башен:
        // переносим N дисков с A на B, используя C как вспомогательный.
        solveHanoi(N, 'A', 'B', 'C');

        // Теперь у нас есть maxHeightPerStep[0..stepCount-1].
        // Нужно сгруппировать шаги по одинаковому maxHeight.
        // Для каждого значения maxH от 1 до N объединим все шаги с таким maxH.
        for (int h = 1; h <= N; h++) {
            int firstIndex = -1;
            for (int i = 0; i < stepCount; i++) {
                if (maxHeightPerStep[i] == h) {
                    if (firstIndex == -1) {
                        firstIndex = i;
                    } else {
                        union(firstIndex, i);
                    }
                }
            }
        }

        // Подсчёт размеров всех различных множеств
        // Используем временный массив для отметки корней
        boolean[] isRoot = new boolean[stepCount];
        int distinctCount = 0;
        for (int i = 0; i < stepCount; i++) {
            int r = find(i);
            if (!isRoot[r]) {
                isRoot[r] = true;
                distinctCount++;
            }
        }

        // Собираем размеры всех множеств
        int[] groups = new int[distinctCount];
        int idx = 0;
        for (int i = 0; i < stepCount; i++) {
            int r = find(i);
            if (isRoot[r]) {
                groups[idx++] = size[r];
                isRoot[r] = false; // чтобы не считать дважды
            }
        }

        // Сортируем размеры по возрастанию (обычная сортировка вставками)
        for (int i = 1; i < groups.length; i++) {
            int key = groups[i];
            int j = i - 1;
            while (j >= 0 && groups[j] > key) {
                groups[j + 1] = groups[j];
                j--;
            }
            groups[j + 1] = key;
        }

        // Вывод
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < groups.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(groups[i]);
        }
        System.out.println(sb.toString());
    }

    // Рекурсивное решение Ханойских башен
    private static void solveHanoi(int n, char from, char to, char aux) {
        if (n == 0) return;
        solveHanoi(n - 1, from, aux, to);
        moveDisk(from, to);
        solveHanoi(n - 1, aux, to, from);
    }

    // Перемещение одного диска между стержнями
    private static void moveDisk(char from, char to) {
        // Обновляем высоты стержней
        if (from == 'A') hA--;
        else if (from == 'B') hB--;
        else hC--;

        if (to == 'A') hA++;
        else if (to == 'B') hB++;
        else hC++;

        // Запоминаем max высоту после этого шага
        int maxH = hA;
        if (hB > maxH) maxH = hB;
        if (hC > maxH) maxH = hC;

        maxHeightPerStep[stepCount] = maxH;
        stepCount++;
    }

    // DSU: поиск с сжатием пути
    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    // DSU: объединение по размеру
    private static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;
        if (size[ra] < size[rb]) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }
}