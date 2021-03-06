package org.seasar.doma.quarkus.deployment.model;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;

@Dao
public interface MessageDao {

  @Select
  Message selectById(int id);
}
