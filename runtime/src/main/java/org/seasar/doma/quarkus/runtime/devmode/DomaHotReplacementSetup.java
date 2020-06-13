package org.seasar.doma.quarkus.runtime.devmode;

import io.quarkus.dev.spi.HotReplacementContext;
import io.quarkus.dev.spi.HotReplacementSetup;
import java.nio.file.Path;
import java.util.List;
import org.seasar.doma.quarkus.runtime.DomaRecorder;

public class DomaHotReplacementSetup implements HotReplacementSetup {

  @Override
  public void setupHotDeployment(HotReplacementContext hotReplacementContext) {
    List<Path> resources = hotReplacementContext.getResourcesDir();
    DomaRecorder.setHotReplacementResources(resources);
  }
}
