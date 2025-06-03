package by.it.group410972.pahodnia.lesson02;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

public class C_GreedyKnapsack {
    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        InputStream inputStream = C_GreedyKnapsack.class.getResourceAsStream("greedyKnapsack.txt");
        double costFinal = new C_GreedyKnapsack().calc(inputStream);
        long finishTime = System.currentTimeMillis();
        System.out.printf("Общая стоимость %f (время %d)\n", costFinal, finishTime - startTime);
    }

    double calc(InputStream inputStream) throws FileNotFoundException {
        Scanner input = new Scanner(inputStream);
        int n = input.nextInt();      // сколько предметов в файле
        int W = input.nextInt();      // какой вес у рюкзака
        Item[] items = new Item[n];   // список предметов

        for (int i = 0; i < n; i++) {
            items[i] = new Item(input.nextInt(), input.nextInt());
        }

        Arrays.sort(items); // сортируем предметы по убыванию стоимости на единицу веса

        double result = 0;
        int remainingWeight = W;

        for (Item item : items) {
            if (remainingWeight == 0) break;

            if (item.weight <= remainingWeight) {
                result += item.cost;
                remainingWeight -= item.weight;
            } else {
                result += (item.costPerWeight() * remainingWeight);
                remainingWeight = 0;
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

        double costPerWeight() {
            return (double) cost / weight;
        }

        @Override
        public int compareTo(Item o) {
            return Double.compare(o.costPerWeight(), this.costPerWeight()); // сортировка по убыванию
        }

        @Override
        public String toString() {
            return "Item{" + "cost=" + cost + ", weight=" + weight + '}';
        }
    }
}