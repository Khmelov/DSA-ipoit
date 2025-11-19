package by.it.group410972.kazyukevich.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);

        int count = scanner.nextInt();
        int length = scanner.nextInt();
        scanner.nextLine();

        Map<String, Character> codeTable = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(": ");
            char symbol = parts[0].charAt(0);
            String code = parts[1];
            codeTable.put(code, symbol);
        }

        String encodedString = scanner.nextLine();

        StringBuilder currentCode = new StringBuilder();
        for (char bit : encodedString.toCharArray()) {
            currentCode.append(bit);
            if (codeTable.containsKey(currentCode.toString())) {
                result.append(codeTable.get(currentCode.toString()));
                currentCode.setLength(0);
            }
        }

        return result.toString();
    }
}
