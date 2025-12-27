package by.it.group410971.ratkevich.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        // базовые случаи
        if (n == null || n < 0)
            throw new IllegalArgumentException("n must be non-negative");
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        // создаём массив для всех F(0)…F(n)
        BigInteger[] fib = new BigInteger[n + 1];
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        for (int i = 2; i <= n; i++) {
            // F(i) = F(i-1) + F(i-2)
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }
        return fib[n];
    }
}

