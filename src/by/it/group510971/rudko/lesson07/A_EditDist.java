package by.it.group510971.rudko.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
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


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!


        //рекурсия с мемоизацией: memo[i][j] - расстояние между префиксами one[0..i) и two[0..j)
        int[][] memo = new int[one.length() + 1][two.length() + 1];
        for (int[] row : memo) Arrays.fill(row, -1);
        int result = distance(one, two, one.length(), two.length(), memo);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }


    private int distance(String one, String two, int i, int j, int[][] memo) {
        if (i == 0) return j;                 //вставляем весь оставшийся префикс
        if (j == 0) return i;                 //удаляем весь оставшийся префикс
        if (memo[i][j] != -1) return memo[i][j];
        int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;
        int res = Math.min(distance(one, two, i - 1, j, memo) + 1,                  //удаление
                  Math.min(distance(one, two, i, j - 1, memo) + 1,                  //вставка
                           distance(one, two, i - 1, j - 1, memo) + cost));         //замена/совпадение
        memo[i][j] = res;
        return res;
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
