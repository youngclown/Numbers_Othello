package com.chat.number.service;

import com.chat.number.domain.entity.MemberEntity;
import com.chat.number.dto.MemberDto;
import com.chat.number.repository.MemberRepository;
import com.chat.number.type.RoleType;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Transactional
  public void joinUser(MemberDto memberDto) {
    // 비밀번호 암호화
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
    memberRepository.save(memberDto.toEntity());
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<MemberEntity> userEntityWrapper = memberRepository.findByEmail(username);
    MemberEntity userEntity = userEntityWrapper.get();

    List<GrantedAuthority> authorities = new ArrayList<>();

    if (("admin@example.com").equals(username)) {
      authorities.add(new SimpleGrantedAuthority(RoleType.ADMIN.getValue()));
    } else {
      authorities.add(new SimpleGrantedAuthority(RoleType.MEMBER.getValue()));
    }

    return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
  }





}