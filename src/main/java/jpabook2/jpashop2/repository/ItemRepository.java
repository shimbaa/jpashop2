package jpabook2.jpashop2.repository;

import jpabook2.jpashop2.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { // id 값이 null 이라는 것은 완전히 새로 생성한 객체라는 뜻. 새로운 item 이기 때문에 저장! 즉 신규 등록.
            em.persist(item);
        } else {
            em.merge(item); // 이 경우는 이미 등록 되어있는 item 이고, merge 는 update 라고 이해하면 됨.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
