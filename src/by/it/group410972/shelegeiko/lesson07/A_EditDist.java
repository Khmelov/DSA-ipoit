package by.it.group410972.shelegeiko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        int n = one.length();
        int m = two.length();

        // dp[i][j] будет хранить расстояние между первыми i символами строки one
        // и первыми j символами строки two
        int[][] dp = new int[n + 1][m + 1];

        // Инициализация базовых случаев
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i; // Стоимость удаления i символов, чтобы получить пустую строку
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j; // Стоимость вставки j символов в пустую строку
        }

        // Заполняем остальную часть таблицы
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Стоимость замены: 0, если символы равны, 1 - если нет
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                // Выбираем минимум из трех операций: вставка, удаление, замена
                int insertion = dp[i][j - 1] + 1;
                int deletion = dp[i - 1][j] + 1;
                int substitution = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(Math.min(insertion, deletion), substitution);
            }
        }

        // Результат находится в правом нижнем углу
        int result = dp[n][m];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}