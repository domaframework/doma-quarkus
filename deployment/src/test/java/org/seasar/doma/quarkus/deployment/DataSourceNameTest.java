package org.seasar.doma.quarkus.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.test.QuarkusUnitTest;
import javax.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;

public class DataSourceNameTest {

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
                                  + "quarkus.datasource.inventory.jdbc.driver=org.h2.Driver\n"
                                  + "quarkus.doma.datasource-name=inventory\n"),
                          "application.properties"));

  @Inject Config config;

  @Inject AgroalDataSource defaultDataSource;

  @Inject
  @DataSource("inventory")
  AgroalDataSource inventoryDataSource;

  @Test
  void test() {
    assertNotNull(config.getDataSource());
    assertNotEquals(config.getDataSource(), defaultDataSource);
    assertEquals(config.getDataSource(), inventoryDataSource);
  }
}
