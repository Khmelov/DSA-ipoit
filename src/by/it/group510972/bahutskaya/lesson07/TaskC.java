package by.it.group510972.bahutskaya.lesson07;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Не только число правок, но и сами шаги: заменить, вставить, удалить или оставить.

public class TaskC {

    String getDistanceEdinting(String one, String two) {
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
        // Идём из угла таблицы назад и записываем каждую правку
        java.util.ArrayList<String> steps = new java.util.ArrayList<>();
        int i = n;
        int j = m;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && one.charAt(i - 1) == two.charAt(j - 1) && dp[i][j] == dp[i - 1][j - 1]) {
                steps.add("#,");
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                steps.add("~" + two.charAt(j - 1) + ",");
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                steps.add("-" + one.charAt(i - 1) + ",");
                i--;
            } else {
                steps.add("+" + two.charAt(j - 1) + ",");
                j--;
            }
        }
        StringBuilder result = new StringBuilder();
        for (int k = steps.size() - 1; k >= 0; k--) {
            result.append(steps.get(k));
        }
        return result.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskC.class.getResourceAsStream("dataABC.txt");
        TaskC instance = new TaskC();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}
