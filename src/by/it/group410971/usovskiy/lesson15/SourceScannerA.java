package by.it.group410971.usovskiy.lesson15;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SourceScannerA {

    // Результат по одному файлу: относительный путь + размер
    private static class FileInfo {
        String relativePath;
        int size;

        FileInfo(String relativePath, int size) {
            this.relativePath = relativePath;
            this.size = size;
        }
    }

    public static void main(String[] args) {
        String src = System.getProperty("user.dir")
                + File.separator + "src" + File.separator;

        File srcDir = new File(src);
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            // нечего сканировать
            return;
        }

        List<FileInfo> result = new ArrayList<>();
        scanDirectory(srcDir, src, result);

        // сортируем по размеру, затем по пути лексикографически
        result.sort(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo a, FileInfo b) {
                int cmp = Integer.compare(a.size, b.size);
                if (cmp != 0) return cmp;
                return a.relativePath.compareTo(b.relativePath);
            }
        });

        StringBuilder out = new StringBuilder();
        for (FileInfo fi : result) {
            out.setLength(0);
            out.append(fi.size).append(' ').append(fi.relativePath);
            System.out.println(out);
        }
    }

    // Рекурсивный обход каталога, сбор *.java
    private static void scanDirectory(File dir, String srcRoot, List<FileInfo> out) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (f.isDirectory()) {
                scanDirectory(f, srcRoot, out);
            } else if (f.isFile() && f.getName().endsWith(".java")) {
                processJavaFile(f, srcRoot, out);
            }
        }
    }

    private static void processJavaFile(File file, String srcRoot, List<FileInfo> out) {
        String content;
        try {

            byte[] bytes = Files.readAllBytes(file.toPath());
            content = new String(bytes, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return;
        } catch (IOException e) {
            return;
        }


        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String noPkgImports = stripPackageAndImports(content);

        String trimmed = trimLowChars(noPkgImports);

        int size = trimmed.getBytes(StandardCharsets.UTF_8).length;

        String absPath = file.getAbsolutePath();
        String relPath = absPath.substring(srcRoot.length());

        out.add(new FileInfo(relPath, size));
    }

    // Удаляет строку package и все строки import, однопроходно по символам
    private static String stripPackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            int lineStart = i;
            // ищем конец строки
            while (i < len && text.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i;       // позиция '\n' или len (без включения)
            if (i < len && text.charAt(i) == '\n') {
                i++;               // включаем перевод строки
            }

            // анализируем текущую строку [lineStart, lineEnd)
            int p = lineStart;
            while (p < lineEnd && text.charAt(p) <= ' ') {
                p++;
            }
            boolean skip = false;
            if (p < lineEnd) {
                // проверяем на "package" или "import"
                if (startsWithWord(text, p, lineEnd, "package")) {
                    skip = true;
                } else if (startsWithWord(text, p, lineEnd, "import")) {
                    skip = true;
                }
            }

            if (!skip) {
                // копируем строку вместе с '\n' (если он был)
                sb.append(text, lineStart, i);
            }
        }

        return sb.toString();
    }

    // Проверка: начинается ли участок строки [start, end) с данного слова
    private static boolean startsWithWord(String s, int start, int end, String word) {
        int wlen = word.length();
        if (start + wlen > end) return false;
        for (int i = 0; i < wlen; i++) {
            if (s.charAt(start + i) != word.charAt(i)) return false;
        }
        // дальше может идти пробел, точка с запятой и т.п. — для нас не принципиально
        return true;
    }

    // Удаляем все символы с кодом <33 в начале и конце текста
    private static String trimLowChars(String text) {
        int start = 0;
        int end = text.length();

        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }

        if (start == 0 && end == text.length()) {
            return text;
        }
        return text.substring(start, end);
    }
}
