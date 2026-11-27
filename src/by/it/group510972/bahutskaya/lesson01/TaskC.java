package by.it.group510972.bahutskaya.lesson01;

// Остаток от n-го Фибоначчи. Число огромное, поэтому берём период Пизано.

public class TaskC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        TaskC fibo = new TaskC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (n == 0 || m == 1) {
            return 0L;
        }
        // Находим период и берём остаток от n
        int period = pisano(m);
        int index = (int) (n % period);
        return fibMod(index, m);
    }

    private int pisano(int m) {
        int prev = 0;
        int cur = 1;
        for (int i = 0; i < m * 6; i++) {
            int next = (prev + cur) % m;
            prev = cur;
            cur = next;
            if (prev == 0 && cur == 1) {
                return i + 1;
            }
        }
        return m * 6;
    }

    private long fibMod(int n, int m) {
        if (n <= 1) {
            return n;
        }
        int prev = 0;
        int cur = 1;
        for (int i = 2; i <= n; i++) {
            int next = (prev + cur) % m;
            prev = cur;
            cur = next;
        }
        return cur;
    }

}
