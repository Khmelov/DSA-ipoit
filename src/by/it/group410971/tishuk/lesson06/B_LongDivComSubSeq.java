package by.it.group410971.tishuk.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_LongDivComSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // читаем количество элементов
        int n = scanner.nextInt();
        int[] m = new int[n];

        // читаем массив
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        // dp[i] — максимальная длина кратной подпоследовательности, заканчивающейся на m[i]
        int[] dp = new int[n];
        int result = 0;

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // каждый элемент сам по себе — подпоследовательность длины 1
            for (int j = 0; j < i; j++) {
                if (m[i] % m[j] == 0) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            result = Math.max(result, dp[i]); // обновляем глобальный максимум
        }

        return result;
    }
}
