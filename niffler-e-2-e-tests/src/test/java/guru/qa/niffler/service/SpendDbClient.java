package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.SpendJson;


public class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  private final SpendRepository spendRepository = new SpendRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
      CFG.spendJdbcUrl()
  );

  @Override
  public SpendJson createSpend(SpendJson spend) {
    return xaTransactionTemplate.execute(() -> {
          spendRepository.create(SpendEntity.fromJson(spend));
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          return SpendJson.fromEntity(spendEntity);
        }
    );
  }
}
