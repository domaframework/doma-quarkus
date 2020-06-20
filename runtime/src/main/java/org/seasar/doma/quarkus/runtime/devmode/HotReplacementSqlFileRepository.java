package org.seasar.doma.quarkus.runtime.devmode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.jdbc.AbstractSqlFileRepository;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.dialect.Dialect;

public class HotReplacementSqlFileRepository extends AbstractSqlFileRepository {

  private final List<Path> hotReplacementResources;

  public HotReplacementSqlFileRepository(List<Path> hotReplacementResources) {
    this.hotReplacementResources = Collections.unmodifiableList(hotReplacementResources);
  }

  @Override
  protected SqlFile getSqlFileWithCacheControl(Method method, String path, Dialect dialect) {
    return createSqlFile(method, path, dialect);
  }

  @Override
  protected String getSql(String path) {
    for (Path resource : hotReplacementResources) {
      Path resolved = resource.resolve(path);
      if (Files.exists(resolved)) {
        try {
          return String.join("\n", Files.readAllLines(resolved));
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }
    }
    return null;
  }
}
