package by.it.group410972.shelegeiko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_Knapsack {

    int getMaxWeight(InputStream stream ) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] будет хранить максимальный вес для рюкзака вместимостью i
        int[] dp = new int[W + 1];

        // Перебираем каждый слиток
        for (int i = 0; i < n; i++) {
            // Для каждого слитка обновляем массив dp в обратном порядке
            // чтобы не использовать один и тот же слиток дважды.
            for (int j = W; j >= gold[i]; j--) {
                // Выбираем: взять текущий слиток или нет.
                // dp[j] - не берем слиток
                // dp[j - gold[i]] + gold[i] - берем слиток
                dp[j] = Math.max(dp[j], dp[j - gold[i]] + gold[i]);
            }
        }

        // Результат для полной вместимости W находится в последней ячейке
        int result = dp[W];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_Knapsack.class.getResourceAsStream("dataB.txt");
        B_Knapsack instance = new B_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}