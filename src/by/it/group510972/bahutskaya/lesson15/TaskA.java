package by.it.group510972.bahutskaya.lesson15;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Смотрим java-файлы без package и import и печатаем размер с путём.
public class TaskA {
    public static void main(String[] args) throws IOException {
        Path root = Path.of(System.getProperty("user.dir"), "src");
        List<FileText> files = new ArrayList<>();
        try (var walk = Files.walk(root)) {
            walk.filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                String text = read(path);
                if (text == null || text.contains("@Test") || text.contains("org.junit.Test")) {
                    return;
                }
                String cleaned = trimControls(removePackageAndImports(text));
                files.add(new FileText(root.relativize(path).toString(), cleaned.getBytes(StandardCharsets.UTF_8).length));
            });
        }
        files.sort(Comparator.comparingInt((FileText file) -> file.size).thenComparing(file -> file.path));
        for (FileText file : files) {
            System.out.println(file.size + " " + file.path);
        }
    }

    static String read(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT);
            try {
                return decoder.decode(ByteBuffer.wrap(bytes)).toString();
            } catch (CharacterCodingException ignored) {
                return new String(bytes, StandardCharsets.ISO_8859_1);
            }
        } catch (IOException ignored) {
            return null;
        }
    }

    static String removePackageAndImports(String text) {
        StringBuilder result = new StringBuilder(text.length());
        int start = 0;
        for (int i = 0; i <= text.length(); i++) {
            if (i != text.length() && text.charAt(i) != '\n') {
                continue;
            }
            String line = text.substring(start, i);
            String trimmed = stripLeft(line);
            // Строки package и import не берём
            if (!trimmed.startsWith("package ") && !trimmed.startsWith("import ")) {
                result.append(line);
                if (i != text.length()) {
                    result.append('\n');
                }
            }
            start = i + 1;
        }
        return result.toString();
    }

    static String stripLeft(String line) {
        int index = 0;
        while (index < line.length() && line.charAt(index) <= ' ') {
            index++;
        }
        return line.substring(index);
    }

    static String trimControls(String text) {
        int start = 0;
        int end = text.length();
        while (start < end && text.charAt(start) < 33) {
            start++;
        }
        while (end > start && text.charAt(end - 1) < 33) {
            end--;
        }
        return text.substring(start, end);
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
