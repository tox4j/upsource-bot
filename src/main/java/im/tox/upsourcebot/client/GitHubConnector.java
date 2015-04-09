package im.tox.upsourcebot.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.kohsuke.github.GHCommitState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import im.tox.upsourcebot.Reviewer;
import im.tox.upsourcebot.client.tasks.FindReviewerTask;
import im.tox.upsourcebot.client.tasks.GitHubAssignTask;
import im.tox.upsourcebot.client.tasks.GitHubCommitStatusTask;
import im.tox.upsourcebot.client.tasks.GitHubGetOpenPullRequestsTask;
import im.tox.upsourcebot.client.tasks.GitHubGreeterTask;
import im.tox.upsourcebot.client.tasks.GitHubIssueCommentTask;
import im.tox.upsourcebot.client.tasks.RetryingCallable;

public class GitHubConnector {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubConnector.class);

  private final GitHub gitHub;
  private final ImmutableMap<String, ImmutableList<Reviewer>> reviewCandidates;
  private final String greetingsFormat;
  private final ImmutableMap<String, ExecutorService> executors;

  public GitHubConnector(GitHub gitHub,
      ImmutableMap<String, ImmutableList<Reviewer>> reviewCandidates,
      String greetingsFormat, ImmutableMap<String, ExecutorService> executors) {
    this.gitHub = gitHub;
    this.reviewCandidates = reviewCandidates;
    this.greetingsFormat = greetingsFormat;
    this.executors = executors;
  }

  public void commentOnIssue(String repoName, String comment, int issueNumber) {
    executors.get(repoName).submit(RetryingCallable.of(
        new GitHubIssueCommentTask(gitHub, repoName, issueNumber, comment)));
  }

  public void setPendingCommitStatus(String repoName, String commitSHA, String url,
      String description, String context) {
    executors.get(repoName).submit(RetryingCallable
        .of(new GitHubCommitStatusTask(gitHub, repoName, commitSHA, GHCommitState.PENDING, url,
            description, context)));
  }

  public void assignAndGreet(String repoName, String author, int issueNumber) {
    Future<String> reviewer = findReviewer(repoName, author);
    ExecutorService executorService = executors.get(repoName);
    executorService.submit(RetryingCallable
        .of(new GitHubAssignTask(gitHub, repoName, issueNumber, reviewer)));
    executorService.submit(RetryingCallable
        .of(new GitHubGreeterTask(gitHub, repoName, issueNumber, greetingsFormat, reviewer,
            author)));
  }

  public void handleStartup() {
    executors
        .forEach(this::handleRepositoryStartup);
  }

  private Future<String> findReviewer(String repoName, String author) {
    return executors.get(repoName).submit(RetryingCallable
        .of(new FindReviewerTask(gitHub, repoName, reviewCandidates.get(repoName), author)));
  }

  private void handleRepositoryStartup(String repoName, ExecutorService executorService) {
    Future<List<GHPullRequest>> pullRequests = executorService
        .submit(RetryingCallable.of(new GitHubGetOpenPullRequestsTask(gitHub, repoName)));
    executorService.execute(() -> {
      List<GHPullRequest> prs;
      try {
        prs = pullRequests.get();
      } catch (InterruptedException e) {
        LOGGER.error("Pull Request fetch was interrupted", e);
        Thread.currentThread().interrupt();
        return;
      } catch (ExecutionException e) {
        LOGGER.error("Pull Request fetch task was aborted", e);
        return;
      }
      prs.stream().filter(ghPullRequest -> ghPullRequest.getAssignee() == null).forEach(
          ghPullRequest -> assignAndGreet(repoName, ghPullRequest.getUser().getLogin(),
              ghPullRequest.getNumber()));
    });
  }

}
