package jpabook2.jpashop2.service;

import jpabook2.jpashop2.domain.Delivery;
import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.domain.OrderItem;
import jpabook2.jpashop2.domain.item.Item;
import jpabook2.jpashop2.repository.ItemRepository;
import jpabook2.jpashop2.repository.MemberRepository;
import jpabook2.jpashop2.repository.OrderRepository;
import jpabook2.jpashop2.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        // 실제로는 배송정보 입력 받거나 기존정보 쓰거나 그런 식으로 해야되지만
        // 예제를 단순화 하기 위해 이렇게 함.

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);// static 메서드임! 생성메서드
        // 예제를 단순화 하기 위해서 상품은 하나만 주문 할 수 있게 해놓은 것.

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        // Order 클래스 안에 cascade 옵션 걸려있는 필드 값들은 전부다 order 가 persist 될때 같이 persist 됨.
        // orderItem 과 delivery. (cascade 없으면 모두 각각 persist 해줘야 한다.)
        // 위의 두 놈은 다른 곳에서는 안쓰고 order 만 얘네를 참조해서 쓴다. 딱 이런 범위에서 cascade 를 걸어야 한다.
        // 예를 들어 다른 애들도 delivery 참조하고 이런 상황이면 cascade 쓰면 안됨. private owner 일때만 써야함.
        // 다른 애들도 쓰는 경우는 별도의 repository 두고 각각 persist 하는 것이 맞다.
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel(); // jpa 가 dirty checking (변경 내역 감지) 해서 update 쿼리를 다 날려줌. mybatis 나 jdbcTemplate 같은 경우는 update 쿼리를 직접 다 날려야함.
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
