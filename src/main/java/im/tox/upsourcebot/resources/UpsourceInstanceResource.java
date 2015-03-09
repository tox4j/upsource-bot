package im.tox.upsourcebot.resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import im.tox.upsourcebot.core.UpsourceInstance;
import im.tox.upsourcebot.jdbi.UpsourceInstanceDAO;

@Path("/upsources")
@Produces(MediaType.APPLICATION_JSON)
public class UpsourceInstanceResource {

  private final UpsourceInstanceDAO upsourceInstanceDAO;

  public UpsourceInstanceResource(UpsourceInstanceDAO upsourceInstanceDAO) {
    this.upsourceInstanceDAO = upsourceInstanceDAO;
  }

  @GET
  @Path("/dummy")
  public UpsourceInstance getDummy() {
    try {
      return new UpsourceInstance(1L, new URL("http://upsource.slevermann.de:8081/"),
                                  "Default instance");
    } catch (MalformedURLException e) {
      throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  @POST
  @Path("/insert")
  public Response insert() {
    try {
      final UpsourceInstance instance =
          new UpsourceInstance(new URL("http://upsource.slevermann.de:8081/"), "Default instance");
      upsourceInstanceDAO.insert(instance.getName(), instance.getUpsourceURL().toString());
    } catch (MalformedURLException e) {
      return Response.serverError().build();
    }
    return Response.ok().build();
  }

  @GET
  @Path("/{id}")
  public Optional<UpsourceInstance> get(@PathParam("id") long id) {
    return upsourceInstanceDAO.findById(id);
  }

  @GET
  @Path("/all")
  public List<UpsourceInstance> all() {
    return upsourceInstanceDAO.getAll();
  }

}
