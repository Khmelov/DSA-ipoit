package by.it.group410972.stankevich.lesson01;

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
        if (n <= 1) {
            return n;
        }

        long[] pisanoPeriod = getPisanoPeriod(m);
        int periodLength = pisanoPeriod.length;

        int remainder = (int) (n % periodLength);

        return pisanoPeriod[remainder];
    }

    private long[] getPisanoPeriod(int m) {
        long previous = 0;
        long current = 1;

        long[] pisano = new long[m * m];
        pisano[0] = previous;
        pisano[1] = current;

        int i;
        for (i = 2; i < pisano.length; i++) {
            long temp = (previous + current) % m;
            pisano[i] = temp;
            previous = current;
            current = temp;

            if (previous == 0 && current == 1) {
                break;
            }
        }
        long[] result = new long[i];
        System.arraycopy(pisano, 0, result, 0, i);
        return result;
    }

}

