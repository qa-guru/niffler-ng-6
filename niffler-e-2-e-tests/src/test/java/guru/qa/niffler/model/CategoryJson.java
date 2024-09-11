package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryJson(
    @JsonProperty("id")
    String id,
    @JsonProperty("name")
    String name,
    @JsonProperty("username")
    String username,
    @JsonProperty("archived")
    boolean archived) {

}
