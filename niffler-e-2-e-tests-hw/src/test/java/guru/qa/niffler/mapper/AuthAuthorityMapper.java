package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.model.AuthAuthorityJson;

public class AuthAuthorityMapper {

    public AuthAuthorityEntity toEntity(AuthAuthorityJson authAuthorityJson) {
        return AuthAuthorityEntity.builder()
                .id(authAuthorityJson.getId())
                .user(AuthUserEntity.builder().id(authAuthorityJson.getUserId()).build())
                .authority(authAuthorityJson.getAuthority())
                .build();
    }

    public AuthAuthorityJson toDto(AuthAuthorityEntity authAuthorityEntity) {
        return AuthAuthorityJson.builder()
                .id(authAuthorityEntity.getId())
                .userId(authAuthorityEntity.getUser().getId())
                .authority(authAuthorityEntity.getAuthority())
                .build();
    }

}