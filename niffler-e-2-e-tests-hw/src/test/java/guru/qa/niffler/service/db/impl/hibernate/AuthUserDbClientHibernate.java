package guru.qa.niffler.service.db.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.service.db.AuthUserDbClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class AuthUserDbClientHibernate implements AuthUserDbClient {

    private static final AuthUserMapper authUserMapper = new AuthUserMapper();
    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final XaTransactionTemplate xaTxTemplate = new XaTransactionTemplate(AUTH_JDBC_URL);
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();

    @Override
    public AuthUserJson create(@NonNull AuthUserJson authUserJson) {
        log.info("Creating new auth user by DTO: {}", authUserJson);
        return xaTxTemplate.execute(() ->
                authUserMapper.toDto(
                        authUserRepository.create(
                                authUserMapper.toEntity(authUserJson))));
    }

    public AuthUserJson createWithAuthorities(@NonNull AuthUserJson authUserJson) {
        log.info("Creating new auth user with authorities by DTO: {}", authUserJson);
        var authUserEntity = authUserMapper.toEntity(authUserJson);
        authUserEntity.setAuthorities(
                List.of(AuthAuthorityEntity.builder().authority(Authority.read).user(authUserEntity).build(),
                        AuthAuthorityEntity.builder().authority(Authority.write).user(authUserEntity).build())
        );
        return xaTxTemplate.execute(() ->
                authUserMapper.toDto(
                        authUserRepository.create(authUserMapper.toEntity(authUserJson))));
    }

    @Override
    public Optional<AuthUserJson> findById(@NonNull UUID id) {
        log.info("Get user by id = [{}]", id);
        return xaTxTemplate.execute(() ->
                authUserRepository
                        .findById(id)
                        .map(authUserMapper::toDto));
    }

    @Override
    public Optional<AuthUserJson> findByUsername(@NonNull String username) {
        log.info("Get user by username = [{}]", username);
        return xaTxTemplate.execute(() ->
                authUserRepository
                        .findByUsername(username)
                        .map(authUserMapper::toDto));
    }

    @Override
    public List<AuthUserJson> findAll() {
        log.info("Get all auth users");
        return xaTxTemplate.execute(() ->
                authUserRepository
                        .findAll().stream()
                        .map(authUserMapper::toDto)
                        .toList());
    }

    @Override
    public void remove(@NonNull AuthUserJson authUser) {
        log.info("Remove user: {}", authUser);
        xaTxTemplate.execute(() -> {
            authUserRepository
                    .remove(authUserMapper.toEntity(authUser));
            return null;
        });
    }

}
