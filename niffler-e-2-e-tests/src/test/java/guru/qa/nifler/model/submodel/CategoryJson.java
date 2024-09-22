package guru.qa.nifler.model.submodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record CategoryJson(
    @JsonProperty("id")
    UUID id,
    @JsonProperty("name")
    String name,
    @JsonProperty("username")
    String username,
    @JsonProperty("archived")
    boolean archived) {
}