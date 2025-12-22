package by.it.group410972.kapur;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class HomeWork {

  protected String output;

  protected Class<?> findClass(String className) {
    try {
      return Class.forName(getClass().getPackageName() + "." + className);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Class not found: " + className, e);
    }
  }

  protected HomeWork run(String input) {
    return run(input, true);
  }

  protected HomeWork run(String input, boolean trim) {
    PrintStream originalOut = System.out;
    InputStream originalIn = System.in;

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(buffer));

      invokeMainFromTest();

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      System.setOut(originalOut);
      System.setIn(originalIn);
    }

    output = buffer.toString(StandardCharsets.UTF_8);
    if (trim) {
      output = output.trim();
    }

    return this;
  }

  private void invokeMainFromTest() throws Exception {
    String testMethodName = detectTestMethodName();
    String className = testMethodName.substring("test".length());

    Class<?> clazz = findClass(className);
    Method mainMethod = clazz.getMethod("main", String[].class);
    mainMethod.invoke(null, (Object) new String[0]);
  }

  private String detectTestMethodName() {
    for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
      if (element.getClassName().equals(getClass().getName())
          && element.getMethodName().startsWith("test")) {
        return element.getMethodName();
      }
    }
    throw new RuntimeException("Cannot determine test method name");
  }

  public HomeWork include(String text) {
    if (!output.contains(text)) {
      throw new AssertionError(
          "\nExpected to include:\n" + text +
              "\nActual output:\n" + output
      );
    }
    return this;
  }

  public HomeWork exclude(String text) {
    if (output.contains(text)) {
      throw new AssertionError(
          "\nExpected to exclude:\n" + text +
              "\nActual output:\n" + output
      );
    }
    return this;
  }
}
