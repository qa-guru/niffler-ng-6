package guru.qa.niffler.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class CategoryEntity implements Serializable {

    private UUID id;
    private String name;
    private String username;
    private boolean archived;

}
