package im.tox.upsourcebot;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class UpsourceConfiguration extends Configuration {

  @Valid
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();

  @NotNull
  private String gitHubOAuthToken;

  @NotNull
  private String gitHubWebhookSecret;

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

  @JsonProperty("githubwebhooksecret")
  public String getGitHubWebhookSecret() {
    return gitHubWebhookSecret;
  }

  @JsonProperty("githubwebhooksecret")
  public void setGitHubWebhookSecret(String gitHubWebhookSecret) {
    this.gitHubWebhookSecret = gitHubWebhookSecret;
  }

}
