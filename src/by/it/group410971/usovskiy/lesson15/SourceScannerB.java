package by.it.group410971.usovskiy.lesson15;



import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SourceScannerB {

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
            return;
        }

        List<FileInfo> results = new ArrayList<>();
        scanDirectory(srcDir, src, results);

        // сортировка: сначала по размеру, затем по пути
        results.sort(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo a, FileInfo b) {
                int cmp = Integer.compare(a.size, b.size);
                if (cmp != 0) return cmp;
                return a.relativePath.compareTo(b.relativePath);
            }
        });

        StringBuilder sb = new StringBuilder();
        for (FileInfo fi : results) {
            sb.setLength(0);
            sb.append(fi.size).append(' ').append(fi.relativePath);
            System.out.println(sb);
        }
    }

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
        } catch (IOException e) {
            return;
        }

        // Пропускаем тестовые файлы
        if (content.contains("@Test") || content.contains("org.junit.Test")) {
            return;
        }

        String withoutPkgImports = stripPackageAndImports(content);

        String withoutComments = stripComments(withoutPkgImports);

        String trimmed = trimLowChars(withoutComments);

        String finalText = removeEmptyLines(trimmed);

        int size = finalText.getBytes(StandardCharsets.UTF_8).length;

        String absPath = file.getAbsolutePath();
        String relPath = absPath.substring(srcRoot.length());

        out.add(new FileInfo(relPath, size));
    }

    // ----- шаг 1: удалить package и import строки -----
    private static String stripPackageAndImports(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            int lineStart = i;
            while (i < len && text.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i; // позиция до \n
            if (i < len && text.charAt(i) == '\n') {
                i++;         // включаем \n в строку
            }

            int p = lineStart;
            while (p < lineEnd && text.charAt(p) <= ' ') {
                p++;
            }

            boolean skip = false;
            if (p < lineEnd) {
                if (startsWithWord(text, p, lineEnd, "package")) {
                    skip = true;
                } else if (startsWithWord(text, p, lineEnd, "import")) {
                    skip = true;
                }
            }

            if (!skip) {
                sb.append(text, lineStart, i);
            }
        }
        return sb.toString();
    }

    private static boolean startsWithWord(String s, int start, int end, String word) {
        int wlen = word.length();
        if (start + wlen > end) return false;
        for (int i = 0; i < wlen; i++) {
            if (s.charAt(start + i) != word.charAt(i)) return false;
        }
        return true;
    }

    // ----- шаг 2: удалить комментарии -----

    private static String stripComments(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        final int NORMAL = 0;
        final int LINE_COMMENT = 1;
        final int BLOCK_COMMENT = 2;
        final int STRING = 3;
        final int CHAR = 4;

        int state = NORMAL;

        while (i < len) {
            char c = text.charAt(i);
            char next = (i + 1 < len) ? text.charAt(i + 1) : '\0';

            switch (state) {
                case NORMAL:
                    if (c == '/' && next == '/') {
                        state = LINE_COMMENT;
                        i += 2;
                    } else if (c == '/' && next == '*') {
                        state = BLOCK_COMMENT;
                        i += 2;
                    } else if (c == '"') {
                        state = STRING;
                        sb.append(c);
                        i++;
                    } else if (c == '\'') {
                        state = CHAR;
                        sb.append(c);
                        i++;
                    } else {
                        sb.append(c);
                        i++;
                    }
                    break;

                case LINE_COMMENT:
                    if (c == '\n' || c == '\r') {
                        state = NORMAL;
                        sb.append(c); // сохраняем перенос строки
                    }
                    i++;
                    break;

                case BLOCK_COMMENT:
                    if (c == '*' && next == '/') {
                        state = NORMAL;
                        i += 2;
                    } else {
                        i++;
                    }
                    break;

                case STRING:
                    sb.append(c);
                    if (c == '\\' && i + 1 < len) {
                        // экранированный символ внутри строки
                        sb.append(text.charAt(i + 1));
                        i += 2;
                    } else {
                        if (c == '"') {
                            state = NORMAL;
                        }
                        i++;
                    }
                    break;

                case CHAR:
                    sb.append(c);
                    if (c == '\\' && i + 1 < len) {
                        // экранированный символ в символьном литерале
                        sb.append(text.charAt(i + 1));
                        i += 2;
                    } else {
                        if (c == '\'') {
                            state = NORMAL;
                        }
                        i++;
                    }
                    break;
            }
        }

        return sb.toString();
    }

    // ----- шаг 3: обрезать все символы с кодом < 33 на краях текста -----
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

    // ----- шаг 4: удалить пустые строки -----
    private static String removeEmptyLines(String text) {
        StringBuilder sb = new StringBuilder(text.length());
        int len = text.length();
        int i = 0;

        while (i < len) {
            int lineStart = i;
            while (i < len && text.charAt(i) != '\n') {
                i++;
            }
            int lineEnd = i;
            if (i < len && text.charAt(i) == '\n') {
                i++; // включаем '\n'
            }

            // Проверяем, нет ли в строке значимых символов (>=33)
            int p = lineStart;
            boolean nonEmpty = false;
            while (p < lineEnd) {
                if (text.charAt(p) >= 33) {
                    nonEmpty = true;
                    break;
                }
                p++;
            }

            if (nonEmpty) {
                sb.append(text, lineStart, i);
            }
        }

        return sb.toString();
    }
}

