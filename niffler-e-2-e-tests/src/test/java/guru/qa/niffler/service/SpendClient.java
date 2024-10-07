package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;

public interface SpendClient {
  SpendJson createSpend(SpendJson spend);
}
