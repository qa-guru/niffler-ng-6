package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.AuthAuthorityJson;

import javax.annotation.Nonnull;

public class AuthAuthorityMapper {

    public AuthAuthorityEntity toEntity(@Nonnull AuthAuthorityJson authAuthorityJson) {
        return AuthAuthorityEntity.builder()
                .id(authAuthorityJson.getId())
                .user(AuthUserEntity.builder().id(authAuthorityJson.getUser()).build())
                .authority(authAuthorityJson.getAuthority())
                .build();
    }

    public AuthAuthorityJson toDto(@Nonnull AuthAuthorityEntity authAuthorityEntity) {
        return AuthAuthorityJson.builder()
                .id(authAuthorityEntity.getId())
                .user(authAuthorityEntity.getUser().getId())
                .authority(authAuthorityEntity.getAuthority())
                .build();
    }

}