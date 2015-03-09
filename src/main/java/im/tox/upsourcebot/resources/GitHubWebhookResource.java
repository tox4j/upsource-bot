package im.tox.upsourcebot.resources;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import im.tox.upsourcebot.client.GitHubConnector;
import im.tox.upsourcebot.core.payloads.IssueWebhook;

@Path("/github-webhook")
@Consumes(MediaType.APPLICATION_JSON)
public class GitHubWebhookResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(GitHubWebhookResource.class);

  private GitHubConnector gitHubConnector;

  public GitHubWebhookResource(GitHubConnector gitHubConnector) {
    this.gitHubConnector = gitHubConnector;
  }

  @POST
  public Response receiveHook(IssueWebhook payload) {
    String senderName = payload.getSender().getLogin();
    String message = "Hello, @" + senderName + ", nice to meet you! I am a robot.";
    gitHubConnector.commentOnIssue(payload.getRepository().getFullName(), message,
                                   payload.getIssue().getNumber());
    return Response.ok().build();
  }

}
