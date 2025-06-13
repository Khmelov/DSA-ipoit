package by.it.group410971.tishuk.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        int n = scanner.nextInt();
        int[] m = new int[n];

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int[] dp = new int[n];       // длины подпоследовательностей
        int[] prev = new int[n];     // для восстановления пути

        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);

        int maxLen = 1;
        int lastIndex = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (m[j] >= m[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                lastIndex = i;
            }
        }

        // Восстановление индексов
        List<Integer> indices = new ArrayList<>();
        while (lastIndex != -1) {
            indices.add(lastIndex + 1); // индекс с 1
            lastIndex = prev[lastIndex];
        }

        Collections.reverse(indices);

        System.out.println(maxLen);
        for (int index : indices) {
            System.out.print(index + " ");
        }

        return maxLen;
    }
}
