package by.it.group510972.bahutskaya.lesson02;
// Рюкзак можно набивать кусками. Сначала берём то, где за килограмм платят больше.

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class TaskC {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = TaskC.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new TaskC().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();
        int W = input.nextInt();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.printf("Всего предметов: %d. Рюкзак вмещает %d кг.\n", n, W);

        double result = 0;
        // Сортируем по стоимости за килограмм
        Arrays.sort(items);
        int left = W;
        for (Item item : items) {
            if (left == 0) {
                break;
            }
            if (item.weight <= left) {
                result += item.cost;
                left -= item.weight;
            } else {
                // Последний предмет режем, чтобы добить рюкзак
                result += item.cost * (left / (double) item.weight);
                left = 0;
            }
        }

        System.out.printf("Удалось собрать рюкзак на сумму %f\n", result);
        return result;
    }

    private static class Item implements Comparable<Item> {
        int cost;
        int weight;

        Item(int cost, int weight) {
            this.cost = cost;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Item{" +
                   "cost=" + cost +
                   ", weight=" + weight +
                   '}';
        }

        @Override
        public int compareTo(Item o) {
            double left = (double) o.cost / o.weight;
            double right = (double) cost / weight;
            return Double.compare(left, right);
        }
    }
}
