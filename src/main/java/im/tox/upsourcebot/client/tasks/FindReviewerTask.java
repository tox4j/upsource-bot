package im.tox.upsourcebot.client.tasks;

import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import im.tox.upsourcebot.Reviewer;

/**
 * Task to find a reviewer
 */
public class FindReviewerTask extends GitHubTask<String> {

  private List<Reviewer> candidates;
  private String author;

  public FindReviewerTask(GitHub gitHub, String repoName, List<Reviewer> candidates,
      String author) {
    super(gitHub, repoName);
    this.candidates = candidates;
    this.author = author;
  }

  @Nullable
  @Override
  public String call() throws IOException, InterruptedException {
    Reviewer reviewer;
    if (candidates.size() == 1) {
      reviewer = candidates.get(0);
    } else {
      candidates = candidates.stream().filter(r -> !r.getName().equals(author)).collect(
          Collectors.toList());
      reviewer = candidates.get(new Random().nextInt(candidates.size()));
    }
    return reviewer.getName();
  }
}
