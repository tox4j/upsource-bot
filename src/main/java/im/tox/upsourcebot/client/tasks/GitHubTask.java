package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * Common parent for tasks that need to interact with a repository on GitHub
 */
public abstract class GitHubTask implements RecoveringCallable<IOException> {

  protected GitHub gitHub;
  protected String repoName;

  /**
   * @param gitHub   the GitHub instance
   * @param repoName the full name of the repository
   */
  public GitHubTask(GitHub gitHub, String repoName) {
    this.gitHub = gitHub;
    this.repoName = repoName;
  }

}
