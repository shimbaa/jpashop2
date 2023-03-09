package jpabook2.jpashop2.service;


import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 테스트 케이스에 붙어 있으면 메서드 돌리고 롤백함.
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("shim");

        //when
        Long savedId = memberService.join(member);

        //then
        Assertions.assertEquals(member, memberRepository.findOne(savedId));

        // 해결해야 할 의문점: @Rollback(false) 하면 h2 db 에 select * from member 해서 보이던 data 가 왜
        // @Rollback 안하고 테스트 돌리니까 사라졌는가?
        // 별개의 메서드 실행이고 별개의 데이터 아닌가?
        // 같은 것 이라고는 memberName 뿐 일텐데?
        // @Rollback(false) 로 돌린 데이터는 남아 있어야 하는 거 아니여? 원리가 궁금~
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("shim");

        Member member2 = new Member();
        member2.setName("shim");

        //when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 한다!!

        //then
        fail("예외가 발생해야 한다."); // assert 에서 제공하는 fail();
    }

}