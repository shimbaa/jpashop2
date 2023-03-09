package jpabook2.jpashop2.repository;

import jpabook2.jpashop2.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor // 서비스 개발 강의 뒷부분의 injection 설명에서 코드 변경홤.
public class MemberRepository {

//    @PersistenceContext
    // EntityManager 는 @Autowired 로 injection 안되고 @PersistenceContext 가 표준이어서
    // 이것으로 해야 하는데 springBoot 가 @Autowired 도 되게끔 해주는 것임.
    // 그래서 final 붙은 필드값에 @Autowired 붙여주는 롬복의 @RequiredArgsConstructor
    // 쓸 수 있는 것.
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
