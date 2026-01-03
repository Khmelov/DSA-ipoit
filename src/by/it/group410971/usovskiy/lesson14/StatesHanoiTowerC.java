package by.it.group410971.usovskiy.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    // ===== DSU (Disjoint Set Union) с эвристикой по размеру + сжатие путей =====
    private static int[] parent;
    private static int[] size;

    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // сжатие пути
        }
        return parent[x];
    }

    private static void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return;

        // эвристика по размеру множества
        if (size[ra] < size[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
    }

    // ===== состояние башен =====
    // стержни: 0 = A, 1 = B, 2 = C
    private static int[] h = new int[3];   // высоты на A,B,C
    private static int[] repByMaxH;        // представитель кластера для каждого h_max
    private static int stepIndex = 0;      // номер шага (состояния после хода)

    private static void recordState() {
        // наибольшая высота трёх пирамид
        int maxH = h[0];
        if (h[1] > maxH) maxH = h[1];
        if (h[2] > maxH) maxH = h[2];

        int idx = stepIndex++;
        parent[idx] = idx;
        size[idx] = 1;

        int rep = repByMaxH[maxH];
        if (rep == -1) {
            repByMaxH[maxH] = idx;    // первый шаг с таким maxH
        } else {
            union(idx, rep);          // объединяем с уже существующим кластером
        }
    }

    // классическое рекурсивное решение Ханойских башен
    private static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;
        hanoi(n - 1, from, aux, to);

        // перенести диск n: обновляем только высоты башен
        h[from]--;
        h[to]++;
        recordState(); // фиксируем состояние после хода

        hanoi(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int N = sc.nextInt();          // высота стартовой пирамиды на A

        if (N <= 0) {
            // нет ходов, нет кластеров
            return;
        }

        int totalSteps = (int) ((1L << N) - 1); // 2^N - 1 состояний (после каждого хода)

        parent = new int[totalSteps];
        size   = new int[totalSteps];

        // repByMaxH[h] = индекс первого шага с данным maxH, -1 если ещё не было
        repByMaxH = new int[N + 1]; // max высота от 1 до N
        Arrays.fill(repByMaxH, -1);

        // стартовое состояние: все N дисков на A
        h[0] = N;
        h[1] = 0;
        h[2] = 0;
        stepIndex = 0;

        // переносим пирамиду с A (0) на B (1) через C (2)
        hanoi(N, 0, 1, 2);

        // собираем размеры компонент (только по корням DSU)
        int[] clusterSizes = new int[totalSteps];
        int clusters = 0;
        for (int i = 0; i < stepIndex; i++) {
            if (parent[i] == i) {          // корень множества
                clusterSizes[clusters++] = size[i];
            }
        }

        // сортировка размеров по возрастанию
        Arrays.sort(clusterSizes, 0, clusters);

        // вывод
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < clusters; i++) {
            if (i > 0) out.append(' ');
            out.append(clusterSizes[i]);
        }
        System.out.println(out.toString());
    }
}
