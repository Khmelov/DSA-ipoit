package by.it.group410972.shelegeiko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ... (остальные комментарии опущены для краткости)
public class C_HeapMax {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_HeapMax.class.getResourceAsStream("dataC.txt");
        C_HeapMax instance = new C_HeapMax();
        System.out.println("MAX=" + instance.findMaxValue(stream));
    }

    Long findMaxValue(InputStream stream) {
        Long maxValue = 0L;
        MaxHeap heap = new MaxHeap();
        Scanner scanner = new Scanner(stream);
        Integer count = scanner.nextInt();
        scanner.nextLine(); // Поглотить символ новой строки
        for (int i = 0; i < count; ) {
            String s = scanner.nextLine();
            if (s.equalsIgnoreCase("extractMax")) {
                Long res = heap.extractMax();
                if (res != null && res > maxValue) maxValue = res;
                i++;
            }
            if (s.contains(" ")) {
                String[] p = s.split(" ");
                if (p[0].equalsIgnoreCase("insert")) {
                    heap.insert(Long.parseLong(p[1]));
                }
                i++;
            }
        }
        return maxValue;
    }

    private class MaxHeap {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        private List<Long> heap = new ArrayList<>();

        private void swap(int i, int j) {
            Long temp = heap.get(i);
            heap.set(i, heap.get(j));
            heap.set(j, temp);
        }

        private void siftDown(int i) { // просеивание вниз
            int maxIndex = i;
            while (true) {
                int leftChild = 2 * i + 1;
                if (leftChild < heap.size() && heap.get(leftChild) > heap.get(maxIndex)) {
                    maxIndex = leftChild;
                }

                int rightChild = 2 * i + 2;
                if (rightChild < heap.size() && heap.get(rightChild) > heap.get(maxIndex)) {
                    maxIndex = rightChild;
                }

                if (i != maxIndex) {
                    swap(i, maxIndex);
                    i = maxIndex;
                } else {
                    break;
                }
            }
        }

        private void siftUp(int i) { // просеивание вверх
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap.get(i) > heap.get(parent)) {
                    swap(i, parent);
                    i = parent;
                } else {
                    break;
                }
            }
        }

        void insert(Long value) { // вставка
            heap.add(value);
            siftUp(heap.size() - 1);
        }

        Long extractMax() { // извлечение и удаление максимума
            if (heap.isEmpty()) {
                return null;
            }
            Long result = heap.get(0);
            if (heap.size() > 1) {
                heap.set(0, heap.get(heap.size() - 1));
                heap.remove(heap.size() - 1);
                siftDown(0);
            } else {
                heap.remove(0);
            }
            return result;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }
}