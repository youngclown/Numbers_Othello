package com.chat.number.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleType {
  ADMIN("ROLE_ADMIN"),
  MEMBER("ROLE_MEMBER");

  private String value;
}