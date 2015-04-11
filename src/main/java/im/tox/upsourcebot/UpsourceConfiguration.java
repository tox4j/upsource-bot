package im.tox.upsourcebot;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class UpsourceConfiguration extends Configuration {

  @Valid
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();

  @NotBlank
  private String gitHubOAuthToken;

  @NotBlank
  private String gitHubWebhookSecret;

  @Valid
  @NotEmpty
  private List<Repository> repositories;

  @NotBlank
  private String githubName;

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

  @JsonProperty("repos")
  public List<Repository> getRepositories() {
    return repositories;
  }

  @JsonProperty("repos")
  public void setRepositories(List<Repository> repositories) {
    this.repositories = repositories;
  }

  @JsonProperty("githubname")
  public String getGithubName() {
    return githubName;
  }

  @JsonProperty("githubname")
  public void setGithubName(String githubName) {
    this.githubName = githubName;
  }

}
