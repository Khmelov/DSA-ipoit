package by.it.a_khmelev.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {

    public static void main(String[] args) {
        try {
            // Получаем путь к каталогу src
            String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
            File srcDir = new File(src);

            if (!srcDir.exists() || !srcDir.isDirectory()) {
                // Если каталог не найден, завершаем работу
                return;
            }

            String srcRoot = srcDir.getAbsolutePath();
            List<FileInfo> fileInfos = new ArrayList<>();

            // Рекурсивно обходим все файлы
            scanDirectory(srcDir, fileInfos, srcRoot);

            // Сортируем файлы: сначала по размеру, затем по пути
            fileInfos.sort(Comparator
                    .comparingInt(FileInfo::getSize)
                    .thenComparing(FileInfo::getRelativePath));

            // Выводим результаты
            for (FileInfo fileInfo : fileInfos) {
                System.out.println(fileInfo.getSize() + " " + fileInfo.getRelativePath());
            }

        } catch (Exception e) {
            // Игнорируем все исключения
        }
    }

    private static void scanDirectory(File directory, List<FileInfo> fileInfos, String srcRoot) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Рекурсивно обходим подкаталоги
                scanDirectory(file, fileInfos, srcRoot);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                // Обрабатываем Java файлы
                processJavaFile(file, fileInfos, srcRoot);
            }
        }
    }

    private static void processJavaFile(File file, List<FileInfo> fileInfos, String srcRoot) {
        try {
            // Читаем содержимое файла с обработкой ошибок кодировки
            String content = readFileWithFallback(file.toPath());

            // Пропускаем тестовые файлы
            if (content.contains("@Test") || content.contains("org.junit.Test")) {
                return;
            }

            // Обрабатываем содержимое (удаляем package, import, комментарии)
            String processedContent = processContent(content);

            // Вычисляем размер в байтах (UTF-8)
            byte[] bytes = processedContent.getBytes(StandardCharsets.UTF_8);
            int size = bytes.length;

            // Получаем относительный путь
            String absolutePath = file.getAbsolutePath();
            String relativePath = absolutePath.substring(srcRoot.length());

            // Убираем начальный разделитель, если есть
            if (relativePath.startsWith(File.separator)) {
                relativePath = relativePath.substring(1);
            }

            // Заменяем разделители на / для единообразия
            relativePath = relativePath.replace(File.separatorChar, '/');

            // Добавляем информацию о файле
            fileInfos.add(new FileInfo(size, relativePath));

        } catch (Exception e) {
            // Игнорируем файлы, которые не удалось обработать
        }
    }

    private static String readFileWithFallback(Path path) throws IOException {
        // Пробуем разные кодировки для чтения файла
        Charset[] charsets = {
            StandardCharsets.UTF_8,
            StandardCharsets.ISO_8859_1,
            Charset.forName("Windows-1251"),
            Charset.forName("CP1252"),
            StandardCharsets.US_ASCII
        };

        for (Charset charset : charsets) {
            try {
                return Files.readString(path, charset);
            } catch (IOException e) {
                // Пробуем следующую кодировку
                continue;
            }
        }

        throw new IOException("Cannot read file with any supported encoding");
    }

    private static String processContent(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }

        // 1. Удаляем комментарии за O(n)
        String withoutComments = removeComments(content);

        // 2. Удаляем package и import, пустые строки за O(n)
        String processed = removePackageImportAndEmptyLines(withoutComments);

        // 3. Удаляем символы с кодом < 33 в начале и конце
        processed = trimControlCharacters(processed);

        return processed;
    }

    private static String removeComments(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int i = 0;

        while (i < length) {
            char current = content.charAt(i);

            // Проверяем на однострочный комментарий //
            if (current == '/' && i + 1 < length && content.charAt(i + 1) == '/') {
                // Пропускаем до конца строки
                while (i < length && content.charAt(i) != '\n') {
                    i++;
                }
                continue;
            }

            // Проверяем на многострочный комментарий /* */
            if (current == '/' && i + 1 < length && content.charAt(i + 1) == '*') {
                i += 2; // Пропускаем /*

                // Ищем */
                while (i < length - 1 && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }

                if (i < length - 1) {
                    i += 2; // Пропускаем */
                }
                continue;
            }

            // Проверяем на Javadoc /** */
            if (current == '/' && i + 2 < length && content.charAt(i + 1) == '*' && content.charAt(i + 2) == '*') {
                i += 3; // Пропускаем /**

                // Ищем */
                while (i < length - 1 && !(content.charAt(i) == '*' && content.charAt(i + 1) == '/')) {
                    i++;
                }

                if (i < length - 1) {
                    i += 2; // Пропускаем */
                }
                continue;
            }

            // Если это не комментарий, добавляем символ к результату
            result.append(current);
            i++;
        }

        return result.toString();
    }

    private static String removePackageImportAndEmptyLines(String content) {
        StringBuilder result = new StringBuilder();
        int length = content.length();
        int lineStart = 0;
        boolean inLine = false;

        for (int i = 0; i < length; i++) {
            char c = content.charAt(i);

            if (c == '\n' || i == length - 1) {
                // Конец строки или конец файла
                int lineEnd = (i == length - 1) ? i + 1 : i;
                String line = content.substring(lineStart, lineEnd);
                String trimmedLine = line.trim();

                // Проверяем, не пустая ли строка и не начинается ли с package/import
                if (!trimmedLine.isEmpty() &&
                    !trimmedLine.startsWith("package ") &&
                    !trimmedLine.startsWith("import ")) {

                    // Если это не первая строка, добавляем перевод строки
                    if (inLine) {
                        result.append("\n");
                    }
                    result.append(line);
                    inLine = true;
                }

                lineStart = i + 1;
            }
        }

        return result.toString();
    }

    private static String trimControlCharacters(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        // Удаляем символы с кодом < 33 в начале
        int start = 0;
        while (start < str.length() && str.charAt(start) < 33) {
            start++;
        }

        // Удаляем символы с кодом < 33 в конце
        int end = str.length() - 1;
        while (end >= start && str.charAt(end) < 33) {
            end--;
        }

        if (start > end) {
            return "";
        }

        return str.substring(start, end + 1);
    }

    // Вспомогательный класс для хранения информации о файле
    private static class FileInfo {
        private final int size;
        private final String relativePath;

        public FileInfo(int size, String relativePath) {
            this.size = size;
            this.relativePath = relativePath;
        }

        public int getSize() {
            return size;
        }

        public String getRelativePath() {
            return relativePath;
        }
    }
}
