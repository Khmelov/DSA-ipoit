package by.it.group410972.sheshtakauskas.lesson01.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    static long[] sizes;
    static int maxHeight;

    static void hanoi(int n, int from, int to, int aux, int[] counts) {
        if (n == 0) return;

        hanoi(n - 1, from, aux, to, counts);

        counts[from]--;
        counts[to]++;
        int h = Math.max(Math.max(counts[0], counts[1]), counts[2]);
        sizes[h]++;

        hanoi(n - 1, aux, to, from, counts);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        maxHeight = N;
        sizes = new long[N + 1];
        int[] counts = new int[]{N, 0, 0};

        hanoi(N, 0, 1, 2, counts);

        long[] result = new long[N + 1];
        int k = 0;
        for (int i = 1; i <= N; i++) {
            if (sizes[i] > 0) result[k++] = sizes[i];
        }

        for (int i = 0; i < k; i++) {
            for (int j = i + 1; j < k; j++) {
                if (result[i] > result[j]) {
                    long tmp = result[i];
                    result[i] = result[j];
                    result[j] = tmp;
                }
            }
        }

        for (int i = 0; i < k; i++) {
            System.out.print(result[i]);
            if (i < k - 1) System.out.print(" ");
        }
    }
}
