package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GitHub;

import java.io.IOException;

public abstract class GitHubTask implements RecoveringCallable<IOException> {

  protected GitHub gitHub;
  protected String repoName;

  public GitHubTask(GitHub gitHub, String repoName) {
    this.gitHub = gitHub;
    this.repoName = repoName;
  }
}
