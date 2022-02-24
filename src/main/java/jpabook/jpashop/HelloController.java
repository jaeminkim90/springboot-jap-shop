package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) { // 저장용 객체
        model.addAttribute("data" , "hello!");
        return "hello"; // 스프링 부트가 화면 이름을 찾아준다

    }
}
