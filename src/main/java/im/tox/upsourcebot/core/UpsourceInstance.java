package im.tox.upsourcebot.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents an upsource instance we can connect to
 */
public class UpsourceInstance {

  private long id;

  private URL upsourceURL;

  private String name;

  public UpsourceInstance() {
    // Empty constructor for Jackson deserialization
  }

  public UpsourceInstance(URL upsourceURL, String name) {
    String protocol = upsourceURL.getProtocol();
    if (!(protocol.equals("http") || protocol.equals("https"))) {
      throw new IllegalArgumentException("Upsource URLs must use http or https");
    }
    this.upsourceURL = upsourceURL;
    this.name = name;
  }

  public UpsourceInstance(long id, URL upsourceURL, String name) {
    this(upsourceURL, name);
    this.id = id;
  }

  @JsonProperty
  public long getId() {
    return id;
  }

  @JsonProperty
  public URL getUpsourceURL() {
    return upsourceURL;
  }

  @JsonProperty
  public String getName() {
    return name;
  }

  public static class UpsourceInstanceMapper implements ResultSetMapper<UpsourceInstance> {

    @Override
    public UpsourceInstance map(int index, ResultSet r, StatementContext ctx) throws SQLException {
      try {
        return new UpsourceInstance(r.getLong("id"), new URL(r.getString("url")),
                                    r.getString("name"));
      } catch (MalformedURLException e) {
        throw new SQLException(e.getLocalizedMessage(), e);
      }
    }
  }

}
