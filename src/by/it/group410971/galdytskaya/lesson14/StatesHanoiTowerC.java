package by.it.group410971.galdytskaya.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static final int MAX_N = 20;
    private static final int MAX_STEPS = (1 << MAX_N) - 1;

    private static int[] parent;
    private static int[] size;

    private static int[] maxHeightPerStep;

    private static int stepCount;

    private static int hA;
    private static int hB;
    private static int hC;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.close();

        int totalSteps = (1 << N) - 1;

        parent = new int[totalSteps];
        size = new int[totalSteps];
        maxHeightPerStep = new int[totalSteps];

        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        hA = N;
        hB = 0;
        hC = 0;
        stepCount = 0;

        solveHanoi(N, 'A', 'B', 'C');

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

        boolean[] isRoot = new boolean[stepCount];
        int distinctCount = 0;
        for (int i = 0; i < stepCount; i++) {
            int r = find(i);
            if (!isRoot[r]) {
                isRoot[r] = true;
                distinctCount++;
            }
        }

        int[] groups = new int[distinctCount];
        int idx = 0;
        for (int i = 0; i < stepCount; i++) {
            int r = find(i);
            if (isRoot[r]) {
                groups[idx++] = size[r];
                isRoot[r] = false;
            }
        }

        for (int i = 1; i < groups.length; i++) {
            int key = groups[i];
            int j = i - 1;
            while (j >= 0 && groups[j] > key) {
                groups[j + 1] = groups[j];
                j--;
            }
            groups[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < groups.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(groups[i]);
        }
        System.out.println(sb.toString());
    }

    private static void solveHanoi(int n, char from, char to, char aux) {
        if (n == 0) return;
        solveHanoi(n - 1, from, aux, to);
        moveDisk(from, to);
        solveHanoi(n - 1, aux, to, from);
    }

    private static void moveDisk(char from, char to) {
        if (from == 'A') hA--;
        else if (from == 'B') hB--;
        else hC--;

        if (to == 'A') hA++;
        else if (to == 'B') hB++;
        else hC++;

        int maxH = hA;
        if (hB > maxH) maxH = hB;
        if (hC > maxH) maxH = hC;

        maxHeightPerStep[stepCount] = maxH;
        stepCount++;
    }

    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

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