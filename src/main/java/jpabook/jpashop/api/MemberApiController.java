package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController // @Controller + @ResponseBody -> 데이터를 바로 보낸다
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 첫번째 버전의 회원 등록 API
    // 엔티티를 파라미터로 바로 받는 방식은 좋지 않다
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // @RequestBody를 사용하면 json 요청 내용을 모두 member로 변환
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 회원 등록 API V2
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        // 파라미터로 별도의 데이터객체를 사용한다

        Member member = new Member();
        member.setName(request.getName());

        // data는 별도의 데이터 객체를 이용해 받지만, member로 변환해 join을 처리한다
        // DB 저장 후 반환된 id를 데이터로 전달
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}
