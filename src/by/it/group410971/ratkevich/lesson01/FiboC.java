package by.it.group410971.ratkevich.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (m == 1) return 0;          // любой Fibonacci mod 1 = 0
        long period = pisanoPeriod(m); // шаг 1
        long k      = n % period;      // шаг 2
        return fibMod(k, m);           // шаг 3
    }

    /** Шаг 1: длина периода Писано π(m)  (0,1 … 0,1). */
    private long pisanoPeriod(int m) {
        long prev = 0, curr = 1;
        for (long i = 0; i < 6L * m; i++) {
            long tmp = (prev + curr) % m;
            prev = curr;
            curr = tmp;
            if (prev == 0 && curr == 1)   // нашли начало нового периода
                return i + 1;
        }
        throw new IllegalStateException("Pisano period wasn’t found!");
    }

    /** Шаг 3: линейное вычисление F(k) mod m (k ≤ ~6·10⁵). */
    private long fibMod(long k, int m) {
        if (k == 0) return 0;
        long prev = 0, curr = 1;
        for (long i = 2; i <= k; i++) {
            long tmp = (prev + curr) % m;
            prev = curr;
            curr = tmp;
        }
        return curr;
    }
}

