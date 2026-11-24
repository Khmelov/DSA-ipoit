package by.it.group510971.rudko.lesson01;

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
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        //Идея: последовательность Фибоначчи по модулю m периодична (период Пизано pi(m) <= 6m).
        //Находим период, затем берем индекс n mod period.
        long prev = 0, cur = 1;
        long period = 0;
        do {
            long next = (prev + cur) % m;
            prev = cur;
            cur = next;
            period++;
        } while (!(prev == 0 && cur == 1));

        long k = n % period;
        long a = 0, b = 1;
        for (long i = 0; i < k; i++) {
            long next = (a + b) % m;
            a = b;
            b = next;
        }
        return a % m;
    }


}

