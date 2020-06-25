package org.seasar.doma.quarkus.deployment;

import io.quarkus.builder.item.SimpleBuildItem;
import java.util.Objects;
import org.seasar.doma.quarkus.runtime.DomaSettings;

public final class DomaSettingsBuildItem extends SimpleBuildItem {

  private final DomaSettings settings;

  public DomaSettingsBuildItem(DomaSettings settings) {
    this.settings = Objects.requireNonNull(settings);
  }

  public DomaSettings getSettings() {
    return settings;
  }
}
