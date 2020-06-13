package org.seasar.doma.quarkus.runtime.devmode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.seasar.doma.jdbc.ScriptFileLoader;

public class HotReplacementScriptFileLoader implements ScriptFileLoader {

  private final List<Path> hotReplacementResources;

  public HotReplacementScriptFileLoader(List<Path> hotReplacementResources) {
    this.hotReplacementResources = Collections.unmodifiableList(hotReplacementResources);
  }

  @Override
  public URL loadAsURL(String path) {
    for (Path resource : hotReplacementResources) {
      Path resolved = resource.resolve(path);
      if (Files.exists(resolved)) {
        try {
          return resolved.toUri().toURL();
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }
    }
    return null;
  }
}
