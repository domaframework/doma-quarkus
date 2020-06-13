package org.seasar.doma.quarkus.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.quarkus.test.QuarkusUnitTest;
import javax.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;

public class InjectConfigTest {

  @RegisterExtension
  static QuarkusUnitTest runner =
      new QuarkusUnitTest()
          .setArchiveProducer(
              () ->
                  ShrinkWrap.create(JavaArchive.class)
                      .add(
                          new StringAsset(
                              "quarkus.datasource.db-kind=h2\n"
                                  + "quarkus.datasource.username=USERNAME-NAMED\n"
                                  + "quarkus.datasource.jdbc.url=jdbc:h2:tcp://localhost/mem:testing\n"
                                  + "quarkus.datasource.jdbc.driver=org.h2.Driver\n"),
                          "application.properties"));

  @Inject Config config;

  @Test
  public void test() {
    assertNotNull(config);
    assertNotNull(config.getDataSource());
    assertNotNull(config.getDialect());
    assertNotNull(config.getSqlFileRepository());
    assertNotNull(config.getScriptFileLoader());
    assertNotNull(config.getJdbcLogger());
    assertNotNull(config.getRequiresNewController());
    assertNotNull(config.getClassHelper());
    assertNotNull(config.getCommandImplementors());
    assertNotNull(config.getQueryImplementors());
    assertNotNull(config.getExceptionSqlLogType());
    assertNotNull(config.getUnknownColumnHandler());
    assertNotNull(config.getExceptionSqlLogType());
    assertNotNull(config.getNaming());
    assertNotNull(config.getMapKeyNaming());
    assertNotNull(config.getCommenter());
    assertNotNull(config.getEntityListenerProvider());
    assertNull(config.getTransactionManager());
    assertEquals("<default>", config.getDataSourceName());
    assertEquals(0, config.getBatchSize());
    assertEquals(0, config.getFetchSize());
    assertEquals(0, config.getMaxRows());
    assertEquals(0, config.getQueryTimeout());
  }
}
