package by.it.group410972.kazyukevich.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {

    String getDistanceEdinting(String one, String two) {

        int firstStringLength = one.length();
        int secondStringLength = two.length();

        int[][] minEditDistance = new int[firstStringLength + 1][secondStringLength + 1];

        for (int i = 0; i <= firstStringLength; minEditDistance[i][0] = i++) {}

        for (int j = 0; j <= secondStringLength; minEditDistance[0][j] = j++) {}

        for (int i = 1; i <= firstStringLength; i++) {
            for (int j = 1; j <= secondStringLength; j++) {
                int cost = (one.charAt(i - 1) == two.charAt(j - 1)) ? 0 : 1;

                minEditDistance[i][j] = Math.min(
                        minEditDistance[i - 1][j] + 1,
                        Math.min(
                                minEditDistance[i][j - 1] + 1,
                                minEditDistance[i - 1][j - 1] + cost
                        )
                );
            }
        }

        StringBuilder resultBuilder = new StringBuilder();

        int i = firstStringLength;
        int j = secondStringLength;

        while (i > 0 || j > 0) {
            if (i > 0 && minEditDistance[i][j] == minEditDistance[i - 1][j] + 1) {
                resultBuilder.insert(0, "-" + one.charAt(i - 1) + ",");

                i--;
            } else if (j > 0 && minEditDistance[i][j] == minEditDistance[i][j - 1] + 1) {
                resultBuilder.insert(0, "+" + two.charAt(j - 1) + ",");

                j--;
            } else {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    resultBuilder.insert(0, "#,");
                } else {
                    resultBuilder.insert(0, "~" + two.charAt(j - 1) + ",");
                }

                i--;
                j--;
            }
        }

        String result = resultBuilder.toString();
        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }

}