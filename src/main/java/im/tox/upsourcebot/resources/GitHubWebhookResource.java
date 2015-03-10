package im.tox.upsourcebot.resources;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import im.tox.upsourcebot.client.GitHubConnector;
import im.tox.upsourcebot.core.payloads.IssueWebhook;
import im.tox.upsourcebot.core.payloads.PullRequestWebhook;

@Path("/github-webhook/{upsource-name}")
@Consumes(MediaType.APPLICATION_JSON)
public class GitHubWebhookResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubWebhookResource.class);

  private GitHubConnector gitHubConnector;

  public GitHubWebhookResource(GitHubConnector gitHubConnector) {
    this.gitHubConnector = gitHubConnector;
  }

  @POST
  @Path("/issue")
  public Response receiveHook(IssueWebhook payload,
                              @PathParam("upsource-name") String upsourceName) {
    new Thread(() -> {
      if ("opened".equals(payload.getAction())) {
        String senderName = payload.getSender().getLogin();
        String message = "Hello, @" + senderName + ", nice to meet you! I am a robot.";
        gitHubConnector.commentOnIssue(payload.getRepository().getFullName(), message,
                                       payload.getIssue().getNumber());
      }
    }).start();
    return Response.accepted().build();
  }

  @POST
  @Path("/pull-request")
  public Response receiveHook(@Valid PullRequestWebhook payload,
                              @PathParam("upsource-name") String upsourceName) {
    switch (payload.getAction()) {
      case "opened":
        // Handle creation
        // Fall through to synchronized
      case "synchronized":
        // Set commit status
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
}
