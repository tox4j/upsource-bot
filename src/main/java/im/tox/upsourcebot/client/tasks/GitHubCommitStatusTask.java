package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GHCommitState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

/**
 * Task to set a Commit Status on a commit in a GitHub repository
 */
public class GitHubCommitStatusTask extends GitHubTask {

  private String commitSHA;
  private GHCommitState state;
  private String url;
  private String context;
  private String description;

  /**
   * @param gitHub      the GitHub instance
   * @param repoName    full repository name that contains the commit
   * @param commitSHA   the SHA1 of the commit
   * @param state       the state to set for the commit
   * @param url         the URL for more information on this state
   * @param description the description text, a reason for this state
   * @param context     the optional context, useful when multiple states are assigned by different
   *                    applications
   */
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

  /**
   * @param gitHub      the GitHub instance
   * @param repoName    full repository name that contains the commit
   * @param commitSHA   the SHA1 of the commit
   * @param state       the state to set for the commit
   * @param url         the URL for more information on this state
   * @param description the description text, a reason for this state
   */
  public GitHubCommitStatusTask(GitHub gitHub, String repoName, String commitSHA,
      GHCommitState state, String url, String description) {
    this(gitHub, repoName, commitSHA, state, url, description, null);
  }

  @Override
  public Void call() throws IOException, InterruptedException {
    GHRepository repository = gitHub.getRepository(repoName);
    repository.createCommitStatus(commitSHA, state, url, description, context);
    return null;
  }

}
