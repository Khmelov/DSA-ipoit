package by.it.group410972.kapur.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class SourceScannerA {

  private record Entry(long size, String path) {}

  public static void main(String[] args) throws Exception {
    Path root = Path.of(System.getProperty("user.dir")
        + File.separator + "src" + File.separator);

    List<Entry> result = new ArrayList<>();

    try (var walk = Files.walk(root)) {
      walk.filter(p -> p.toString().endsWith(".java"))
          .forEach(p -> {
            String text = readIgnoringMalformed(p);
            if (text == null) return;

            if (text.contains("@Test") || text.contains("org.junit.Test")) {
              return;
            }

            String processed = process(text);
            byte[] bytes = processed.getBytes(StandardCharsets.UTF_8);
            long size = bytes.length;

            String rel = root.relativize(p).toString();
            result.add(new Entry(size, rel));
          });
    }

    result.sort((a, b) -> {
      int cmp = Long.compare(a.size, b.size);
      if (cmp != 0) return cmp;
      return a.path.compareTo(b.path);
    });

    for (Entry e : result) {
      System.out.println(e.size + " " + e.path);
    }
  }

  /** Чтение файла с игнорированием MalformedInputException */
  private static String readIgnoringMalformed(Path p) {
    try {
      byte[] bytes = Files.readAllBytes(p);

      CharsetDecoder decoder = StandardCharsets.UTF_8
          .newDecoder()
          .onMalformedInput(CodingErrorAction.IGNORE)
          .onUnmappableCharacter(CodingErrorAction.IGNORE);

      CharBuffer cb = decoder.decode(ByteBuffer.wrap(bytes));
      return cb.toString();

    } catch (IOException e) {
      return null;
    }
  }

  /** Удаление package/import + trimming control chars < 33 */
  private static String process(String text) {
    StringBuilder sb = new StringBuilder();
    String[] lines = text.split("\n", -1);

    for (String line : lines) {
      String t = stripLeading(line);
      if (t.startsWith("package ") || t.startsWith("import ")) {
        continue;
      }
      sb.append(line).append('\n');
    }

    String res = sb.toString();
    return trimLow(res);
  }

  /** Удаляем ведущие пробелы быстро (без regex) */
  private static String stripLeading(String s) {
    int i = 0;
    while (i < s.length() && s.charAt(i) <= ' ') i++;
    return s.substring(i);
  }

  /** Удаление символов с кодом < 33 с начала и конца */
  private static String trimLow(String s) {
    int start = 0;
    int end = s.length() - 1;

    while (start <= end && s.charAt(start) < 33) start++;
    while (end >= start && s.charAt(end) < 33) end--;

    return (start > end) ? "" : s.substring(start, end + 1);
  }
}
