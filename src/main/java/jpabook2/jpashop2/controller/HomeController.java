package jpabook2.jpashop2.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home() {
        log.info("home controller"); // logger 찍는 이유는 에러 났을 때 해당 "home controller" 문구가 로그로 남아 있으면 html 로 가서 문제가 있었구나 하고 알 수 있기 때문.
         return "home";
    }
}
