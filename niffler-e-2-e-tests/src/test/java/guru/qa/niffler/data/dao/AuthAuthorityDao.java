package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

public interface AuthAuthorityDao {

    void createAuthorities(AuthorityEntity... authority);
}
