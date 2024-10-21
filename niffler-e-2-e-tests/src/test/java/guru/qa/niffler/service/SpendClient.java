package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

public interface SpendClient {
  SpendJson createSpend(SpendJson spend);

  CategoryJson createCategory(CategoryJson category);

  void removeCategory(CategoryJson category);
}
