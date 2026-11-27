package by.it.group510972.bahutskaya.lesson15;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// То же, но ещё выкидываем комментарии и пустые строки.
public class TaskB {
    public static void main(String[] args) throws IOException {
        Path root = Path.of(System.getProperty("user.dir"), "src");
        List<FileText> files = new ArrayList<>();
        try (var walk = Files.walk(root)) {
            walk.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                String text = TaskA.read(path);
                if (text == null || text.contains("@Test") || text.contains("org.junit.Test")) {
                    return;
                }
                String cleaned = removeEmptyLines(TaskA.trimControls(removeComments(TaskA.removePackageAndImports(text))));
                files.add(new FileText(root.relativize(path).toString(), cleaned.getBytes(StandardCharsets.UTF_8).length));
            });
        }
        files.sort(Comparator.comparingInt((FileText file) -> file.size).thenComparing(file -> file.path));
        for (FileText file : files) {
            System.out.println(file.size + " " + file.path);
        }
    }

    static String removeComments(String text) {
        StringBuilder result = new StringBuilder(text.length());
        // Идём по тексту и выкидываем комментарии, строки в кавычках не трогаем
        boolean line = false;
        boolean block = false;
        boolean string = false;
        boolean character = false;
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            char next = i + 1 < text.length() ? text.charAt(i + 1) : 0;
            if (line) {
                if (current == '\n') {
                    line = false;
                    result.append(current);
                }
                continue;
            }
            if (block) {
                if (current == '*' && next == '/') {
                    block = false;
                    i++;
                } else if (current == '\n') {
                    result.append(current);
                }
                continue;
            }
            if (string) {
                result.append(current);
                if (current == '\\' && i + 1 < text.length()) {
                    result.append(text.charAt(++i));
                } else if (current == '"') {
                    string = false;
                }
                continue;
            }
            if (character) {
                result.append(current);
                if (current == '\\' && i + 1 < text.length()) {
                    result.append(text.charAt(++i));
                } else if (current == '\'') {
                    character = false;
                }
                continue;
            }
            if (current == '/' && next == '/') {
                line = true;
                i++;
                continue;
            }
            if (current == '/' && next == '*') {
                block = true;
                i++;
                continue;
            }
            if (current == '"') {
                string = true;
                result.append(current);
                continue;
            }
            if (current == '\'') {
                character = true;
                result.append(current);
                continue;
            }
            result.append(current);
        }
        return result.toString();
    }

    static String removeEmptyLines(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int start = 0;
        boolean any = false;
        for (int i = 0; i <= text.length(); i++) {
            if (i != text.length() && text.charAt(i) != '\n') {
                continue;
            }
            String line = text.substring(start, i);
            if (!TaskA.stripLeft(line).isEmpty()) {
                if (any) {
                    result.append('\n');
                }
                result.append(line.stripTrailing());
                any = true;
            }
            start = i + 1;
        }
        return result.toString();
    }

    private static final class FileText {
        final String path;
        final int size;

        FileText(String path, int size) {
            this.path = path;
            this.size = size;
        }
    }
}
