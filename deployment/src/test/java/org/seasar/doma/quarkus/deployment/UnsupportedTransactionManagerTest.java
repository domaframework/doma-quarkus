package org.seasar.doma.quarkus.deployment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.QuarkusUnitTest;
import javax.inject.Inject;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class UnsupportedTransactionManagerTest {

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

  @Inject TransactionManager tm;

  @Test
  void test() {
    var ex =
        assertThrows(
            UnsupportedOperationException.class,
            () -> {
              tm.required(() -> {});
            });
    assertTrue(ex.getMessage().startsWith("Use quarkus-narayana-jta"));
  }
}
