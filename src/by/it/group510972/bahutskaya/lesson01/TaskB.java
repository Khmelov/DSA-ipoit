package by.it.group510972.bahutskaya.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

// Фибоначчи через список. Число может быть очень длинным.

public class TaskB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        TaskB fibo = new TaskB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        if (n == 0) {
            return BigInteger.ZERO;
        }
        // Записываем 0 и 1, дальше каждое число — сумма двух предыдущих
        ArrayList<BigInteger> fib = new ArrayList<>(n + 1);
        fib.add(BigInteger.ZERO);
        fib.add(BigInteger.ONE);
        for (int i = 2; i <= n; i++) {
            fib.add(fib.get(i - 1).add(fib.get(i - 2)));
        }
        return fib.get(n);
    }

}
