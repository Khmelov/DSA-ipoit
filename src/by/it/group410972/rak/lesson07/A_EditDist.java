package by.it.group410972.rak.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {
    private int[][] memo;

    int getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();
        memo = new int[n + 1][m + 1];
        // Инициализируем кэш значениями -1 (не вычислено)
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }
        return editDist(one, two, n, m);
    }

    // Рекурсивная функция вычисления расстояния Левенштейна с мемоизацией
    private int editDist(String one, String two, int i, int j) {
        // Если один из индексов == 0, значит расстояние = длина другого (вставка или удаление)
        if (i == 0) return j;
        if (j == 0) return i;

        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        if (one.charAt(i - 1) == two.charAt(j - 1)) {
            // Если символы равны, просто идём дальше без изменения расстояния
            memo[i][j] = editDist(one, two, i - 1, j - 1);
        } else {
            // Иначе рассматриваем три операции:
            int insert = editDist(one, two, i, j - 1) + 1;    // вставка
            int delete = editDist(one, two, i - 1, j) + 1;    // удаление
            int replace = editDist(one, two, i - 1, j - 1) + 1; // замена
            memo[i][j] = Math.min(insert, Math.min(delete, replace));
        }
        return memo[i][j];
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
