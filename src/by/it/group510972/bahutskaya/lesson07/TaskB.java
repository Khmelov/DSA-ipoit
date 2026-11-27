package by.it.group510972.bahutskaya.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// То же расстояние между строками, только таблицей.

public class TaskB {

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        // Заполняем таблицу: цена превратить кусок одной строки в кусок другой
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        dp[i - 1][j - 1] + cost,
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                );
            }
        }
        return dp[n][m];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskB.class.getResourceAsStream("dataABC.txt");
        TaskB instance = new TaskB();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}
