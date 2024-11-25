package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SessionJson {

    @JsonProperty("username")
    private String username;

    @JsonProperty("issuedAt")
    private Date issuedAt;

    @JsonProperty("expiresAt")
    private Date expiresAt;

}
