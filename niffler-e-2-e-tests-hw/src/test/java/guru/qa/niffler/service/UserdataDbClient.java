package guru.qa.niffler.service;

import guru.qa.niffler.model.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataDbClient {

    UserModel create(UserModel userModel);

    Optional<UserModel> findById(UUID id);

    Optional<UserModel> findByUsername(String username);

    List<UserModel> findAll();

    void delete(UserModel userModel);

}
