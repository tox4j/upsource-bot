package im.tox.upsourcebot.web;

import org.skife.jdbi.v2.DBI;

import im.tox.upsourcebot.web.jdbi.UpsourceInstanceDAO;
import im.tox.upsourcebot.web.resources.UpsourceInstanceResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.java8.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class UpsourceApplication extends Application<UpsourceConfiguration> {

  public static void main(String[] args) throws Exception {
    new UpsourceApplication().run(args);
  }

  @Override
  public void run(UpsourceConfiguration configuration, Environment environment) throws Exception {
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");
    final UpsourceInstanceDAO upsourceInstanceDAO = jdbi.onDemand(UpsourceInstanceDAO.class);
    environment.jersey().register(new UpsourceInstanceResource(upsourceInstanceDAO));
  }

  @Override
  public String getName() {
    return "upsource-bot";
  }

  @Override
  public void initialize(Bootstrap<UpsourceConfiguration> bootstrap) {
    bootstrap.addBundle(new MigrationsBundle<UpsourceConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(UpsourceConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    });
    bootstrap.addBundle(new Java8Bundle());
  }
}
