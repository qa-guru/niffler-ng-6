package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;

public interface AuthAuthorityDao {

  void create(AuthorityEntity... authority);

  List<AuthorityEntity> findAll();

}
