package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Task to greet a pull request author
 */
public class GitHubGreeterTask extends GitHubIssueCommentTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubGreeterTask.class);

  private String format;
  private Future<String> futureReviewer;
  private String author;

  /**
   * @param gitHub      the GitHub instance for this task
   * @param repoName    full name of the repository
   * @param issueNumber number of the issue to comment on
   * @param format      string format for the greeting comment
   * @param reviewer    the reviewer for this pull request
   * @param author      the author of this pull request
   */
  public GitHubGreeterTask(GitHub gitHub, String repoName, int issueNumber,
      String format, Future<String> reviewer, String author) {
    super(gitHub, repoName, issueNumber, null);
    this.format = format;
    this.futureReviewer = reviewer;
    this.author = author;
  }

  @Override
  public Void call() throws IOException, InterruptedException {
    String reviewer;
    try {
      reviewer = futureReviewer.get();
    } catch (ExecutionException e) {
      LOGGER.error("Assignee task was aborted", e);
      // Wrap in IOException for lack of variadic generics
      throw new IOException(e);
    }
    comment = String.format(format, author, reviewer);
    return super.call();
  }

}
