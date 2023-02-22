package jpabook2.jpashop2;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
        // member 를 반환하지 않고 member Id를 반환하는이유
        // command 와 query 를 분리해라 원칙에 의해서.
        // 저장을 하고나면 가급적이면 side effect 를 일으키는 커맨드 성 이기 때문에
        // return 값을 거의 안만듦. 대신에 id 정도 있으면 다시 조회할 수 있으니까.
        // 주로 이렇게 설계.
    }
    
    public
}
