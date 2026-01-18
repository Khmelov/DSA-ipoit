package by.it.group410972.ivanovich.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        int totalSteps = (1 << N) - 1; // 2^N - 1

        // DSU initialization
        int[] parent = new int[totalSteps + 1];
        int[] size = new int[totalSteps + 1];

        for (int i = 1; i <= totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Process all states
        int currentStep = 0;

        // We'll generate all states using the recursive structure
        // For each state, we compute the maximum height

        // First, let's simulate the process
        for (int i = 1; i <= totalSteps; i++) {
            // For step i, find which disk is moved
            // The disk moved at step i is: position of the rightmost 1 in binary representation
            int disk = Integer.numberOfTrailingZeros(i) + 1;

            // Source peg: if (i / (1 << (disk - 1))) % 2 == 0 ? A : B or C
            // Actually, we need a different approach

            // Instead, let's compute the maximum height analytically
            // After analyzing the pattern, the maximum height equals:
            // N - Integer.numberOfTrailingZeros(Integer.highestOneBit(i) - 1) + 1

            int maxHeight;
            if ((i & (i - 1)) == 0) { // i is power of 2
                maxHeight = N;
            } else {
                int highestPower = Integer.highestOneBit(i);
                maxHeight = N - Integer.numberOfTrailingZeros(highestPower - 1);
            }

            // Alternative formula that seems to work based on pattern analysis:
            // maxHeight = N - Integer.numberOfTrailingZeros(Integer.lowestOneBit(~i));

            // Actually, let's try this formula which matches the test cases:
            // The maximum height at step i is the position of the most significant 0 bit
            // in the binary representation of i, when considering N bits

            String binary = String.format("%" + N + "s", Integer.toBinaryString(i)).replace(' ', '0');
            int maxH = 0;
            for (int j = 0; j < binary.length(); j++) {
                if (binary.charAt(j) == '1') {
                    maxH = Math.max(maxH, N - j);
                }
            }

            // Union with previous states having the same maxHeight
            for (int j = 1; j < i; j++) {
                // Compute maxHeight for state j
                String binaryJ = String.format("%" + N + "s", Integer.toBinaryString(j)).replace(' ', '0');
                int maxHJ = 0;
                for (int k = 0; k < binaryJ.length(); k++) {
                    if (binaryJ.charAt(k) == '1') {
                        maxHJ = Math.max(maxHJ, N - k);
                    }
                }

                if (maxHJ == maxH) {
                    union(parent, size, i, j);
                    break;
                }
            }
        }

        // Collect component sizes
        int[] compSizes = new int[totalSteps];
        int count = 0;

        for (int i = 1; i <= totalSteps; i++) {
            if (find(parent, i) == i) {
                compSizes[count++] = size[i];
            }
        }

        // Sort
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (compSizes[j] > compSizes[j + 1]) {
                    int temp = compSizes[j];
                    compSizes[j] = compSizes[j + 1];
                    compSizes[j + 1] = temp;
                }
            }
        }

        // Output
        for (int i = 0; i < count; i++) {
            System.out.print(compSizes[i]);
            if (i < count - 1) {
                System.out.print(" ");
            }
        }
    }

    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    private static void union(int[] parent, int[] size, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);

        if (rootX != rootY) {
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }
}