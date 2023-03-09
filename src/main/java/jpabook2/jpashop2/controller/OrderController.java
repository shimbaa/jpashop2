package jpabook2.jpashop2.controller;

import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.domain.Order;
import jpabook2.jpashop2.domain.item.Item;
import jpabook2.jpashop2.repository.OrderSearch;
import jpabook2.jpashop2.service.ItemService;
import jpabook2.jpashop2.service.MemberService;
import jpabook2.jpashop2.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, // @RequestParam 은 orderForm 의 form-submit 에서 select 되는 것의 name 값이다.
                        @RequestParam("itemId") Long itemId,     // 그리고 변수에 바인딩 되는 것.
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        // 영속성 컨텍스트 유지를 위해(?) 커맨드성(?) 핵심비즈니스 로직은 controller 같이 밖에서 엔티티를 조회 하지 않게 하고, 식별자 id 값만 보내는 식으로 한다.
        // 상품주문 강의 후반부에 나오는 내용임.
        // 예제에서는 단순화 하기 위해서 하나의 상품만 주문 할 수 있도록 해놨지만 조금만 고치면 여러 개의 상품 주문 할 수 있게 바꿀 수 있다.
        return "redirect:/orders";

        //만약 주문된 결과 페이지로 가는 것 이라면 Long id = orderService.order(memberId, itemId, count);
        //해서 return "redirect:/orders" + id; 이런식으로 할 수 있다.
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        //이렇게 단순 위임이면 repository 바로 호출 해도됨.
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
