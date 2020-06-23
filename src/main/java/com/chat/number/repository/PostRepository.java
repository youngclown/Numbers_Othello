package com.chat.number.repository;


import com.chat.number.domain.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA의 편한점은 Entity 객체에 ORM 대상이란 점을 어노테이션을 이용하여 명시만 해주면,
 * JPA에서 해당하는 Repository를 만들어줍니다.
 */
public interface PostRepository extends JpaRepository<PostEntity,Long> {
}
