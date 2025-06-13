package by.it.group410972.shelegeiko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_Knapsack {

    int getMaxWeight(InputStream stream) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        Scanner scanner = new Scanner(stream);
        int W = scanner.nextInt(); // Вместимость рюкзака
        int n = scanner.nextInt(); // Количество типов слитков
        int[] gold = new int[n];
        for (int i = 0; i < n; i++) {
            gold[i] = scanner.nextInt();
        }

        // dp[i] будет хранить максимальный вес для рюкзака вместимостью i
        int[] dp = new int[W + 1];

        // Проходим по всем возможным вместимостям от 1 до W
        for (int i = 1; i <= W; i++) {
            // Для каждой вместимости i, перебираем все доступные слитки
            for (int j = 0; j < n; j++) {
                // Если слиток помещается в рюкзак вместимостью i
                if (gold[j] <= i) {
                    // Рассматриваем вариант: взять этот слиток.
                    // Общий вес будет равен весу слитка + максимальный вес,
                    // который можно было унести в рюкзаке с оставшейся вместимостью.
                    int potentialWeight = dp[i - gold[j]] + gold[j];

                    // Обновляем dp[i], если нашли лучший вариант
                    if (potentialWeight > dp[i]) {
                        dp[i] = potentialWeight;
                    }
                }
            }
        }

        // Результат для полной вместимости W находится в последней ячейке
        int result = dp[W];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_Knapsack.class.getResourceAsStream("dataA.txt");
        A_Knapsack instance = new A_Knapsack();
        int res = instance.getMaxWeight(stream);
        System.out.println(res);
    }
}