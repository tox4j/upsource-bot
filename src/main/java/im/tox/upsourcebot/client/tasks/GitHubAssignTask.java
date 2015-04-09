package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

/**
 * Task to assign an issue (pull requests are also issues) to a specified user
 */
public class GitHubAssignTask extends GitHubIssueTask {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubAssignTask.class);

  private Future<String> futureAssignee;

  /**
   * @param gitHub      the GitHub instance
   * @param repoName    the full name of the repository
   * @param issueNumber the issue number of this assignment
   * @param assignee    the assignee for this issue (no @)
   */
  public GitHubAssignTask(GitHub gitHub, String repoName, int issueNumber,
      Future<String> assignee) {
    super(gitHub, repoName, issueNumber);
    this.futureAssignee = assignee;
  }


  @Nullable
  @Override
  public Void call() throws IOException, InterruptedException {
    try {
      gitHub.getRepository(repoName).getIssue(issueNumber).assignTo(
          gitHub.getUser(futureAssignee.get()));
    } catch (ExecutionException e) {
      LOGGER.error("Assignee task was aborted", e);
      // Wrap in IOException for lack of variadic generics
      throw new IOException(e);
    }
    return null;
  }

}
