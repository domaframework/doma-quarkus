package org.seasar.doma.quarkus.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.test.QuarkusUnitTest;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class ScriptExecutorNoFileTest {

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
                                  + "quarkus.doma.sql-load-script=no-file\n"
                                  + "quarkus.doma.log.sql=true\n"),
                          "application.properties")
                      .addAsResource("import.sql"));

  @Inject DataSource dataSource;

  @Test
  public void test() throws Exception {
    Integer count = null;
    try (var connection = dataSource.getConnection()) {
      try (var statement = connection.createStatement()) {
        try (var resultSet =
            statement.executeQuery(
                "select count(*) from information_schema.tables where table_name = 'EMPLOYEE'")) {
          if (resultSet.next()) {
            count = resultSet.getInt(1);
          }
        }
      }
    }
    assertEquals(0, count);
  }
}
