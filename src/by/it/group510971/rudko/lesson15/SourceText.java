package by.it.group510971.rudko.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

//Общие действия с текстами .java файлов. Все операции только читают файлы на диске.
class SourceText {

    //каталог src проекта: из него берутся относительные пути
    static Path srcRoot() {
        return Path.of(System.getProperty("user.dir"), "src");
    }

    //все файлы *.java в каталоге и подкаталогах, по пути
    static List<Path> javaFiles(Path root) throws IOException {
        try (Stream<Path> walk = Files.walk(root)) {
            return walk.filter(p -> p.toString().endsWith(".java") && Files.isRegularFile(p))
                    .sorted()
                    .toList();
        }
    }

    //читаем как UTF-8; байты, которые не являются UTF-8, заменяются символом U+FFFD, исключения нет
    static String read(Path file) throws IOException {
        return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    }

    //тесты (содержат @Test или org.junit.Test) в обработке не участвуют
    static boolean isTest(String text) {
        return text.contains("@Test") || text.contains("org.junit.Test");
    }

    //удаляет строки package и import за один проход по строкам
    static String removePackageAndImports(String text) {
        StringBuilder out = new StringBuilder(text.length());
        int start = 0;
        while (start < text.length()) {
            int end = text.indexOf('\n', start);
            int next = (end == -1) ? text.length() : end + 1;
            String line = text.substring(start, next);
            String t = line.stripLeading();
            if (!(t.startsWith("package ") || t.startsWith("import "))) {
                out.append(line);
            }
            start = next;
        }
        return out.toString();
    }

    //удаляет комментарии за один проход (строки, символы и текстовые блоки """ не трогаем)
    static String removeComments(String text) {
        StringBuilder out = new StringBuilder(text.length());
        int n = text.length();
        int i = 0;
        while (i < n) {
            char c = text.charAt(i);
            if (c == '/' && i + 1 < n && text.charAt(i + 1) == '/') {          //строчный комментарий
                i += 2;
                while (i < n && text.charAt(i) != '\n') i++;
            } else if (c == '/' && i + 1 < n && text.charAt(i + 1) == '*') {   //блочный комментарий
                i += 2;
                while (i < n && !(text.charAt(i) == '*' && i + 1 < n && text.charAt(i + 1) == '/')) {
                    if (text.charAt(i) == '\n') out.append('\n');              //строки сохраняем
                    i++;
                }
                i += 2;
            } else if (text.startsWith("\"\"\"", i)) {                          //текстовый блок
                int close = i + 3;
                while (close < n && !text.startsWith("\"\"\"", close)) {
                    close += (text.charAt(close) == '\\') ? 2 : 1;
                }
                int stop = Math.min(n, close + 3);
                out.append(text, i, stop);
                i = stop;
            } else if (c == '"' || c == '\'') {                                 //строка или символ
                int j = i + 1;
                while (j < n && text.charAt(j) != c && text.charAt(j) != '\n') {
                    j += (text.charAt(j) == '\\') ? 2 : 1;
                }
                int stop = Math.min(n, j + 1);
                out.append(text, i, stop);
                i = stop;
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString();
    }

    //удаляет пустые (и состоящие только из пробелов) строки
    static String removeEmptyLines(String text) {
        StringBuilder out = new StringBuilder(text.length());
        for (String line : text.split("\n", -1)) {
            if (!line.isBlank()) {
                out.append(line).append('\n');
            }
        }
        return out.toString();
    }

    //убирает символы с кодом меньше 33 (пробел, управляющие) в начале и конце текста
    static String trimControl(String text) {
        return text.trim();
    }

    //заменяет каждую последовательность символов с кодом меньше 33 на один пробел
    static String collapseControl(String text) {
        StringBuilder out = new StringBuilder(text.length());
        boolean inGap = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 33) {
                if (!inGap) out.append(' ');
                inGap = true;
            } else {
                out.append(c);
                inGap = false;
            }
        }
        return out.toString();
    }
}
