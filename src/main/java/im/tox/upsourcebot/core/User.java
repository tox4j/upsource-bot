package im.tox.upsourcebot.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Represents a user of the web-interface
 */
public class User {

  private long id;

  private String name;

  @JsonIgnore
  private String password;

  @JsonIgnore
  private String passwordRepeat;

  @JsonIgnore
  private String passwordHash;

  public User() {
  }

  public User(long id, String name, String passwordHash) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
  }

  public User(long id, String name, String password, String passwordRepeat,
      String passwordHash) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.passwordRepeat = passwordRepeat;
    this.passwordHash = passwordHash;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonIgnore
  public String getPasswordHash() {
    return passwordHash;
  }

  @JsonIgnore
  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  @JsonIgnore
  public String getPasswordRepeat() {
    return passwordRepeat;
  }

  @JsonProperty
  public void setPasswordRepeat(String passwordRepeat) {
    this.passwordRepeat = passwordRepeat;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    if (id != user.id) {
      return false;
    }
    return !(name != null ? !name.equals(user.name) : user.name != null);

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  public static class UserMapper implements ResultSetMapper<User> {

    @Override
    public User map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      return new User(r.getLong("id"), r.getString("name"), r.getString("passwordHash"));
    }
  }

}
