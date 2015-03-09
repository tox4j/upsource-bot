package im.tox.upsourcebot.client;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GitHubConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubConnector.class);

  private GitHub gitHub;

  public GitHubConnector(GitHub gitHub) {
    this.gitHub = gitHub;
  }

  public void commentOnIssue(String repoName, String comment, int issueNumber) {
    new Thread(() -> {
      GHRepository repository;
      try {
        repository = gitHub.getRepository(repoName);
      } catch (IOException e) {
        LOGGER.error("Unable to connect to repository: " + repoName);
        return;
      }
      GHIssue issue;
      try {
        issue = repository.getIssue(issueNumber);
      } catch (IOException e) {
        LOGGER.error("Unable to to find issue number " + issueNumber);
        return;
      }
      try {
        issue.comment(comment);
      } catch (IOException e) {
        LOGGER.error("Unable to comment on issue " + issueNumber + ": " + comment);
      }
    }
    ).start();
  }

}
