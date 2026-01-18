package by.it.group410972.ivanovich.lesson15;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.*;
import java.util.*;

public class SourceScannerA {
    public static void main(String[] args) {
        // Получаем путь к каталогу src
        String src = System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "src" + FileSystems.getDefault().getSeparator();
        Path srcPath = Paths.get(src);

        // Список для хранения информации о файлах
        List<FileInfo> fileInfos = new ArrayList<>();

        try {
            // Обходим все файлы .java в каталоге src и подкаталогах
            Files.walk(srcPath)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .forEach(p -> {
                        try {
                            // Читаем содержимое файла с обработкой ошибок кодировки
                            String content = readFileWithFallback(p);

                            // Проверяем, что файл не содержит тестов
                            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                                return;
                            }

                            // Обрабатываем содержимое файла
                            String processedContent = processContent(content);

                            // Вычисляем размер в байтах
                            int size = processedContent.getBytes(StandardCharsets.UTF_8).length;

                            // Получаем относительный путь от src
                            String relativePath = srcPath.relativize(p).toString();

                            fileInfos.add(new FileInfo(relativePath, size));

                        } catch (IOException e) {
                            // Игнорируем ошибки чтения файлов
                        }
                    });

            // Сортируем файлы по размеру, а при равных размерах - по пути
            Collections.sort(fileInfos, (a, b) -> {
                if (a.size != b.size) {
                    return Integer.compare(a.size, b.size);
                }
                return a.path.compareTo(b.path);
            });

            // Выводим результаты
            for (FileInfo info : fileInfos) {
                System.out.println(info.size + " " + info.path);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обходе каталога: " + e.getMessage());
        }
    }

    private static String readFileWithFallback(Path path) throws IOException {
        // Пытаемся прочитать с разными кодировками для обработки MalformedInputException
        List<Charset> charsets = Arrays.asList(
                StandardCharsets.UTF_8,
                Charset.forName("windows-1251"),
                StandardCharsets.ISO_8859_1
        );

        for (Charset charset : charsets) {
            try {
                byte[] bytes = Files.readAllBytes(path);
                return new String(bytes, charset);
            } catch (CharacterCodingException e) {
                // Пробуем следующую кодировку
                continue;
            } catch (IOException e) {
                throw e;
            }
        }

        // Если все кодировки не подошли, читаем как UTF-8 и заменяем некорректные символы
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8)
                .replaceAll("\\ufffd", ""); // Удаляем символы замены
    }

    private static String processContent(String content) {
        // 1. Удаляем строку package и все импорты за O(n)
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith("package ") &&
                    !trimmedLine.startsWith("import ")) {
                result.append(line).append("\n");
            }
        }

        String processed = result.toString();

        // 2. Удаляем все символы с кодом <33 в начале и конце текста
        processed = removeControlCharsFromEdges(processed);

        return processed;
    }

    private static String removeControlCharsFromEdges(String text) {
        if (text.isEmpty()) {
            return text;
        }

        // Удаляем в начале
        int start = 0;
        while (start < text.length() && text.charAt(start) < 33) {
            start++;
        }

        // Если весь текст состоял из управляющих символов
        if (start == text.length()) {
            return "";
        }

        // Удаляем в конце
        int end = text.length() - 1;
        while (end >= start && text.charAt(end) < 33) {
            end--;
        }

        return text.substring(start, end + 1);
    }

    // Вспомогательный класс для хранения информации о файле
    static class FileInfo {
        String path;
        int size;

        FileInfo(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}