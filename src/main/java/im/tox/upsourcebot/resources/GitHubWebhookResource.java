package im.tox.upsourcebot.resources;


import com.google.common.collect.ImmutableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import im.tox.upsourcebot.client.GitHubConnector;
import im.tox.upsourcebot.core.payloads.IssueCommentWebhook;
import im.tox.upsourcebot.core.payloads.IssueWebhook;
import im.tox.upsourcebot.core.payloads.PullRequestWebhook;
import im.tox.upsourcebot.filters.GitHubHMAC;

@Path("/github-webhook/")
@Consumes(MediaType.APPLICATION_JSON)
@GitHubHMAC
public class GitHubWebhookResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubWebhookResource.class);

  private GitHubConnector gitHubConnector;
  private ImmutableList<String> repoNames;

  public GitHubWebhookResource(GitHubConnector gitHubConnector, ImmutableList<String> repoNames) {
    this.gitHubConnector = gitHubConnector;
    this.repoNames = repoNames;
  }

  @POST
  @Path("/issue")
  public Response receiveHook(IssueWebhook payload) {
    switch (payload.getAction()) {
      case "opened":
      case "assigned":
      case "unassigned":
      case "labeled":
      case "unlabeled":
      case "closed":
      case "reopened":
        break;
      default:
        LOGGER.error("GitHub Issue payload format changed");
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    return Response.accepted().build();
  }

  @POST
  @Path("/pull-request")
  public Response receiveHook(PullRequestWebhook payload) {
    String repoName = payload.getRepository().getFullName();
    if (!repoNames.contains(repoName)) {
      LOGGER.error("Repository {} is not configured.", repoName);
      return Response.status(Response.Status.BAD_REQUEST).build();
    }
    switch (payload.getAction()) {
      case "opened":
        gitHubConnector
            .assignAndGreet(repoName, payload.getSender().getLogin(), payload.getNumber());
        // Handle creation
        // Fall through to synchronize
      case "synchronize":
        break;
      case "assigned":
      case "unassigned":
      case "labeled":
      case "unlabeled":
      case "closed":
      case "reopened":
        break;
      default:
        LOGGER.error("GitHub Pull request payload format changed");
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    return Response.accepted().build();
  }

  @POST
  @Path("/issue-comment")
  public Response receiveHook(IssueCommentWebhook payload) {
    String repoName = payload.getRepository().getFullName();
    if (!repoNames.contains(repoName)) {
      LOGGER.error("Repository {} is not configured.", repoName);
      return Response.status(Response.Status.BAD_REQUEST).build();
    }

    if (payload.getAction().equals("created")) {

    } else {
      LOGGER.error("GitHub issue comment payload format changed");
      return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    return Response.accepted().build();
  }

}
