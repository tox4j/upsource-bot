package im.tox.upsourcebot;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.kohsuke.github.GitHub;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class UpsourceConfiguration extends Configuration {

  @Valid
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();

  @Valid
  @NotNull
  private String gitHubOAuthToken;

  @JsonProperty("database")
  public DataSourceFactory getDataSourceFactory() {
    return database;
  }

  @JsonProperty("database")
  public void setDataSourceFactory(DataSourceFactory database) {
    this.database = database;
  }

  @JsonProperty("githuboauthtoken")
  public String getGitHubOAuthToken() {
    return gitHubOAuthToken;
  }

  @JsonProperty("githuboauthtoken")
  public void setGitHubOAuthToken(String gitHubOAuthToken) {
    this.gitHubOAuthToken = gitHubOAuthToken;
  }

}
