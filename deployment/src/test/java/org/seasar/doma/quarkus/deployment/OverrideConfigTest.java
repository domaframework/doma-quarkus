package org.seasar.doma.quarkus.deployment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.QuarkusUnitTest;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;

public class OverrideConfigTest {

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
                          "application.properties")
                      .addClasses(MyConfig.class));

  @Inject Config config;

  @ApplicationScoped
  static class MyConfig implements Config {
    @Override
    public DataSource getDataSource() {
      return null;
    }

    @Override
    public Dialect getDialect() {
      return null;
    }
  }

  @Test
  void test() {
    assertTrue(config.toString().contains("MyConfig"));
  }
}
