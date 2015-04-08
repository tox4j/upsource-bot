package im.tox.upsourcebot;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import javax.validation.Valid;

public class Repository {

  @NotBlank
  @JsonProperty
  private String owner;

  @NotBlank
  @JsonProperty
  private String name;

  @NotEmpty
  @Valid
  @JsonProperty
  private List<Reviewer> reviewers;

  public String getOwner() {
    return owner;
  }

  public String getName() {
    return name;
  }

  public List<Reviewer> getReviewers() {
    return reviewers;
  }

}
