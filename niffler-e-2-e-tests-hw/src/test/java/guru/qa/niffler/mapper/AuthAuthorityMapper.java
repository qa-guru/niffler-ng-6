package guru.qa.niffler.mapper;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;

public class AuthAuthorityMapper {

    public AuthAuthorityEntity toEntity(AuthAuthorityJson authAuthorityJson) {
        return AuthAuthorityEntity.builder()
                .id(authAuthorityJson.getId())
                .userId(authAuthorityJson.getUserId())
                .authority(authAuthorityJson.getAuthority())
                .build();
    }

    public AuthAuthorityJson toDto(AuthAuthorityEntity authAuthorityEntity) {
        return AuthAuthorityJson.builder()
                .id(authAuthorityEntity.getId())
                .userId(authAuthorityEntity.getUserId())
                .authority(authAuthorityEntity.getAuthority())
                .build();
    }

}