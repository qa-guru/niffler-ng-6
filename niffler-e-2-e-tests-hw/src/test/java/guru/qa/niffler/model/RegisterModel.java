package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterModel(

        @JsonProperty("_csrf")
        String csrf,

        @JsonProperty("username")
        String username,

        @JsonProperty("password")
        String password,

        @JsonProperty("passwordSubmit")
        String passwordConfirmation

) {

    public RegisterModel csrf(String csrf) {
        return new RegisterModel(csrf, this.username, this.password, this.passwordConfirmation);
    }

}
