package by.it.group410972.yarmak.lesson08;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_Stairs {

    int getMaxSum(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] stairs = new int[n];

        for (int i = 0; i < n; i++) {
            stairs[i] = scanner.nextInt();
        }

        if (n == 0) return 0;
        if (n == 1) return stairs[0];

        int prev2 = stairs[0]; // dp[i-2]
        int prev1 = Math.max(stairs[0] + stairs[1], stairs[1]); // dp[i-1]

        for (int i = 2; i < n; i++) {
            int current = Math.max(prev1, prev2) + stairs[i];
            prev2 = prev1;
            prev1 = current;
        }

        return prev1;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_Stairs.class.getResourceAsStream("dataC.txt");
        C_Stairs instance = new C_Stairs();
        int res = instance.getMaxSum(stream);
        System.out.println(res);
    }
}