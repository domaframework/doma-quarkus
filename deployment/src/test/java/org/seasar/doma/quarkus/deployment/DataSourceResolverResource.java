package org.seasar.doma.quarkus.deployment;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.seasar.doma.jdbc.Config;

@Path("/{tenant}")
public class DataSourceResolverResource {

  @Inject Config config;

  @GET
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public String name(@PathParam("id") int id) throws Exception {
    var dataSource = config.getDataSource();
    try (var connection = dataSource.getConnection()) {
      try (var statement = connection.createStatement()) {
        try (var resultSet = statement.executeQuery("select name from employee")) {
          if (resultSet.next()) {
            return resultSet.getString(1);
          }
          throw new IllegalStateException("empty");
        }
      }
    }
  }
}
