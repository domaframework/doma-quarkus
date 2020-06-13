package org.seasar.doma.quarkus.deployment;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.QuarkusUnitTest;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.quarkus.runtime.DbConfig;

public class MultipleConfigTest {
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
                                  + "quarkus.datasource.jdbc.driver=org.h2.Driver\n"
                                  + "quarkus.datasource.inventory.db-kind=h2\n"
                                  + "quarkus.datasource.inventory.username=USERNAME-NAMED\n"
                                  + "quarkus.datasource.inventory.jdbc.url=jdbc:h2:tcp://localhost/mem:testing\n"
                                  + "quarkus.datasource.inventory.jdbc.driver=org.h2.Driver\n"),
                          "application.properties")
                      .addClasses(MyProducer.class));

  @Inject Config defaultConfig;

  @Inject
  @org.seasar.doma.quarkus.Config("inventory")
  Config inventoryConfig;

  static class MyProducer {

    @Singleton
    @org.seasar.doma.quarkus.Config("inventory")
    Config inventoryConfig(
        @io.quarkus.agroal.DataSource("inventory") AgroalDataSource dataSource,
        @Default DbConfig config) {
      return config.newBuilder().setDataSource(dataSource).setDataSourceName("inventory").build();
    }
  }

  @Test
  void test() {
    assertNotNull(defaultConfig);
    assertNotNull(inventoryConfig);
    assertNotEquals(defaultConfig, inventoryConfig);
  }
}
