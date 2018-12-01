package io.github.binaryfoo.res;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ClasspathIO {

  public static List<String> readLines(String fileName) {
    try (InputStream in = open(fileName)) {
      return IOUtils.readLines(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read " + fileName, e);
    }
  }

  public static InputStream open(String fileName) {
    return ClasspathIO.class.getClassLoader().getResourceAsStream(fileName);
  }
}
