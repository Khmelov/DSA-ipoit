package by.it.group410972.shelegeiko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
... (условие задачи) ...
*/

public class A_LIS {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_LIS.class.getResourceAsStream("dataA.txt");
        A_LIS instance = new A_LIS();
        int result = instance.getSeqSize(stream);
        System.out.print(result);
    }

    int getSeqSize(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        //общая длина последовательности
        int n = scanner.nextInt();
        int[] m = new int[n];
        //читаем всю последовательность
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        if (n == 0) {
            return 0;
        }

        // dp[i] хранит длину НВП, заканчивающейся на элементе m[i]
        int[] dp = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимальная длина НВП - 1 (сам элемент)
            for (int j = 0; j < i; j++) {
                if (m[j] < m[i]) {
                    // Если можно продлить последовательность, заканчивающуюся на m[j]
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        // Итоговый результат - максимальное значение в массиве dp
        int result = 0;
        for (int value : dp) {
            if (value > result) {
                result = value;
            }
        }

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}