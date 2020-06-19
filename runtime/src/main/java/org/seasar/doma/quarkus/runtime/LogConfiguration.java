package org.seasar.doma.quarkus.runtime;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import org.jboss.logging.Logger;

@ConfigGroup
public class LogConfiguration {

  /** */
  @ConfigItem(defaultValue = "DEBUG")
  public Logger.Level level = Logger.Level.DEBUG;

  /** */
  @ConfigItem public boolean sql;

  /** */
  @ConfigItem public boolean dao;

  /** */
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
