package jpabook2.jpashop2.controller;

import jpabook2.jpashop2.domain.item.Book;
import jpabook2.jpashop2.domain.item.Item;
import jpabook2.jpashop2.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "/items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {

        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        //*** 이런 식으로 setXXX 로 다 박아 넣는 설계 말고 createBook() 하고 파라미터 넘기고 하는 것이 더 나은 설계임.
        //Order 에서 본 것 처럼 static 생성자 메서드를 가지고 의도에 맞게 생성하게 하는 것이 좋음.
        //여기서는 예제이고 편하게 할 수 있는 것들이 많아서 setter 열어 놓은 것.

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); // 예제 단순화 위해서 그냥 Book 으로 캐스팅 해서 씀.

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    // 실무에서 업데이트, 변경 할때는 이런 식으로 setXXX 하면 안된다.
    // 이렇게 하지말고 변경 되는 지점 확실히 알 수 있도록 addStock() 이나 change() 뭐 이런 것을 통해 변경하는 방식으로 해야한다.

    @PostMapping("items/{itemId}/edit") // itemId 임의로 바꿔서 조작할 수 도 있어서 실무에서는 해당 유저가 권한이 있는지 체크하는 것 필요.
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//        itemService.saveItem(book);
        //사실 위의 컨트롤러에서 엔티티를 생성하는 코드는 좋지 않은 코드임.
        //BookForm 을 웹계층에서만 쓰고자 만들었고, form 에서 book 으로 set 을 통해 담았지만,
        //더 나은 설계는 ItemService 에서 updateItem() 메서드를 통해 파라미터 값을 넘겨서 update 를 하는 것이다. (파라미터는 form.getName() 이런식)
        //엔티티는 컨트롤러에서 반환하면 안된다. 엔티티를 api 에 반환하면 안된다.
        //엔티티는 dto 로 변환을 해서 반환하는 것이 좋다.

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
