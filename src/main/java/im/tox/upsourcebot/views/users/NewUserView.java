package im.tox.upsourcebot.views.users;


import im.tox.upsourcebot.views.ViewConventions;

public class NewUserView extends ViewConventions {

  private boolean nameEmpty = false;

  private boolean nameTaken = false;

  private boolean passwordEmpty = false;

  private boolean passwordMismatch = false;

  public NewUserView() {
    super("new.ftl");
  }

  public NewUserView(boolean nameEmpty, boolean nameTaken, boolean emptyPassword,
      boolean passwordMismatch) {
    this();
    this.nameEmpty = nameEmpty;
    this.nameTaken = nameTaken;
    this.passwordEmpty = emptyPassword;
    this.passwordMismatch = passwordMismatch;
  }

  public boolean isNameEmpty() {
    return nameEmpty;
  }

  public boolean isNameTaken() {
    return nameTaken;
  }

  public boolean isPasswordEmpty() {
    return passwordEmpty;
  }

  public boolean isPasswordMismatch() {
    return passwordMismatch;
  }
}
