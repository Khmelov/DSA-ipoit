package by.it.group510972.bahutskaya.lesson08;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

// Рюкзак: каждый слиток берём не больше одного раза.

public class TaskB {

    int getMaxWeight(InputStream stream ) {
        Scanner scanner = new Scanner(stream);
        int w=scanner.nextInt();
        int n=scanner.nextInt();
        int gold[]=new int[n];
        for (int i = 0; i < n; i++) {
            gold[i]=scanner.nextInt();
        }

        // Идём от тяжёлого веса к лёгкому, чтобы не взять слиток дважды
        boolean[] can = new boolean[w + 1];
        can[0] = true;
        for (int bar : gold) {
            for (int weight = w; weight >= bar; weight--) {
                if (can[weight - bar]) {
                    can[weight] = true;
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
        InputStream stream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        int res=instance.getMaxWeight(stream);
        System.out.println(res);
    }

}
