package by.it.group510972.bahutskaya.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// По кодам букв собираем строку обратно.

public class TaskB {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = TaskB.class.getResourceAsStream("dataB.txt");
        TaskB instance = new TaskB();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        scanner.nextLine();
        // Читаем код каждой буквы
        Map<String, Character> codeToChar = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine();
            int colon = line.indexOf(':');
            char symbol = line.charAt(0);
            String code = line.substring(colon + 1).trim();
            codeToChar.put(code, symbol);
        }
        // Идём по битам, пока не соберётся код буквы
        String encoded = scanner.nextLine();
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < encoded.length() && result.length() < length; i++) {
            current.append(encoded.charAt(i));
            Character symbol = codeToChar.get(current.toString());
            if (symbol != null) {
                result.append(symbol);
                current.setLength(0);
            }
        }
        return result.toString(); //01001100100111
    }

}
