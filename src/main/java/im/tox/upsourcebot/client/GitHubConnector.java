package im.tox.upsourcebot.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.kohsuke.github.GHCommitState;
import org.kohsuke.github.GitHub;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import im.tox.upsourcebot.Reviewer;
import im.tox.upsourcebot.client.tasks.GitHubAssignTask;
import im.tox.upsourcebot.client.tasks.GitHubCommitStatusTask;
import im.tox.upsourcebot.client.tasks.GitHubIssueCommentTask;
import im.tox.upsourcebot.client.tasks.RetryingCallable;

public class GitHubConnector {

  private static final int SLOT_TIME = 5000;
  private static final int CEILING = 16;

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
    String reviewer = findReviewer(repoName, author);
    executors.get(repoName).submit(RetryingCallable
        .of(new GitHubAssignTask(gitHub, repoName, issueNumber, reviewer)));
    commentOnIssue(repoName, String.format(greetingsFormat, author, reviewer), issueNumber);
  }

  private String findReviewer(String repoName, String author) {
    List<Reviewer> candidates = reviewCandidates.get(repoName);
    Reviewer reviewer;
    if (candidates.size() == 1) {
      reviewer = candidates.get(0);
    } else {
      candidates = candidates.stream().filter(r -> !r.getName().equals(author)).collect(
          Collectors.toList());
      reviewer = candidates.get(new Random().nextInt(candidates.size()));
    }
    return reviewer.getName();
  }

}
