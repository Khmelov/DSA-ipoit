package by.it.group410972.shelegeiko.lesson01;

import java.util.ArrayList;
import java.util.List;

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
        List<Long> pisano = new ArrayList<>();
        pisano.add(0L);
        pisano.add(1L);
        long a = 0;
        long b = 1;
        long c;
        for (int i = 2; i <= 6 * m; ++i) {
            c = (a + b) % m;
            pisano.add(c);
            a = b;
            b = c;
            if (a == 0 && b == 1) {
                pisano.remove(pisano.size() - 1);
                pisano.remove(pisano.size() - 1);
                break;
            }
        }
        long period = pisano.size();
        long index = n % period;
        return pisano.get((int) index);
    }


}

