package im.tox.upsourcebot.views.users;

import im.tox.upsourcebot.core.User;
import im.tox.upsourcebot.views.ViewConventions;

public class UserView extends ViewConventions {

  private User user;

  private boolean passwordMismatch;

  private boolean passwordEmpty;

  private boolean passwordIncorrect;

  public UserView(User u) {
    super("user.ftl");
    this.user = u;
  }

  public UserView(User user, boolean passwordMismatch, boolean passwordEmpty,
      boolean passwordIncorrect) {
    this(user);
    this.passwordMismatch = passwordMismatch;
    this.passwordEmpty = passwordEmpty;
    this.passwordIncorrect = passwordIncorrect;
  }

  public User getUser() {
    return user;
  }

  public boolean isPasswordMismatch() {
    return passwordMismatch;
  }

  public boolean isPasswordEmpty() {
    return passwordEmpty;
  }

  public boolean isPasswordIncorrect() {
    return passwordIncorrect;
  }

}
