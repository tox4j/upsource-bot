package im.tox.upsourcebot.resources;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import im.tox.upsourcebot.core.User;
import im.tox.upsourcebot.jdbi.UserDao;
import im.tox.upsourcebot.views.users.AllUsersView;
import im.tox.upsourcebot.views.users.NewUserView;
import im.tox.upsourcebot.views.users.UserView;

@Path("/users")
public class UserResource {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

  private UserDao dao;

  public UserResource(UserDao dao) {
    this.dao = dao;
  }

  @GET
  @Path("/{id}")
  public UserView getUser(@PathParam("id") long id) {
    return new UserView(dao.findById(id));
  }

  @POST
  @Path("/{id}")
  public Response editUser(@FormParam("current") String currentPassword,
      @FormParam("password") String password,
      @FormParam("password-repeat") String passwordRepeat, @PathParam("id") long id) {
    if (currentPassword == null || password == null || passwordRepeat == null) {
      throw new BadRequestException();
    }

    User user = dao.findById(id);

    if (user == null) {
      return Response.status(Response.Status.NOT_FOUND).entity(new UserView(null)).build();
    }

    boolean error = false;
    boolean passwordIncorrect = false;
    if (!BCrypt.checkpw(currentPassword, user.getPasswordHash())) {
      error = true;
      passwordIncorrect = true;
    }
    boolean passwordEmpty = false;
    if (password.trim().isEmpty()) {
      error = true;
      passwordEmpty = true;
    }
    boolean passwordMismatch = false;
    if (!password.equals(passwordRepeat)) {
      error = true;
      passwordMismatch = true;
    }

    if (!error) {
      int rowsChanged = dao.updatePassword(BCrypt.hashpw(password, BCrypt.gensalt()), id);
      if (rowsChanged == 0) {
        return Response.status(Response.Status.NOT_FOUND).entity(new UserView(null)).build();
      }
    }

    return Response.status(error ? Response.Status.BAD_REQUEST : Response.Status.OK)
        .entity(new UserView(user, passwordMismatch, passwordEmpty, passwordIncorrect)).build();
  }

  @GET
  public AllUsersView getAll() {
    return new AllUsersView(dao.getAll());
  }

  @GET
  @Path("/new")
  public NewUserView newUser() {
    return new NewUserView();
  }

  @POST
  @Path("/new")
  public Response createUser(@FormParam("username") String name,
      @FormParam("password") String password, @FormParam("password-repeat") String passwordRepeat) {
    if (name == null || password == null || passwordRepeat == null) {
      throw new BadRequestException();
    }

    boolean error = false;
    boolean nameEmpty = false;
    if (name.trim().isEmpty()) {
      error = true;
      nameEmpty = true;
    }
    boolean nameTaken = false;
    if (!nameEmpty && dao.findByName(name) != null) {
      error = true;
      nameTaken = true;
    }
    boolean passwordEmpty = false;
    if (password.trim().isEmpty()) {
      error = true;
      passwordEmpty = true;
    }
    boolean passwordMismatch = false;
    if (!password.equals(passwordRepeat)) {
      error = true;
      passwordMismatch = true;
    }

    if (error) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(new NewUserView(nameEmpty, nameTaken, passwordEmpty, passwordMismatch)).build();
    }
    long id = dao.insert(name, BCrypt.hashpw(password, BCrypt.gensalt()));

    return Response.ok(getUser(id)).build();
  }

  @POST
  @Path("/delete/{id}")
  public AllUsersView deleteUser(@PathParam("id") long id) {
    dao.delete(id);
    return getAll();
  }
}
