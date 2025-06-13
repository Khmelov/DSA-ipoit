package by.it.group410972.shelegeiko.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_LongNotUpSubSeq {

    private int[] resultIndices; // Поле для хранения итоговых индексов

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();
        int length = instance.getNotUpSeqSize(stream);

        System.out.println(length);
        for (int i = 0; i < instance.resultIndices.length; i++) {
            System.out.print(instance.resultIndices[i] + (i < instance.resultIndices.length - 1 ? " " : ""));
        }
    }

    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        int[] m = new int[n];
        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        if (n == 0) {
            this.resultIndices = new int[0];
            return 0;
        }

        int[] tails = new int[n]; // Хранит значения
        int[] tails_indices = new int[n]; // Хранит индексы этих значений в m
        int[] prev = new int[n]; // Хранит предшествующие индексы для восстановления пути
        int length = 0;

        for (int i = 0; i < n; i++) {
            int num = m[i];

            // Бинарный поиск для нахождения места для num в tails
            // Мы ищем первую позицию j, где tails[j] < num
            int left = 0, right = length;
            int pos = 0;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] >= num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            pos = left;

            // Обновляем tails
            tails[pos] = num;
            tails_indices[pos] = i;

            // Устанавливаем предшественника
            if (pos > 0) {
                prev[i] = tails_indices[pos - 1];
            } else {
                prev[i] = -1; // нет предшественника
            }

            if (pos == length) {
                length++;
            }
        }

        // Восстанавливаем путь
        this.resultIndices = new int[length];
        int currentIndex = tails_indices[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            resultIndices[i] = currentIndex + 1; // +1 для 1-based индексации
            currentIndex = prev[currentIndex];
        }

        return length;
    }
}