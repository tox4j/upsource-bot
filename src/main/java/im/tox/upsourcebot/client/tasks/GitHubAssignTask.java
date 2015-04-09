package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GitHub;

import java.io.IOException;

import javax.annotation.Nullable;

/**
 *
 */
public class GitHubAssignTask extends GitHubIssueTask {

  private String assignee;

  /**
   * @param gitHub      the GitHub instance
   * @param repoName    the full name of the repository
   * @param issueNumber the issue number of this assignment
   * @param assignee    the assignee for this issue (no @)
   */
  public GitHubAssignTask(GitHub gitHub, String repoName, int issueNumber, String assignee) {
    super(gitHub, repoName, issueNumber);
    this.assignee = assignee;
  }

  @Nullable
  @Override
  public Void call() throws IOException, InterruptedException {
    gitHub.getRepository(repoName).getIssue(issueNumber).assignTo(gitHub.getUser(assignee));
    return null;
  }

}
