package im.tox.upsourcebot.views.users;

import java.util.List;

import im.tox.upsourcebot.core.User;
import im.tox.upsourcebot.views.ViewConventions;

public class AllUsersView extends ViewConventions {

  private List<User> users;

  public AllUsersView(List<User> users) {
    super("all.ftl");
    this.users = users;
  }

  public List<User> getUsers() {
    return users;
  }

}
