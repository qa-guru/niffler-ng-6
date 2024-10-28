package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataClient {

    UserJson create(UserJson userJson);

    Optional<UserJson> findById(UUID id);

    Optional<UserJson> findByUsername(String username);

    List<UserJson> findAll();

    void sendInvitation(UserJson requester, UserJson addressee);

    void addFriend(UserJson requester, UserJson addressee);

    void remove(UserJson userJson);

}
