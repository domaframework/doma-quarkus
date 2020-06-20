package org.seasar.doma.quarkus.deployment;

import io.quarkus.builder.item.SimpleBuildItem;
import java.util.Objects;
import org.seasar.doma.quarkus.runtime.DomaConfiguration;

public final class MergedConfigurationBuildItem extends SimpleBuildItem {

  private final DomaConfiguration configuration;

  public MergedConfigurationBuildItem(DomaConfiguration configuration) {
    this.configuration = Objects.requireNonNull(configuration);
  }

  public DomaConfiguration getConfiguration() {
    return configuration;
  }
}
