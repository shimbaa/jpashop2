package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.item.Item;
import jpabook2.jpashop2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    // 보면 여기 코드들은 단순히 itemRepository 에 위임만 하고 끝나는 코드들임.
    // 그래서 경우에 따라서는 이렇게 코드 짜지 말고
    // controller 에서 그냥 바로 repository 로 접근 해서 써도 문제가 없다고 말함.
    private final ItemRepository itemRepository;

    /**
     * 영속성 컨텍스트가 자동 변경
     */
    @Transactional
    public void updateItem(Long id, String name, int price, int stockQuantity) {
        // 이런 코드에서 파라미터 너무 많아지면 dto 만들어서 dto 객체를 넘기는 식으로 설계.
        // updateItem(Long id, UpdateItemDto itemDto) 이런식으로. dto 에 말 그대로 값들 담아서 넘기는 것. 물론 dto 안에 id 가 들어가도 된다.
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        // ** setter 는 쓰지 말자!! 엔티티 안에서 추적할 수 있는 변경 메서드를 생성하는 것이 훨씬 좋다!!
    }

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
