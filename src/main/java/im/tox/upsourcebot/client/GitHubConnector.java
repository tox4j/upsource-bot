package im.tox.upsourcebot.client;

import org.kohsuke.github.GHCommitState;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import im.tox.upsourcebot.client.tasks.BackoffStrategy;
import im.tox.upsourcebot.client.tasks.ExponentialBackoffStrategy;
import im.tox.upsourcebot.client.tasks.GitHubCommitStatusTask;
import im.tox.upsourcebot.client.tasks.GitHubIssueCommentTask;
import im.tox.upsourcebot.client.tasks.RetryUtil;

public class GitHubConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubConnector.class);
  private static final int MAX_RETRIES = 10;
  private static final int SLOT_TIME = 5000;
  private static final int CEILING = 16;

  private GitHub gitHub;


  public GitHubConnector(GitHub gitHub) {
    this.gitHub = gitHub;
  }

  public void commentOnIssue(String repoName, String comment, int issueNumber) {
    try {
      RetryUtil
          .retryExecute(new GitHubIssueCommentTask(gitHub, repoName, issueNumber, comment),
              getDefaultBackoffStrategy());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error("GitHub comment thread got interrupted", e);
    } catch (IOException e) {
      LOGGER.error("Commenting failed after " + MAX_RETRIES + " attempts", e);
    }
  }

  public void setPendingCommitStatus(String repoName, String commitSHA, String url,
      String description, String context) {
    try {
      RetryUtil.retryExecute(
          new GitHubCommitStatusTask(gitHub, repoName, commitSHA, GHCommitState.PENDING, url,
              description, context),
          getDefaultBackoffStrategy());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error("GitHub comment thread got interrupted", e);
    } catch (IOException e) {
      LOGGER.error("Setting commit status failed after " + MAX_RETRIES + " attempts", e);
    }
  }

  private static BackoffStrategy getDefaultBackoffStrategy() {
    return new ExponentialBackoffStrategy(MAX_RETRIES, SLOT_TIME, CEILING);
  }

}
