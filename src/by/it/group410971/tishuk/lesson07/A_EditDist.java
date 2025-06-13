package by.it.group410971.tishuk.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        int len1 = one.length();
        int len2 = two.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Базовые случаи
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i; // удаление всех символов из one
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j; // вставка всех символов из two
        }

        // Основная таблица
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,     // удаление
                                dp[i][j - 1] + 1),    // вставка
                        dp[i - 1][j - 1] + cost         // замена или совпадение
                );
            }
        }

        return dp[len1][len2];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);

        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // ab, ab
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // short, ports
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine())); // distance, editing
    }
}
