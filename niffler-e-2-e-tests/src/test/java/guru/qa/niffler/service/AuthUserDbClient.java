package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.AuthorityJson;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class AuthUserDbClient {

    private static final Config CFG = Config.getInstance();
    private final PasswordEncoder passwordEncoder;

    public AuthUserDbClient(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public AuthUserJson createAuth(AuthUserJson authUserJson) {
        return Databases.xaTransaction(Connection.TRANSACTION_SERIALIZABLE, new Databases.XaFunction<AuthUserJson>(connection -> {
            AuthUserEntity authUserEntity = AuthUserEntity.fromJson(authUserJson);

            // Кодируем пароль перед сохранением
            String encodedPassword = passwordEncoder.encode(authUserEntity.getPassword());
            authUserEntity.setPassword(encodedPassword);

            AuthUserDaoJdbc authUserDao = new AuthUserDaoJdbc(connection);
            AuthUserEntity createdUser = authUserDao.create(authUserEntity);

            if (authUserJson.authorities() != null) {
                AuthorityDaoJdbc authorityDao = new AuthorityDaoJdbc(connection);
                List<AuthorityEntity> authorities = authUserJson.authorities().stream()
                        .map(AuthorityJson::toEntity)
                        .collect(Collectors.toList());

                authorityDao.createAuthorities(authorities, createdUser.getId());
            }

            return AuthUserJson.fromEntity(createdUser);
        }, CFG.authJdbcUrl()));
    }
}
