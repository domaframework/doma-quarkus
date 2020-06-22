package org.seasar.doma.quarkus.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.QuarkusUnitTest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class InitialScriptLoaderCustomTest {

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
                                  + "quarkus.doma.sql-load-script=import2.sql\n"
                                  + "quarkus.doma.log.sql=true\n"),
                          "application.properties")
                      .addAsResource("import2.sql"));

  @Inject DataSource dataSource;

  @Test
  public void test() throws Exception {
    int count = 0;
    try (Connection connection = dataSource.getConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery("select id from department")) {
          while (resultSet.next()) {
            count++;
          }
        }
      }
    }
    assertEquals(2, count);
  }
}