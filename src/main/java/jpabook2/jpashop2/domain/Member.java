package jpabook2.jpashop2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Embedded // 내장 타입이라는 뜻.
    //Address 에 @Embeddable 만 붙이거나 둘 중 하나만 해도 되지만 보통 둘 다에 붙임
    private Address address;

    @OneToMany(mappedBy = "member")
    // mappedBy 는 연관 관계의 주인이 아닌, 매핑된 거울 이라는 뜻.
    // Order 테이블에 있는 member 필드에 의해 mapping 되었다 라는 뜻.
    // 즉, 읽기 전용.
    private List<Order> orders = new ArrayList<>();
}
