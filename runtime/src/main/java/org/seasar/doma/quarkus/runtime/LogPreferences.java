package org.seasar.doma.quarkus.runtime;

public class LogPreferences {

  private final boolean sql;
  private final boolean dao;
  private final boolean closingFailure;

  public LogPreferences(boolean sql, boolean dao, boolean closingFailure) {
    this.sql = sql;
    this.dao = dao;
    this.closingFailure = closingFailure;
  }

  public boolean isSql() {
    return sql;
  }

  public boolean isDao() {
    return dao;
  }

  public boolean isClosingFailure() {
    return closingFailure;
  }
}
