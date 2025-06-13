package by.it.group410972.shelegeiko.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] stairs = new int[n];
        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Если ступенек нет, сумма 0
        if (n == 0) {
            return 0;
        }

        // Если ступенька одна, ответ - ее стоимость
        if (n == 1) {
            return stairs[0];
        }

        // Вместо целого массива dp[n] будем использовать две переменные,
        // хранящие максимальные суммы для двух предыдущих шагов.

        // prev2 - максимальная сумма на i-2 шаге (изначально "земля", сумма 0)
        int prev2 = 0;
        // prev1 - максимальная сумма на i-1 шаге (изначально первая ступенька)
        int prev1 = stairs[0];

        // Начинаем со второй ступеньки (индекс 1)
        for (int i = 1; i < n; i++) {
            // Вычисляем максимальную сумму для текущей ступеньки i
            // Это стоимость самой ступеньки + максимум из двух предыдущих шагов
            int current_max = stairs[i] + Math.max(prev1, prev2);

            // Сдвигаем переменные для следующей итерации
            prev2 = prev1;
            prev1 = current_max;
        }

        // После цикла prev1 будет хранить максимальную сумму для последней, n-й ступеньки
        int result = prev1;

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}