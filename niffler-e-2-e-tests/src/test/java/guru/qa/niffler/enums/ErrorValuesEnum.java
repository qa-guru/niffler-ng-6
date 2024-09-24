package guru.qa.niffler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorValuesEnum {

  USER_ALREADY_EXIST("Username `%s` already exists"),
  PASSWORD_NOT_EQUALS("Passwords should be equal");

  final String description;
}