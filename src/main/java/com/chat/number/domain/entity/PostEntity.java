package com.chat.number.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 *  JPA 의 경우 자바 스펙의 상당부븐을 따르므로,
 *  POJO(Plain Old Java Object) 규약을 따라서 작성하게 됩니다.
 *  해당 방식을 벗어날 경우, ORM이 변환을 제대로 수행되지 못합니다.
 */
@Entity
public class PostEntity {

  @Id
  @GeneratedValue
  private Long Id;

  private String title;
  private String content;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostEntity post = (PostEntity) o;
    return Objects.equals(Id, post.Id) &&
            Objects.equals(title, post.title) &&
            Objects.equals(content, post.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Id, title, content);
  }
}
