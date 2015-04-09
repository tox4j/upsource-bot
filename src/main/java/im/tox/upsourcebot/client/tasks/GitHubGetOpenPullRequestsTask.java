package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Task for fetching open GitHub Pull Requests for a repository
 */
public class GitHubGetOpenPullRequestsTask extends GitHubTask<List<GHPullRequest>> {

  public GitHubGetOpenPullRequestsTask(GitHub gitHub, String repoName) {
    super(gitHub, repoName);
  }

  @Nullable
  @Override
  public List<GHPullRequest> call() throws IOException, InterruptedException {
    return gitHub.getRepository(repoName).getPullRequests(GHIssueState.OPEN);
  }

}
