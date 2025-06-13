package by.it.group410972.shelegeiko.lesson03;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Lesson 3. B_Huffman.
// Восстановите строку по её коду и беспрефиксному коду символов.
// ... (остальные комментарии опущены для краткости)
public class B_Huffman {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = B_Huffman.class.getResourceAsStream("dataB.txt");
        B_Huffman instance = new B_Huffman();
        String result = instance.decode(inputStream);
        System.out.println(result);
    }

    String decode(InputStream inputStream) throws FileNotFoundException {
        StringBuilder result = new StringBuilder();
        //прочитаем строку для кодирования из тестового файла
        Scanner scanner = new Scanner(inputStream);
        Integer count = scanner.nextInt();
        Integer length = scanner.nextInt();
        scanner.nextLine(); // Важно: съедаем оставшийся символ новой строки после nextInt()

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        // 1. Создаем карту для хранения кодов: "01" -> 'a'
        Map<String, Character> codeMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String line = scanner.nextLine(); // Считываем строку вида "a: 0"
            String[] parts = line.split(": ");
            char character = parts[0].charAt(0);
            String code = parts[1];
            codeMap.put(code, character);
        }

        // 2. Считываем закодированную строку
        String encodedString = scanner.next();

        // 3. Декодируем строку
        StringBuilder currentCode = new StringBuilder();
        for (char bit : encodedString.toCharArray()) {
            currentCode.append(bit);
            // Проверяем, есть ли собранный код в нашей карте
            if (codeMap.containsKey(currentCode.toString())) {
                // Если есть, добавляем символ в результат
                result.append(codeMap.get(currentCode.toString()));
                // И сбрасываем текущий код для поиска следующего
                currentCode.setLength(0);
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        return result.toString();
    }
}