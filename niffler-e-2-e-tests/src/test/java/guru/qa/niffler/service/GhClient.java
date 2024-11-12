package guru.qa.niffler.service;

import javax.annotation.Nonnull;

public interface GhClient {
  @Nonnull
  String issueState(String issueNumber);
}
