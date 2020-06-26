package org.seasar.doma.quarkus.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Objects;
import java.util.function.Supplier;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.command.ScriptCommand;
import org.seasar.doma.jdbc.query.ScriptQuery;

public class ScriptExecutor {

  private static final String METHOD_NAME = "execute";
  private final Config config;
  private final String path;

  public ScriptExecutor(Config config, String path) {
    this.config = Objects.requireNonNull(config);
    this.path = path;
    if (path != null) {
      execute();
    }
  }

  private void execute() {
    Method method = getMethod();
    ScriptExecutorQuery query = new ScriptExecutorQuery(path, method);
    query.prepare();
    ScriptCommand command = config.getCommandImplementors().createScriptCommand(method, query);
    command.execute();
    query.complete();
  }

  private Method getMethod() {
    try {
      return getClass().getDeclaredMethod(METHOD_NAME);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }

  public class ScriptExecutorQuery implements ScriptQuery {
    private final String path;
    private final URL url;
    private final Method method;

    public ScriptExecutorQuery(String path, Method method) {
      this.path = Objects.requireNonNull(path);
      this.url = config.getScriptFileLoader().loadAsURL(path);
      this.method = Objects.requireNonNull(method);
    }

    @Override
    public URL getScriptFileUrl() {
      return url;
    }

    @Override
    public Supplier<Reader> getReaderSupplier() {
      return () -> {
        try {
          InputStream inputStream = url.openStream();
          return new InputStreamReader(inputStream, Constants.UTF_8);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      };
    }

    @Override
    public String getScriptFilePath() {
      return path;
    }

    @Override
    public String getBlockDelimiter() {
      return config.getDialect().getScriptBlockDelimiter();
    }

    @Override
    public boolean getHaltOnError() {
      return true;
    }

    @Override
    public SqlLogType getSqlLogType() {
      return SqlLogType.FORMATTED;
    }

    @Override
    public Sql<?> getSql() {
      return null;
    }

    @Override
    public String getClassName() {
      return method.getDeclaringClass().getName();
    }

    @Override
    public String getMethodName() {
      return method.getName();
    }

    @Override
    public Method getMethod() {
      return method;
    }

    @Override
    public Config getConfig() {
      return config;
    }

    @Override
    public int getQueryTimeout() {
      return config.getQueryTimeout();
    }

    @Override
    public String comment(String sql) {
      return sql;
    }

    @Override
    public void prepare() {}

    @Override
    public void complete() {}
  }
}
