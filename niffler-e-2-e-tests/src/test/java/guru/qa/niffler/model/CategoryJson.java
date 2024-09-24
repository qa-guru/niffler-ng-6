package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

public record CategoryJson(
        @JsonProperty("id")
        java.util.UUID id,
        @JsonProperty("name")
        String name,
        @JsonProperty("username")
        String username,
        @JsonProperty("archived")
        boolean archived) {
        public static CategoryJson fromEntity(CategoryEntity entity) {
                return new CategoryJson(
                        entity.getId(),
                        entity.getName(),
                        entity.getUsername(),
                        entity.isArchived()
                );
        }
}
