package org.seasar.doma.quarkus.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class LogConfiguration {

  /** Shows SQL logs. */
  @ConfigItem public boolean sql;

  /** Shows DAO logs. */
  @ConfigItem public boolean dao;

  /** Shows the logs of the failure to close JDBC resource. */
  @ConfigItem public boolean closingFailure;

  @Override
  public String toString() {
    return "DomaConfigurationLog{"
        + "sql="
        + sql
        + ", dao="
        + dao
        + ", closingFailure="
        + closingFailure
        + '}';
  }
}
