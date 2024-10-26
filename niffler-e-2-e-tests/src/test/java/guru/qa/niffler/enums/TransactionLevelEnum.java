package guru.qa.niffler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionLevelEnum {

  TRANSACTION_NONE(0),
  TRANSACTION_READ_UNCOMMITTED(1),
  TRANSACTION_READ_COMMITTED(2),
  TRANSACTION_REPEATABLE_READ(4),
  TRANSACTION_SERIALIZABLE(8);

  public final int level;
}
