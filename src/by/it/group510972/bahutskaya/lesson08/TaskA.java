package by.it.group510972.bahutskaya.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Рюкзак: один и тот же слиток можно класть сколько угодно раз.

public class TaskA {

    int getMaxWeight(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        // Отмечаем веса, которые можно набрать, повторяя слитки
        boolean[] can = new boolean[w + 1];
        can[0] = true;
        for (int weight = 0; weight <= w; weight++) {
            if (!can[weight]) {
                continue;
            }
            for (int bar : gold) {
                if (bar > 0 && weight + bar <= w) {
                    can[weight + bar] = true;
                }
            }
        }
        int result = 0;
        for (int weight = w; weight >= 0; weight--) {
            if (can[weight]) {
                result = weight;
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskA.class.getResourceAsStream("dataA.txt");
        TaskA instance = new TaskA();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }
}
