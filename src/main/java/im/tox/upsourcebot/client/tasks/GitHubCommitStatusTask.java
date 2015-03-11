package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GHCommitState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class GitHubCommitStatusTask extends GitHubTask {

  private String commitSHA;
  private GHCommitState state;
  private String url;
  private String context;
  private String description;

  public GitHubCommitStatusTask(GitHub gitHub, String repoName, String commitSHA,
      GHCommitState state, String url, String description,
      String context) {
    super(gitHub, repoName);
    this.commitSHA = commitSHA;
    this.state = state;
    this.url = url;
    this.description = description;
    this.context = context;
  }

  @Override
  public void call() throws IOException, InterruptedException {
    GHRepository repository = gitHub.getRepository(repoName);
    repository.createCommitStatus(commitSHA, state, url, description, context);
  }

}
