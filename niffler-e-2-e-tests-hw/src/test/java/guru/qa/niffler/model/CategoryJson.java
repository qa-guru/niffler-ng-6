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

    public CategoryJson archived(boolean archived) {
        return new CategoryJson(this.id, this.name, this.username, archived);
    }

    public CategoryJson username(String username) {
        return new CategoryJson(this.id, this.name, username, this.archived);
    }

    public CategoryJson name(String name) {
        return new CategoryJson(this.id, name, this.username, this.archived);
    }

}
