package by.it.group410971.tishuk.lesson01;

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
        // Вычисляем Пизано-период для m
        int[] pisano = new int[m * 6]; // длина Пизано-периода всегда <= 6*m
        pisano[0] = 0;
        pisano[1] = 1;

        int periodLength = 0;
        for (int i = 2; i < pisano.length; i++) {
            pisano[i] = (pisano[i - 1] + pisano[i - 2]) % m;
            if (pisano[i - 1] == 0 && pisano[i] == 1) {
                periodLength = i - 1;
                break;
            }
        }

        int index = (int)(n % periodLength);
        return pisano[index];
    }



}

