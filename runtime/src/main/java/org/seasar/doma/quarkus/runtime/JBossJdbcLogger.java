package org.seasar.doma.quarkus.runtime;

import java.sql.SQLException;
import java.util.function.Supplier;
import org.jboss.logging.Logger;
import org.seasar.doma.jdbc.AbstractJdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;

public class JBossJdbcLogger extends AbstractJdbcLogger<Logger.Level> {

  private final LogConfiguration logConfiguration;

  JBossJdbcLogger() {
    super(Logger.Level.DEBUG);
    this.logConfiguration = new LogConfiguration();
  }

  public JBossJdbcLogger(LogConfiguration logConfiguration) {
    super(Logger.Level.DEBUG);
    this.logConfiguration = logConfiguration;
  }

  @Override
  protected void log(
      Logger.Level level,
      String callerClassName,
      String callerMethodName,
      Throwable throwable,
      Supplier<String> messageSupplier) {
    Logger logger = Logger.getLogger(callerClassName);
    logger.log(level, messageSupplier.get(), throwable);
  }

  @Override
  protected void logSql(
      String callerClassName,
      String callerMethodName,
      Sql<?> sql,
      Logger.Level level,
      Supplier<String> messageSupplier) {
    if (logConfiguration.sql) {
      Logger logger = Logger.getLogger(callerClassName);
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
    if (logConfiguration.dao) {
      Logger logger = Logger.getLogger(callerClassName);
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
    if (logConfiguration.dao) {
      Logger logger = Logger.getLogger(callerClassName);
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
    if (logConfiguration.dao) {
      Logger logger = Logger.getLogger(callerClassName);
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
    if (logConfiguration.dao) {
      Logger logger = Logger.getLogger(callerClassName);
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
    if (logConfiguration.closingFailure) {
      Logger logger = Logger.getLogger(callerClassName);
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
    if (logConfiguration.closingFailure) {
      Logger logger = Logger.getLogger(callerClassName);
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
    if (logConfiguration.closingFailure) {
      Logger logger = Logger.getLogger(callerClassName);
      logger.info(messageSupplier.get(), e);
    } else {
      super.logResultSetClosingFailure(
          callerClassName, callerMethodName, e, level, messageSupplier);
    }
  }
}
