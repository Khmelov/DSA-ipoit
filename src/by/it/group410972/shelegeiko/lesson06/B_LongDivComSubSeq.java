package by.it.group410972.shelegeiko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
... (условие задачи) ...
*/

public class B_LongDivComSubSeq {


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataB.txt");
        B_LongDivComSubSeq instance = new B_LongDivComSubSeq();
        int result = instance.getDivSeqSize(stream);
        System.out.print(result);
    }

    int getDivSeqSize(InputStream stream) throws FileNotFoundException {
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

        // dp[i] хранит длину НКП, заканчивающейся на элементе m[i]
        int[] dp = new int[n];

        for (int i = 0; i < n; i++) {
            dp[i] = 1; // Минимальная длина - 1 (сам элемент)
            for (int j = 0; j < i; j++) {
                // Основное отличие от LIS - условие делимости
                if (m[i] % m[j] == 0) {
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