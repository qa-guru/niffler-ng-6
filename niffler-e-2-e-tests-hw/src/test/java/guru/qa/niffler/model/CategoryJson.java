package guru.qa.niffler.model;

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
