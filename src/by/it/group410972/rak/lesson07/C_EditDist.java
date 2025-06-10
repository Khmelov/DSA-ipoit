package by.it.group410972.rak.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    String getDistanceEdinting(String one, String two) {
        int n = one.length();
        int m = two.length();

        int[][] dp = new int[n + 1][m + 1];
        char[][] ops = new char[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            dp[i][0] = i;
            ops[i][0] = '-';
        }
        for (int j = 1; j <= m; j++) {
            dp[0][j] = j;
            ops[0][j] = '+';
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    ops[i][j] = '#';
                } else {
                    int insertCost = dp[i][j - 1] + 1;
                    int deleteCost = dp[i - 1][j] + 1;
                    int replaceCost = dp[i - 1][j - 1] + 1;

                    int minCost = Math.min(insertCost, Math.min(deleteCost, replaceCost));
                    dp[i][j] = minCost;

                    if (minCost == replaceCost) {
                        ops[i][j] = '~';
                    } else if (minCost == deleteCost) {
                        ops[i][j] = '-';
                    } else {
                        ops[i][j] = '+';
                    }
                }
            }
        }

        // Восстановление пути — теперь в список
        List<String> operations = new ArrayList<>();
        int i = n, j = m;
        while (i > 0 || j > 0) {
            char op = ops[i][j];
            switch (op) {
                case '#':
                    operations.add("#");
                    i--;
                    j--;
                    break;
                case '~':
                    operations.add("~" + two.charAt(j - 1));
                    i--;
                    j--;
                    break;
                case '-':
                    operations.add("-" + one.charAt(i - 1));
                    i--;
                    break;
                case '+':
                    operations.add("+" + two.charAt(j - 1));
                    j--;
                    break;
                default:
                    i--;
                    j--;
                    break;
            }
        }
        // Операции получены в обратном порядке — перевернём их
        Collections.reverse(operations);

        // Склеим с запятыми без лишних в начале или конце
        return String.join(",", operations) + ",";
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}