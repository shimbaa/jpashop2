package jpabook2.jpashop2.domain;

import jpabook2.jpashop2.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id") // 연관관계의 주인 ..(맞쥐?)
    private Order order;

    private int orderPrice; // 주문 당시의 가격
    private int count; // 주문 수량

//    protected OrderItem() {}
        // 생성자를 통해 new OrderItem 만들고, setXXX 하는 식으로 생성하는 것을 막기 위한 코드.
        // 생성 메서드를 통해서 생성하도록 강제하는 것.
    // 롬복의 @NoArgsConstructor(access = AccessLevel.PROTECTED) 이것을 붙이면 같은 코드임.
    // *** 항상 코드를 이런 식으로 사용법을 제약 하는 스타일로 해야 좋은 설계와 유지보수가 된다.


    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); // createOrder 가 받는 parameter 값인 orderItem 은 이미 여기서 재고가 까진 것으로 오는 것.
        return orderItem;
    }

    //==비즈니스 로직==//
    //이부분 잘 이해 안됨.
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조히
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
