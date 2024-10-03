package guru.qa.niffler.model;

public record LoginModel(
        String csrf,
        String username,
        String password
) {

    public LoginModel csrf(String csrf) {
        return new LoginModel(csrf, username, password);
    }

}
