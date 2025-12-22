package by.it.group410972.kapur.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class SourceScannerB {

  private static class Result {
    final String path;
    final int size;

    Result(String path, int size) {
      this.path = path;
      this.size = size;
    }
  }

  public static void main(String[] args) {
    String src = System.getProperty("user.dir")
        + File.separator + "src" + File.separator;

    Path root = Path.of(src);
    List<Result> results = new ArrayList<>();

    try (Stream<Path> walk = Files.walk(root)) {
      walk.filter(p -> p.toString().endsWith(".java"))
          .forEach(p -> processFile(root, p, results));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    results.sort(
        Comparator.comparingInt((Result r) -> r.size)
            .thenComparing(r -> r.path)
    );

    for (Result r : results) {
      System.out.println(r.size + " " + r.path);
    }
  }

  private static void processFile(Path root, Path file, List<Result> results) {
    String text;
    try {
      text = Files.readString(file);
    } catch (MalformedInputException e) {
      // корректная обработка: пропускаем файл
      return;
    } catch (IOException e) {
      return;
    }

    if (text.contains("@Test") || text.contains("org.junit.Test")) {
      return;
    }

    text = removePackageAndImports(text);
    text = removeComments(text);
    text = trimControlChars(text);
    text = removeEmptyLines(text);

    int size = text.getBytes().length;
    String relativePath = root.relativize(file).toString();

    results.add(new Result(relativePath, size));
  }

  // 1. Удаление package и import за O(n)
  private static String removePackageAndImports(String s) {
    StringBuilder result = new StringBuilder();
    Scanner scanner = new Scanner(s);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String trimmed = line.trim();
      if (trimmed.startsWith("package ") || trimmed.startsWith("import ")) {
        continue;
      }
      result.append(line).append('\n');
    }
    return result.toString();
  }

  // 2. Удаление комментариев за O(n)
  private static String removeComments(String s) {
    StringBuilder out = new StringBuilder();
    boolean inBlock = false;
    boolean inLine = false;

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      char next = i + 1 < s.length() ? s.charAt(i + 1) : 0;

      if (inBlock) {
        if (c == '*' && next == '/') {
          inBlock = false;
          i++;
        }
        continue;
      }

      if (inLine) {
        if (c == '\n') {
          inLine = false;
          out.append(c);
        }
        continue;
      }

      if (c == '/' && next == '*') {
        inBlock = true;
        i++;
        continue;
      }

      if (c == '/' && next == '/') {
        inLine = true;
        i++;
        continue;
      }

      out.append(c);
    }
    return out.toString();
  }

  // 3. Удаление символов с кодом <33 в начале и конце
  private static String trimControlChars(String s) {
    int start = 0;
    int end = s.length();

    while (start < end && s.charAt(start) < 33) start++;
    while (end > start && s.charAt(end - 1) < 33) end--;

    return s.substring(start, end);
  }

  // 4. Удаление пустых строк
  private static String removeEmptyLines(String s) {
    StringBuilder result = new StringBuilder();
    Scanner scanner = new Scanner(s);

    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      if (!line.trim().isEmpty()) {
        result.append(line).append('\n');
      }
    }
    return result.toString();
  }
}
