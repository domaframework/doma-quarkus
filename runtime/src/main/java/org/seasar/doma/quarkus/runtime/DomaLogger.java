package org.seasar.doma.quarkus.runtime;

import java.sql.SQLException;
import java.util.Objects;
import java.util.function.Supplier;
import org.jboss.logging.Logger;
import org.seasar.doma.jdbc.AbstractJdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;

public class DomaLogger extends AbstractJdbcLogger<Logger.Level> {

  private final LogPreferences logPreferences;

  public DomaLogger(LogPreferences logPreferences) {
    super(Logger.Level.DEBUG);
    this.logPreferences = Objects.requireNonNull(logPreferences);
  }

  public LogPreferences getLogPreferences() {
    return logPreferences;
  }

  @Override
  protected void log(
      Logger.Level level,
      String callerClassName,
      String callerMethodName,
      Throwable throwable,
      Supplier<String> messageSupplier) {
    var logger = Logger.getLogger(callerClassName);
    if (logger.isEnabled(level)) {
      logger.log(level, messageSupplier.get(), throwable);
    }
  }

  @Override
  protected void logSql(
      String callerClassName,
      String callerMethodName,
      Sql<?> sql,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isSql()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get());
    } else {
      super.logSql(callerClassName, callerMethodName, sql, level, messageSupplier);
    }
  }

  @Override
  protected void logDaoMethodEntering(
      String callerClassName,
      String callerMethodName,
      Object[] args,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isDao()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get());
    } else {
      super.logDaoMethodEntering(callerClassName, callerMethodName, args, level, messageSupplier);
    }
  }

  @Override
  protected void logDaoMethodExiting(
      String callerClassName,
      String callerMethodName,
      Object result,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isDao()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get());
    } else {
      super.logDaoMethodExiting(callerClassName, callerMethodName, result, level, messageSupplier);
    }
  }

  @Override
  protected void logDaoMethodThrowing(
      String callerClassName,
      String callerMethodName,
      RuntimeException e,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isDao()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get(), e);
    } else {
      super.logDaoMethodThrowing(callerClassName, callerMethodName, e, level, messageSupplier);
    }
  }

  @Override
  protected void logSqlExecutionSkipping(
      String callerClassName,
      String callerMethodName,
      SqlExecutionSkipCause cause,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isDao()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get());
    } else {
      super.logSqlExecutionSkipping(
          callerClassName, callerMethodName, cause, level, messageSupplier);
    }
  }

  @Override
  protected void logConnectionClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isClosingFailure()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get(), e);
    } else {
      super.logConnectionClosingFailure(
          callerClassName, callerMethodName, e, level, messageSupplier);
    }
  }

  @Override
  protected void logStatementClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isClosingFailure()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get(), e);
    } else {
      super.logStatementClosingFailure(
          callerClassName, callerMethodName, e, level, messageSupplier);
    }
  }

  @Override
  protected void logResultSetClosingFailure(
      String callerClassName,
      String callerMethodName,
      SQLException e,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logPreferences.isClosingFailure()) {
      var logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get(), e);
    } else {
      super.logResultSetClosingFailure(
          callerClassName, callerMethodName, e, level, messageSupplier);
    }
  }
}
