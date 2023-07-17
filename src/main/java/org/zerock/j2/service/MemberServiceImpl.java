package org.zerock.j2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.zerock.j2.dto.MemberDTO;
import org.zerock.j2.entity.Member;
import org.zerock.j2.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
    
    private final MemberRepository memberRepository;
    
    // 런타임익셉션 -> 언체크드익셉션
    public static final class MemberLoginException extends RuntimeException {
        
        public MemberLoginException(String msg){
            super(msg);
        }
    }
    
    @Override
    public MemberDTO login(String email, String pw) {

        MemberDTO memberDTO = null;

        try{
            Optional<Member> result = memberRepository.findById(email);

            Member member = result.orElseThrow();

            if( !member.getPw().equals(pw)){
                throw new MemberLoginException("Password Incorrect");
            }

            memberDTO = MemberDTO.builder()
                    .email(member.getEmail())
                    .pw("") // 받을 땐 받고, 뱉을 땐 빈 문자열로.
                    .nickname(member.getNickname())
                    .admin(member.isAdmin())
                    .build();

        }catch (Exception e){
            throw new MemberLoginException(e.getMessage());

        }

        return memberDTO;
    }
}
