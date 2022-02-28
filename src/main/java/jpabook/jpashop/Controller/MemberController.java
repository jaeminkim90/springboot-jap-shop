package jpabook.jpashop.Controller;

import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new" )
    public String creatForm(Model model) {

        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";

    }
}
