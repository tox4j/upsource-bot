package im.tox.upsourcebot.filters;

import com.google.common.io.ByteStreams;

import org.apache.commons.codec.digest.HmacUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

@GitHubHMAC
public class GitHubHMACFilter implements ContainerRequestFilter {

  private byte[] secret;

  public GitHubHMACFilter(String secret) {
    this.secret = secret.getBytes();
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String hubSignature = requestContext.getHeaderString("X-Hub-Signature");
    if (hubSignature == null) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
      return;
    }

    byte[] requestBody = ByteStreams.toByteArray(requestContext.getEntityStream());
    String hmac = "sha1=" + HmacUtils.hmacSha1Hex(secret, requestBody);
    if (!MessageDigest.isEqual(hmac.getBytes(), hubSignature.getBytes())) {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
    requestContext.setEntityStream(new ByteArrayInputStream(requestBody));
  }

}
