package by.it.group510972.bahutskaya.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Своя куча: положить число и достать самое большое.

public class TaskC {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = TaskC.class.getResourceAsStream("dataC.txt");
        TaskC instance = new TaskC();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                System.out.println();
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert"))
                    heap.insert(Long.parseLong(p[1]));
                i++;
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        private List<Long> heap = new ArrayList<>();

        int siftDown(int i) {
            int size = heap.size();
            while (true) {
                int left = i * 2 + 1;
                int right = left + 1;
                int largest = i;
                if (left < size && heap.get(left) > heap.get(largest)) {
                    largest = left;
                }
                if (right < size && heap.get(right) > heap.get(largest)) {
                    largest = right;
                }
                if (largest == i) {
                    break;
                }
                Long tmp = heap.get(i);
                heap.set(i, heap.get(largest));
                heap.set(largest, tmp);
                i = largest;
            }
            return i;
        }

        int siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(parent) >= heap.get(i)) {
                    break;
                }
                Long tmp = heap.get(i);
                heap.set(i, heap.get(parent));
                heap.set(parent, tmp);
                i = parent;
            }
            return i;
        }

        void insert(Long value) {
            // Кладём в конец и поднимаем вверх
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() {
            if (heap.isEmpty()) {
                return null;
            }
            // Забираем максимум и ставим последний элемент на его место
            Long result = heap.get(0);
            Long last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                siftDown(0);
            }
            return result;
        }
    }
}
