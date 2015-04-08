package im.tox.upsourcebot;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotBlank;

public class Reviewer {

  @NotBlank
  @JsonProperty
  private String name;

  public String getName() {
    return name;
  }

}
