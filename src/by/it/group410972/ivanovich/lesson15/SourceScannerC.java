package by.it.group410972.ivanovich.lesson15;

import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) throws IOException {
        String srcDir = System.getProperty("user.dir") + File.separator + "src";
        Path srcPath = Paths.get(srcDir);

        if (!Files.exists(srcPath)) {
            return;
        }

        // Собираем все обработанные файлы
        List<FileData> files = new ArrayList<>();
        Files.walk(srcPath)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        FileData data = processJavaFile(p, srcPath);
                        if (data != null) {
                            files.add(data);
                        }
                    } catch (IOException ignored) {
                    }
                });

        // Находим копии
        Map<String, Set<String>> copyGroups = new HashMap<>();
        findSimilarFiles(files, copyGroups);

        // Сортируем и выводим
        List<String> sortedPaths = new ArrayList<>(copyGroups.keySet());
        Collections.sort(sortedPaths);

        for (String path : sortedPaths) {
            Set<String> copies = copyGroups.get(path);
            if (!copies.isEmpty()) {
                System.out.println(path);
                List<String> sortedCopies = new ArrayList<>(copies);
                Collections.sort(sortedCopies);
                for (String copy : sortedCopies) {
                    System.out.println(copy);
                }
            }
        }
    }

    private static FileData processJavaFile(Path file, Path srcRoot) throws IOException {
        String content = readFileIgnoringErrors(file);

        // Пропускаем тестовые файлы
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return null;
        }

        // Обработка содержимого
        content = cleanJavaSource(content);

        String relativePath = srcRoot.relativize(file).toString();
        return new FileData(relativePath, content);
    }

    private static String readFileIgnoringErrors(Path path) throws IOException {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            // Пробуем другие кодировки
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, Charset.forName("Windows-1251"));
        }
    }

    private static String cleanJavaSource(String source) {
        // Удаляем package и import
        source = source.replaceAll("^\\s*package\\s+.*?;\\s*", "");
        source = source.replaceAll("\\n\\s*import\\s+.*?;\\s*", "");

        // Удаляем комментарии
        source = removeAllComments(source);

        // Нормализуем пробельные символы
        source = source.replaceAll("\\s+", " ");

        return source.trim();
    }

    private static String removeAllComments(String code) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        while (i < code.length()) {
            // Пропускаем строковые литералы
            if (code.charAt(i) == '"') {
                result.append('"');
                i++;
                while (i < code.length() && code.charAt(i) != '"') {
                    if (code.charAt(i) == '\\' && i + 1 < code.length()) {
                        result.append(code.charAt(i));
                        i++;
                    }
                    result.append(code.charAt(i));
                    i++;
                }
                if (i < code.length()) {
                    result.append('"');
                    i++;
                }
                continue;
            }

            // Пропускаем символьные литералы
            if (code.charAt(i) == '\'') {
                result.append('\'');
                i++;
                while (i < code.length() && code.charAt(i) != '\'') {
                    if (code.charAt(i) == '\\' && i + 1 < code.length()) {
                        result.append(code.charAt(i));
                        i++;
                    }
                    result.append(code.charAt(i));
                    i++;
                }
                if (i < code.length()) {
                    result.append('\'');
                    i++;
                }
                continue;
            }

            // Проверяем на начало комментария
            if (i + 1 < code.length() && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                // Многострочный комментарий
                i += 2;
                while (i + 1 < code.length() && !(code.charAt(i) == '*' && code.charAt(i + 1) == '/')) {
                    i++;
                }
                i += 2;
                continue;
            }

            if (i + 1 < code.length() && code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                // Однострочный комментарий
                i += 2;
                while (i < code.length() && code.charAt(i) != '\n') {
                    i++;
                }
                continue;
            }

            result.append(code.charAt(i));
            i++;
        }

        return result.toString();
    }

    private static void findSimilarFiles(List<FileData> files, Map<String, Set<String>> copyGroups) {
        int n = files.size();

        // Сначала группируем по хешу для быстрого нахождения точных копий
        Map<String, List<FileData>> hashGroups = new HashMap<>();
        for (FileData file : files) {
            String hash = Integer.toString(file.content.hashCode());
            hashGroups.computeIfAbsent(hash, k -> new ArrayList<>()).add(file);
        }

        // Обрабатываем группы с одинаковым хешем
        for (List<FileData> group : hashGroups.values()) {
            if (group.size() > 1) {
                // Все файлы в группе идентичны
                for (int i = 0; i < group.size(); i++) {
                    FileData file1 = group.get(i);
                    Set<String> copies = copyGroups.computeIfAbsent(file1.path, k -> new TreeSet<>());

                    for (int j = 0; j < group.size(); j++) {
                        if (i != j) {
                            copies.add(group.get(j).path);
                        }
                    }
                }
            }
        }

        // Для остальных файлов используем расстояние Левенштейна
        for (int i = 0; i < n; i++) {
            FileData file1 = files.get(i);

            // Пропускаем уже обработанные точные копии
            if (hashGroups.get(Integer.toString(file1.content.hashCode())).size() > 1) {
                continue;
            }

            for (int j = i + 1; j < n; j++) {
                FileData file2 = files.get(j);

                // Пропускаем если уже в одной группе
                if (hashGroups.get(Integer.toString(file2.content.hashCode())).size() > 1) {
                    continue;
                }

                // Быстрая проверка по длине
                if (Math.abs(file1.content.length() - file2.content.length()) >= 10) {
                    continue;
                }

                // Вычисляем расстояние Левенштейна
                int distance = levenshteinDistance(file1.content, file2.content);

                if (distance < 10) {
                    copyGroups.computeIfAbsent(file1.path, k -> new TreeSet<>()).add(file2.path);
                    copyGroups.computeIfAbsent(file2.path, k -> new TreeSet<>()).add(file1.path);
                }
            }
        }
    }

    private static int levenshteinDistance(String s, String t) {
        int m = s.length();
        int n = t.length();

        // Используем алгоритм с двумя строками для экономии памяти
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        // Инициализация
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= m; i++) {
            curr[0] = i;

            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(
                            Math.min(prev[j], curr[j - 1]),
                            prev[j - 1]
                    );
                }
            }

            // Копируем текущую строку в предыдущую
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[n];
    }

    static class FileData {
        String path;
        String content;

        FileData(String path, String content) {
            this.path = path;
            this.content = content;
        }
    }
}