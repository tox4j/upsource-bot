package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;

public class GitHubCommentTask implements RecoveringCallable<IOException> {

  private GitHub gitHub;
  private String repoName;
  private int issueNumber;
  private String comment;

  public GitHubCommentTask(GitHub gitHub, String repoName, int issueNumber, String comment) {
    this.gitHub = gitHub;
    this.repoName = repoName;
    this.issueNumber = issueNumber;
    this.comment = comment;
  }

  @Override
  public void call() throws IOException, InterruptedException {
    GHRepository repository;
    repository = gitHub.getRepository(repoName);
    GHIssue issue;
    issue = repository.getIssue(issueNumber);
    issue.comment(comment);
  }

}

