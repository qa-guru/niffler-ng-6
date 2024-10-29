package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {
  @Nonnull
  SpendJson createSpend(SpendJson spend);

  @Nonnull
  CategoryJson createCategory(CategoryJson category);

  @Nonnull
  CategoryJson updateCategory(CategoryJson category);

  void removeCategory(CategoryJson category);
}
