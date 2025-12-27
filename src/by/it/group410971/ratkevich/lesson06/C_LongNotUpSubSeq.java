package by.it.group410971.ratkevich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: наибольшая невозростающая подпоследовательность

Дано:
    целое число 1<=n<=1E5 ( ОБРАТИТЕ ВНИМАНИЕ НА РАЗМЕРНОСТЬ! )
    массив A[1…n] натуральных чисел, не превосходящих 2E9.

Необходимо:
    Выведите максимальное 1<=k<=n, для которого гарантированно найдётся
    подпоследовательность индексов i[1]<i[2]<…<i[k] <= длины k,
    для которой каждый элемент A[i[k]] не больше любого предыдущего
    т.е. для всех 1<=j<k, A[i[j]]>=A[i[j+1]].

    В первой строке выведите её длину k,
    во второй - её индексы i[1]<i[2]<…<i[k]
    соблюдая A[i[1]]>=A[i[2]]>= ... >=A[i[n]].

    (индекс начинается с 1)

Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ

    Sample Input:
    5
    5 3 4 4 2

    Sample Output:
    4
    1 3 4 5
*/


public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_LongDivComSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int result = instance.getNotUpSeqSize(stream);
        System.out.print(result);
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int[] tail = new int[n];      // tail[i] = индекс последнего элемента подпоследовательности длины i+1
        int[] prev = new int[n];      // prev[i] = индекс предыдущего элемента в последовательности для i
        int length = 1;

        tail[0] = 0;     // начинаем с первого элемента
        prev[0] = -1;

        for (int i = 1; i < n; i++) {
            // бинарный поиск по tail: ищем место для вставки a[i]
            int l = 0, r = length - 1;
            while (l <= r) {
                int m = (l + r) / 2;
                if (a[tail[m]] >= a[i]) { // ищем НЕВОЗРАСТАЮЩУЮ
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }

            if (l > 0) {
                prev[i] = tail[l - 1];
            } else {
                prev[i] = -1;
            }

            tail[l] = i;
            if (l == length) {
                length++;
            }
        }

        // Восстановление последовательности индексов
        int[] indices = new int[length];
        int k = tail[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            indices[i] = k + 1; // +1 для перехода к 1-индексации
            k = prev[k];
        }

        // Вывод
        System.out.println(length);
        for (int idx : indices) {
            System.out.print(idx + " ");
        }
        System.out.println();

        return length;
    }
}