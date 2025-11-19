package by.it.group410972.kazyukevich.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        // Если n равно 0 или 1, возвращаем n
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }

        ArrayList<BigInteger> fib = new ArrayList<>(n + 1);

        fib.add(BigInteger.ZERO); // F(0) = 0
        fib.add(BigInteger.ONE);  // F(1) = 1

        for (int i = 2; i <= n; i++) {
            BigInteger nextFib = fib.get(i - 1).add(fib.get(i - 2)); // F(i) = F(i-1) + F(i-2)
            fib.add(nextFib);
        }

        return fib.get(n);
    }
}