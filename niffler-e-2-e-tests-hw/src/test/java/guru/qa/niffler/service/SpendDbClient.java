package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDbClient {

    SpendJson create(SpendJson spendJson);

    Optional<SpendJson> findById(UUID id);

    List<SpendJson> findByUsernameAndDescription(String username, String description);

    List<SpendJson> findAllByUsername(String username);

    List<SpendJson> findAll();

    void delete(SpendJson spendJson);

}
