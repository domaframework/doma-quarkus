package org.seasar.doma.quarkus.runtime.devmode;

import io.quarkus.dev.spi.HotReplacementContext;
import io.quarkus.dev.spi.HotReplacementSetup;
import java.util.Objects;
import org.seasar.doma.quarkus.runtime.DomaRecorder;

public class DomaHotReplacementSetup implements HotReplacementSetup {

  @Override
  public void setupHotDeployment(HotReplacementContext hotReplacementContext) {
    Objects.requireNonNull(hotReplacementContext);
    var resourcesDirs = hotReplacementContext.getResourcesDir();
    DomaRecorder.setHotReplacementResourcesDirs(resourcesDirs);
  }
}
