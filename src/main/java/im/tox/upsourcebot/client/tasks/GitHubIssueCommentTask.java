package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * Task to comment on an issue in a GitHub repository
 */
public class GitHubIssueCommentTask extends GitHubIssueTask {

  protected String comment;

  /**
   * @param gitHub      the GitHub instance for this task
   * @param repoName    full name of the repository
   * @param issueNumber number of the issue to comment on
   * @param comment     the comment to be sent to the issue
   */
  public GitHubIssueCommentTask(GitHub gitHub, String repoName, int issueNumber, String comment) {
    super(gitHub, repoName, issueNumber);
    this.comment = comment;
  }

  @Override
  public Void call() throws IOException, InterruptedException {
    GHRepository repository;
    repository = gitHub.getRepository(repoName);
    GHIssue issue;
    issue = repository.getIssue(issueNumber);
    issue.comment(comment);
    return null;
  }

}
