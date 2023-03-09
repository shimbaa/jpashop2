package jpabook2.jpashop2.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // Enum 은 반드시 @Enumerated 붙여야함.
    // EnumType 기본값이 ORDINAL 인데 이거 절대 쓰면 안됨
    // ORDINAL 은 0,1,2 로 저장됨.
    private DeliveryStatus status; // READY, COMP
}
