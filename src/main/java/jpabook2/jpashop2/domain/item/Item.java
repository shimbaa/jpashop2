package jpabook2.jpashop2.domain.item;

import jpabook2.jpashop2.domain.Category;
import jpabook2.jpashop2.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 전략을 싱글테이블 로 한 것임.
@DiscriminatorColumn(name = "dtype") // db 가 저장이 될 때 book 인지 movie 인지 album 인지 구분 하는 기준.
@Getter
@Setter
public abstract class Item { // Item 클래스를 상속 시킬 거 여서 abstract 로 만든 것.

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//

    /**
     * stock 증가
     */
    // 보통 개발 할 때 ItemService 같은거에서 stockQuantity 가져오고 quantity 더하고 값만든 다음에
    // item.setStockQuantity() 해서 그 값 넣는 식으로 많이 했을 것.
    // 근데 객체 지향적으로 생각해보면 데이터를 가지고 있는 쪽에 비즈니스 메서드가 있는게 가장 좋음. 그래야 응집력 있음.
    // 그래서 밑에 코드 처럼 있는 것이 가장 관리하기가 좋음.
    // 결론은 밖에서 꺼내가서 계산하고, setter 쓰지말고
    // 이 안에서 계산하고, 이 안에서 필요한 validation 있으면 되는 것.
    // 그것이 가장 객체 지향적인 것.
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
