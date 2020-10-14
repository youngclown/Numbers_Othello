package com.chat.number.dto;

import com.chat.number.domain.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {
  private Long id;
  private String email;
  private String name;
  private String password;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;

  public MemberEntity toEntity(){
    return MemberEntity.builder()
            .id(id)
            .name(name)
            .email(email)
            .password(password)
            .build();
  }
}