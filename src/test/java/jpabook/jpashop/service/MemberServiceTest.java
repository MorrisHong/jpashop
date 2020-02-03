package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("hong");

        //when
        Long newMemberId = memberService.join(member);

        //then
        assertTrue(newMemberId > 0);
        assertEquals(member, memberRepository.findOne(newMemberId));
     }

     @Test
     void 중복_회원_예외() throws Exception {
         //given
         Member member1 = new Member();
         member1.setName("hong");

         Member member2 = new Member();
         member2.setName("hong");

         //when
         memberService.join(member1);

         //then
         assertThrows(IllegalStateException.class, () -> memberService.join(member2));
      }

}