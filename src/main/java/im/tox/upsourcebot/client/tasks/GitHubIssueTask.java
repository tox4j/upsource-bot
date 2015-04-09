package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GitHub;

/**
 * Task class for anything that has to do with issues
 */
public abstract class GitHubIssueTask extends GitHubTask<Void> {

  protected int issueNumber;

  /**
   * @param gitHub      the GitHub instance
   * @param repoName    the full name of the repository
   * @param issueNumber issue this task operates on
   */
  public GitHubIssueTask(GitHub gitHub, String repoName, int issueNumber) {
    super(gitHub, repoName);
    this.issueNumber = issueNumber;
  }

}
