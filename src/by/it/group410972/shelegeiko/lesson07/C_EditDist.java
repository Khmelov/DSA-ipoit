package by.it.group410972.shelegeiko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();

        // 1. Создаем и заполняем DP-таблицу, как в задаче B
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
                int deletion = dp[i - 1][j] + 1;
                int insertion = dp[i][j - 1] + 1;
                int substitution = dp[i - 1][j - 1] + cost;
                dp[i][j] = Math.min(Math.min(deletion, insertion), substitution);
            }
        }

        // 2. Восстанавливаем путь (обратный ход)
        StringBuilder resultBuilder = new StringBuilder();
        int i = n;
        int j = m;

        while (i > 0 || j > 0) {
            int cost = -1; // -1 как флаг, что cost еще не вычислен
            if (i > 0 && j > 0) {
                cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
            }

            // Приоритет отдаем диагональному движению (замена/совпадение),
            // затем удалению, затем вставке.
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + cost) {
                if (cost == 0) {
                    resultBuilder.insert(0, "#,");
                } else {
                    resultBuilder.insert(0, "~" + two.charAt(j - 1) + ",");
                }
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) { // Движение вверх -> Удаление
                resultBuilder.insert(0, "-" + one.charAt(i - 1) + ",");
                i--;
            } else { // Движение влево -> Вставка
                resultBuilder.insert(0, "+" + two.charAt(j - 1) + ",");
                j--;
            }
        }

        String result = resultBuilder.toString();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}