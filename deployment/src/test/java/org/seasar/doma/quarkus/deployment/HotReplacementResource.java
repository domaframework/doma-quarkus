package org.seasar.doma.quarkus.deployment;

import java.nio.file.Files;
import java.nio.file.Paths;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.seasar.doma.jdbc.Config;

@Path("/hot")
public class HotReplacementResource {

  public static final String SQL_FILE =
      "META-INF/org/seasar/doma/quarkus/deployment/model/MyEntityDao/select.sql";

  public static final String SCRIPT_FILE =
      "META-INF/org/seasar/doma/quarkus/deployment/model/MyEntityDao/create.script";

  @Inject Config config;

  @GET
  @Path("/sql")
  @Produces(MediaType.TEXT_PLAIN)
  public String sql() throws Exception {
    var method = getClass().getMethod("sql");
    var sqlFile = config.getSqlFileRepository().getSqlFile(method, SQL_FILE, config.getDialect());
    return sqlFile.getSql();
  }

  @GET
  @Path("/script")
  @Produces(MediaType.TEXT_PLAIN)
  public String script() throws Exception {
    var url = config.getScriptFileLoader().loadAsURL(SCRIPT_FILE);
    return Files.readString(Paths.get(url.toURI()));
  }
}
