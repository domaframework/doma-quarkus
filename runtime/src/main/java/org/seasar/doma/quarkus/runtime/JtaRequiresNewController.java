package org.seasar.doma.quarkus.runtime;

import io.quarkus.arc.DefaultBean;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import org.seasar.doma.jdbc.RequiresNewController;

@ApplicationScoped
@DefaultBean
public class JtaRequiresNewController implements RequiresNewController {
  @Override
  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public <R> R requiresNew(Callback<R> callback) throws Throwable {
    return RequiresNewController.super.requiresNew(callback);
  }
}
