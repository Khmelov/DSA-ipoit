package by.it.group410972.kazyukevich.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        int[] m = new int[n];

        for (int i = 0; i < n; i++) {
            m[i] = scanner.nextInt();
        }

        int result = 0;

        int[] maxSubsequenceLengths  = new int[n];
        int[] prev = new int[n];

        Arrays.fill(prev, -1);

        List<Integer> lastElements = new ArrayList<>();
        List<Integer> elementIndices = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int pos = binarySearch(lastElements, m[i]);
            pos = (pos < 0) ? -pos - 1 : pos;

            if (pos < lastElements.size()) {
                lastElements.set(pos, m[i]);
                elementIndices.set(pos, i);
            } else {
                lastElements.add(m[i]);
                elementIndices.add(i);
            }

            maxSubsequenceLengths [i] = pos + 1;

            if (pos > 0) {
                prev[i] = elementIndices.get(pos - 1);
            }
        }

        result = lastElements.size();

        return result;
    }

    private int binarySearch(List<Integer> list, int target) {
        int left = 0, right = list.size();

        while (left < right) {
            int middle = (left + right) / 2;

            if (list.get(middle) >= target) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }

        return left;
    }
}