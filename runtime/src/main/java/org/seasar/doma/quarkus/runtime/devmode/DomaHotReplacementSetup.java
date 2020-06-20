package org.seasar.doma.quarkus.runtime.devmode;

import io.quarkus.dev.spi.HotReplacementContext;
import io.quarkus.dev.spi.HotReplacementSetup;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.quarkus.runtime.DomaRecorder;

public class DomaHotReplacementSetup implements HotReplacementSetup {

  @Override
  public void setupHotDeployment(HotReplacementContext hotReplacementContext) {
    Objects.requireNonNull(hotReplacementContext);
    List<Path> resourcesDirs = hotReplacementContext.getResourcesDir();
    DomaRecorder.setHotReplacementResourcesDirs(resourcesDirs);
  }
}
