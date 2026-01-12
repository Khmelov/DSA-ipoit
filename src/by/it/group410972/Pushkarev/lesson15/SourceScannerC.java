package by.it.group410972.Pushkarev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SourceScannerC {

    public static void main(String[] args) {
        // Получаем каталог src
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        // Список для хранения обработанных файлов
        List<ProcessedFile> processedFiles = new ArrayList<>();

        try {
            // Рекурсивно обходим все файлы .java
            try (Stream<Path> walk = Files.walk(Paths.get(src))) {
                walk.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(p -> processFile(p, src, processedFiles));
            }
        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталогов: " + e.getMessage());
            return;
        }

        // Сортируем файлы по пути для консистентного вывода
        processedFiles.sort(Comparator.comparing(f -> f.path));

        // Находим копии
        Map<String, List<String>> copiesMap = findCopies(processedFiles);

        // Выводим результат в формате, который ожидает тест
        for (ProcessedFile file : processedFiles) {
            System.out.println(file.path);

            // Если у файла есть копии, выводим их
            List<String> copies = copiesMap.get(file.path);
            if (copies != null && !copies.isEmpty()) {
                // Убираем дубликаты и сортируем
                List<String> uniqueCopies = new ArrayList<>(new LinkedHashSet<>(copies));
                Collections.sort(uniqueCopies);

                for (String copy : uniqueCopies) {
                    System.out.println(copy);
                }
            }
        }
    }

    private static void processFile(Path filePath, String srcRoot, List<ProcessedFile> processedFiles) {
        try {
            // Читаем файл с обработкой ошибок кодировки
            String content = readFileWithFallback(filePath);

            // Пропускаем файлы с тестами
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Обрабатываем содержимое
            String processed = processContent(content);

            // Получаем относительный путь
            String relativePath = Paths.get(srcRoot).relativize(filePath).toString();

            // Сохраняем обработанный файл
            processedFiles.add(new ProcessedFile(relativePath, processed));

        } catch (IOException e) {
            // Игнорируем файлы, которые не удалось прочитать
            if (System.currentTimeMillis() < 0) {
                System.err.println("Ошибка чтения файла: " + filePath);
            }
        }
    }

    private static String readFileWithFallback(Path filePath) throws IOException {
        // Пробуем UTF-8
        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (MalformedInputException e1) {
            // Пробуем Windows-1251 (часто используется для кириллицы)
            try {
                return Files.readString(filePath, Charset.forName("Windows-1251"));
            } catch (MalformedInputException e2) {
                // Пробуем ISO-8859-1
                return Files.readString(filePath, StandardCharsets.ISO_8859_1);
            }
        }
    }

    private static String processContent(String content) {
        // Сначала удаляем package и import строки
        StringBuilder result = new StringBuilder();
        String[] lines = content.split("\n");
        boolean inBlockComment = false;

        for (String line : lines) {
            String trimmedLine = line.trim();

            // Пропускаем package и import
            if (trimmedLine.startsWith("package") || trimmedLine.startsWith("import")) {
                continue;
            }

            // Обработка комментариев
            StringBuilder processedLine = new StringBuilder();
            int i = 0;
            int n = line.length();

            while (i < n) {
                if (inBlockComment) {
                    if (i + 1 < n && line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        i += 2;
                    } else {
                        i++;
                    }
                } else {
                    if (i + 1 < n && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                        break; // Пропускаем оставшуюся часть строки
                    } else if (i + 1 < n && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                        inBlockComment = true;
                        i += 2;
                    } else {
                        processedLine.append(line.charAt(i));
                        i++;
                    }
                }
            }

            // Заменяем управляющие символы на пробелы
            String processed = processedLine.toString();
            for (int j = 0; j < processed.length(); j++) {
                char c = processed.charAt(j);
                if (c < 33) {
                    result.append(' ');
                } else {
                    result.append(c);
                }
            }
            result.append(' '); // Разделитель между строками
        }

        return result.toString().trim();
    }

    private static Map<String, List<String>> findCopies(List<ProcessedFile> files) {
        Map<String, List<String>> copiesMap = new HashMap<>();
        int n = files.size();

        // Используем хеширование для ускорения
        Map<String, List<Integer>> contentMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String shortHash = getShortSignature(files.get(i).content);
            contentMap.computeIfAbsent(shortHash, k -> new ArrayList<>()).add(i);
        }

        // Сравниваем только потенциально похожие файлы
        for (List<Integer> group : contentMap.values()) {
            if (group.size() > 1) {
                for (int i = 0; i < group.size(); i++) {
                    for (int j = i + 1; j < group.size(); j++) {
                        int idx1 = group.get(i);
                        int idx2 = group.get(j);

                        ProcessedFile file1 = files.get(idx1);
                        ProcessedFile file2 = files.get(idx2);

                        // Быстрая проверка по длине
                        if (Math.abs(file1.content.length() - file2.content.length()) >= 10) {
                            continue;
                        }

                        // Проверяем расстояние Левенштейна
                        if (isCopy(file1.content, file2.content)) {
                            copiesMap.computeIfAbsent(file1.path, k -> new ArrayList<>()).add(file2.path);
                            copiesMap.computeIfAbsent(file2.path, k -> new ArrayList<>()).add(file1.path);
                        }
                    }
                }
            }
        }

        // Сортируем списки копий
        for (List<String> copies : copiesMap.values()) {
            Collections.sort(copies);
        }

        return copiesMap;
    }

    private static String getShortSignature(String content) {
        // Создаем сигнатуру на основе длины и первых/последних символов
        if (content.length() < 20) {
            return content;
        }

        int len = content.length();
        return len + ":" +
                content.substring(0, Math.min(10, len)) + ":" +
                content.substring(Math.max(0, len - 10), len);
    }

    private static boolean isCopy(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Если разница в длине >= 10, не копия
        if (Math.abs(len1 - len2) >= 10) {
            return false;
        }

        // Используем оптимизированный алгоритм с порогом 10
        return levenshteinDistance(s1, s2) < 10;
    }

    private static int levenshteinDistance(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();

        // Оптимизация: если строки одинаковые
        if (s1.equals(s2)) return 0;

        // Оптимизация: если одна строка пустая
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        // Используем алгоритм с двумя массивами
        int[] prev = new int[len2 + 1];
        int[] curr = new int[len2 + 1];

        // Инициализация
        for (int j = 0; j <= len2; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= len1; i++) {
            curr[0] = i;

            // Отсечение: если минимальное возможное расстояние уже > 10
            int minInRow = i;
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(curr[j - 1] + 1, prev[j] + 1),
                        prev[j - 1] + cost
                );

                if (curr[j] < minInRow) {
                    minInRow = curr[j];
                }
            }

            // Если минимальное расстояние в строке уже > 10, можно остановиться
            if (minInRow > 10) {
                return minInRow;
            }

            // Обмен массивами
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[len2];
    }

    private static class ProcessedFile {
        String path;
        String content;

        ProcessedFile(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}