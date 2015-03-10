package im.tox.upsourcebot.client;

import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import im.tox.upsourcebot.client.tasks.ExponentialBackoffStrategy;
import im.tox.upsourcebot.client.tasks.GitHubCommentTask;
import im.tox.upsourcebot.client.tasks.RetryUtil;

public class GitHubConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubConnector.class);

  private GitHub gitHub;

  private int maxRetries = 10;

  public GitHubConnector(GitHub gitHub) {
    this.gitHub = gitHub;
  }

  public void commentOnIssue(String repoName, String comment, int issueNumber) {
    try {
      RetryUtil
          .retryExecute(new GitHubCommentTask(gitHub, repoName, issueNumber, comment),
                        new ExponentialBackoffStrategy(maxRetries, 5000, 16));
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error("GitHub comment thread got interrupted", e);
    } catch (IOException e) {
      LOGGER.error("Commenting failed after " + maxRetries + " attempts", e);
    }
  }

  public void setCommitStatus() {
  }

}
