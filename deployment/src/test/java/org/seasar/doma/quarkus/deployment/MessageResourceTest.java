package org.seasar.doma.quarkus.deployment;

import static org.hamcrest.core.Is.is;

import io.quarkus.test.QuarkusUnitTest;
import io.restassured.RestAssured;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.seasar.doma.quarkus.deployment.model.Locale;
import org.seasar.doma.quarkus.deployment.model.Message;
import org.seasar.doma.quarkus.deployment.model.MessageDao;
import org.seasar.doma.quarkus.deployment.model.MessageDaoImpl;
import org.seasar.doma.quarkus.deployment.model.MessageRepository;
import org.seasar.doma.quarkus.deployment.model.Message_;
import org.seasar.doma.quarkus.deployment.model.Text;
import org.seasar.doma.quarkus.deployment.model._Locale;
import org.seasar.doma.quarkus.deployment.model._Message;
import org.seasar.doma.quarkus.deployment.model._Text;

public class MessageResourceTest {

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
                                  + "quarkus.doma.sql-load-script=model.sql\n"
                                  + "quarkus.doma.log.sql=true\n"),
                          "application.properties")
                      .addAsResource("model.sql")
                      .addAsResource(
                          "META-INF/org/seasar/doma/quarkus/deployment/model/MessageDao/selectById.sql")
                      .addClasses(Text.class, _Text.class)
                      .addClasses(Locale.class, _Locale.class)
                      .addClasses(Message.class, Message_.class, _Message.class)
                      .addClasses(MessageDao.class, MessageDaoImpl.class)
                      .addClass(MessageRepository.class)
                      .addClass(MessageResource.class));

  @Test
  void testCriteriaAPI() {
    RestAssured.when().get("/hello").then().body(is("hello"));
  }

  @Test
  void testSqlFile() {
    RestAssured.when().get("/hello/1").then().body(is("hello"));
    RestAssured.when().get("/hello/2").then().body(is("世界"));
  }
}
