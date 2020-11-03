package com.chat.number.service;

import com.chat.number.domain.entity.MemberEntity;
import com.chat.number.dto.MemberDto;
import com.chat.number.repository.MemberRepository;
import com.chat.number.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Service
@Log4j2
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
    MemberEntity userEntity = memberRepository.findByEmail(username).orElse(null);

    if (userEntity == null) {
      log.info("userEntity is null {}", username);
      return null;
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(RoleType.MEMBER.getValue()));

    return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
  }





}