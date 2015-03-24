package im.tox.upsourcebot.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.util.LinkedHashMap;

import io.dropwizard.jackson.Jackson;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests the correct serialization and deserialization of Users
 */
public class UserTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void testSserializeUser() throws Exception {
    User user = new User(1L, "Foo Bar", "foo", "foo", "hash");
    String expected = MAPPER.writeValueAsString(MAPPER.readValue(
        fixture("fixtures/user-nopassword.json"),
        new TypeReference<LinkedHashMap<String, Object>>() {
        }));
    assertEquals(expected, MAPPER.writeValueAsString(user));
  }

  @Test
  public void testDeserializeUser() throws Exception {
    User expected = new User(1L, "Foo Bar", "foo", "foo", null);
    User actual = MAPPER.readValue(fixture("fixtures/user-password.json"), User.class);
    assertEquals(expected, actual);
    assertNull(actual.getPasswordHash());
  }

}
