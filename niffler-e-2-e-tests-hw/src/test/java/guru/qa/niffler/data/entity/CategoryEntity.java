package guru.qa.niffler.data.entity;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CategoryEntity implements Serializable {

    private UUID id;
    private String name;
    private String username;
    private boolean archived;

}
