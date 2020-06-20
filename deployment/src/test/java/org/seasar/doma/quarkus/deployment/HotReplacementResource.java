package org.seasar.doma.quarkus.deployment;

import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.seasar.doma.jdbc.ScriptFileLoader;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;

@Path("/hot")
public class HotReplacementResource {

  public static final String SQL_FILE =
      "META-INF/org/seasar/doma/quarkus/deployment/MyEntityDao/select.sql";

  public static final String SCRIPT_FILE =
      "META-INF/org/seasar/doma/quarkus/deployment/MyEntityDao/create.script";

  @Inject Dialect dialect;
  @Inject SqlFileRepository sqlFileRepository;
  @Inject ScriptFileLoader scriptFileLoader;

  @GET
  @Path("/sql")
  @Produces(MediaType.TEXT_PLAIN)
  public String sql() throws Exception {
    Method method = getClass().getMethod("sql");
    SqlFile sqlFile = sqlFileRepository.getSqlFile(method, SQL_FILE, dialect);
    return sqlFile.getSql();
  }

  @GET
  @Path("/script")
  @Produces(MediaType.TEXT_PLAIN)
  public String script() throws Exception {
    URL url = scriptFileLoader.loadAsURL(SCRIPT_FILE);
    return String.join("\n", Files.readAllLines(Paths.get(url.toURI())));
  }
}
