package org.seasar.doma.quarkus.runtime;

import io.quarkus.arc.DefaultBean;
import io.quarkus.arc.Unremovable;
import java.util.Objects;
import javax.inject.Singleton;

public interface DataSourceNameResolver {

  String resolve(String candidateName);

  @Singleton
  @DefaultBean
  @Unremovable
  class DefaultDataSourceNameResolver implements DataSourceNameResolver {

    @Override
    public String resolve(String candidateName) {
      return Objects.requireNonNull(candidateName);
    }
  }
}
