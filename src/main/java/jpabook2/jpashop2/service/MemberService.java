package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 해당 강의 내용 (회원 서비스 개발) 10분정도? 부터 매우 중요. 복습 필요.

@Service
@Transactional(readOnly = true) // 이렇게 하면 전체에 readOnly 적용됨
@RequiredArgsConstructor // final 붙은 필드의 값만 생성자 만들어 줌.
public class MemberService {

//    @Autowired
    private final MemberRepository memberRepository;

//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired // 생성자가 하나만 있는 경우에는 스프링이 @Autowired 해줌
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원가입
     */
    // 읽기 아닌 쓰기에는 readOnly = true 넣으면 안된다~ 데이터 변경이 안된다.
    // 따로 readOnly = true 아닌것으로 해놓으면 이게 우선권 가져서 적용된다.
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //좀 더 최적화 하려면 사실
        // member 수가 0 보다 크면 예외터지는 거로 하면 된다.
        // ** 실무에서는 multiThread 환경같은거에서 동시에 memberA로 가입할 수 있기
        // ** 때문에 memberName 을 unique 제약조건 등으로 잡는 등 최후의 조치도 필요함.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    // readOnly = true 로 하면 jpa 가 조회 하는 로직에서는 최적화를 한다.
    // 그래서 읽기에는 가급적이면 readOnly = true 해줘라.
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
