package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.AuthAuthorityJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AuthAuthorityMapper {

    public @Nonnull AuthAuthorityEntity toEntity(AuthAuthorityJson authAuthorityJson) {
        return AuthAuthorityEntity.builder()
                .id(authAuthorityJson.getId())
                .user(AuthUserEntity.builder().id(authAuthorityJson.getUser()).build())
                .authority(authAuthorityJson.getAuthority())
                .build();
    }

    public @Nonnull AuthAuthorityJson toDto(AuthAuthorityEntity authAuthorityEntity) {
        return AuthAuthorityJson.builder()
                .id(authAuthorityEntity.getId())
                .user(authAuthorityEntity.getUser().getId())
                .authority(authAuthorityEntity.getAuthority())
                .build();
    }

}