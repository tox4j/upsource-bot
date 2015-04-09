package im.tox.upsourcebot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.kohsuke.github.GitHub;
import org.skife.jdbi.v2.DBI;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import im.tox.upsourcebot.client.GitHubConnector;
import im.tox.upsourcebot.filters.GitHubHMACFilter;
import im.tox.upsourcebot.resources.GitHubWebhookResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.java8.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class UpsourceApplication extends Application<UpsourceConfiguration> {

  public static void main(String[] args) throws Exception {
    new UpsourceApplication().run(args);
  }

  @Override
  public void run(UpsourceConfiguration configuration, Environment environment) throws Exception {
    DBIFactory factory = new DBIFactory();
    DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "data-source");
    GitHub gitHub = GitHub.connectUsingOAuth(configuration.getGitHubOAuthToken());
    ImmutableMap.Builder<String, Repository> repositoryBuilder = ImmutableMap.builder();
    ImmutableMap.Builder<String, ExecutorService> executorBuilder = ImmutableMap.builder();
    configuration.getRepositories().forEach(
        repository -> {
          repositoryBuilder.put(repository.getFullName(), repository);
          executorBuilder.put(repository.getFullName(), Executors.newSingleThreadExecutor());
        });
    List<String> repoNames = configuration.getRepositories().stream().map(Repository::getFullName).
        collect(Collectors.toList());
    GitHubConnector gitHubConnector =
        new GitHubConnector(gitHub, repositoryBuilder.build(), executorBuilder.build());
    gitHubConnector.handleStartup();
    GitHubWebhookResource gitHubWebhookResource =
        new GitHubWebhookResource(gitHubConnector, ImmutableList.copyOf(repoNames));
    environment.jersey().register(
        gitHubWebhookResource);
    environment.jersey().register(new GitHubHMACFilter(configuration.getGitHubWebhookSecret()));
    //environment.jersey().register(new UserResource(jdbi.onDemand(UserDao.class)));
    environment.jersey().register(DeclarativeLinkingFeature.class);
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
    bootstrap.addBundle(new DBIExceptionsBundle());
    bootstrap.addBundle(new ViewBundle<UpsourceConfiguration>() {
      @Override
      public ImmutableMap<String, ImmutableMap<String, String>> getViewConfiguration(
          UpsourceConfiguration configuration) {
        return ImmutableMap.of();
      }
    });
    bootstrap.addBundle(new AssetsBundle());
  }

}
