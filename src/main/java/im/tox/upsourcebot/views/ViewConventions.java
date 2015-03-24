package im.tox.upsourcebot.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.glassfish.jersey.linking.InjectLink;

import java.net.URI;
import java.nio.charset.Charset;

import javax.annotation.Nullable;
import javax.ws.rs.Path;

import io.dropwizard.views.View;

public abstract class ViewConventions extends View {

  /*
   * Cheat here to avoid depending on app-specific resource classes. The Jersey link provider doesn't care that a
   * resource is actually exported, just that it has a URL path.
   */
  @Path("/")
  private static class RootUrl {}

  /**
   * Subclass to add additional links to your view, then attach your subclass to the ...XXX...
   */
  public static class Links {
    @InjectLink(resource = RootUrl.class)
    private URI base;

    /**
     * The base URI, from which all links in this representation can be resolved.
     */
    @JsonProperty
    public URI getBase() {
      return base;
    }
  }

  @JsonIgnore
  private Links links;

  /**
   * Use the default links.
   *
   * @see View#View(String)
   */
  protected ViewConventions(String templateName) {
    this(templateName, new Links());
  }

  /**
   * Use the specified links.
   *
   * @see View#View(String)
   */
  protected ViewConventions(String templateName, Links links) {
    super(templateName);
    this.links = links;
  }

  /**
   * Use the default links.
   *
   * @see View#View(String, Charset)
   */
  protected ViewConventions(String templateName, Charset charset) {
    this(templateName, charset, new Links());
  }

  /**
   * Use the specified links.
   *
   * @see View#View(String, Charset)
   */
  protected ViewConventions(String templateName, Charset charset, Links links) {
    super(templateName, charset);
    this.links = links;
  }

  /**
   * Link metadata for the view. This may be absent.
   */
  @Nullable
  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Links getLinks() {
    return links;
  }

  @JsonIgnore
  public void setLinks(Links links) {
    this.links = links;
  }}
