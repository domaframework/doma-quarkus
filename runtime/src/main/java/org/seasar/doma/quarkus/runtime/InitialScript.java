package org.seasar.doma.quarkus.runtime;

import java.util.Objects;

public class InitialScript {

  public static final String DEFAULT = "import.sql";
  public static final String NO_FILE = "no-file";

  private final String path;

  public InitialScript(String path) {
    this.path = Objects.requireNonNull(path);
  }

  public String getPath() {
    return path;
  }
}
