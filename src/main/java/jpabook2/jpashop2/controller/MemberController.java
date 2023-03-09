package jpabook2.jpashop2.controller;

import jpabook2.jpashop2.domain.Address;
import jpabook2.jpashop2.domain.Member;
import jpabook2.jpashop2.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        // Member 를 안받고 MemberForm 을 만들어서 받는 이유는 필드값들의 차이도 있고 컨트롤러에서 필요한 validation 과 서비스로직에서 필요한 validation 이 다르기 때문
        // 그리고 엔티티에 화면 종속적인 기능을 넣지 않기 위해 이렇게 하는게 좋다. 유지보수하기 어려워진다.
        // 특히 jpa 쓸 때 엔티티를 최대한 순수하게 유지해야 한다.
        // 그래서 controller 에서 이렇게 필요한 데이터만 정제를 해서 밑에 member 에 setXXX 한 것 처럼 사용하는 것이 좋음.

        if (result.hasErrors()) { // 원래 에러 있으면 이 메서드에서(?) 튕겨버리는데 BindingResult 써서 해당 에러 값 받아와서 이렇게 처리가능.
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        // 여기서는 어차피 member 넘겨도 필요한 데이터만 찍어서 화면 뽑는 거니까 (어차피 서버사이드에서 화면 뽑는 거니까) 괜찮지만
        // api 를 만들 때는 이유를 불문하고 절대 엔티티를 넘기면 안된다.
        // 사실 제일 바람직한 방법은 화면 할때도 dto 만들어서 필요한 데이터만 넘기는 것이 제일 깔끔하다.
        return "members/memberList";
    }
}