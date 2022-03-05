package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller + @ResponseBody -> 데이터를 바로 보낸다
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 회원 조회 API - V1: 가장 단순한 형태
    // 엔티티를 직접 반환하는 것은 지양해야 한다
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // 회원 조회 API - V2: DTO를 사용하는 방식
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();

        // List<Member>를 List<MemberDTO>로 변환
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .collect(Collectors.toList());

        // json 데이터가 배열 처리되는 것을 방지하기 위해 Result에 넣어서 반환
        return new Result(collect);
    }

    @Data // Getter와 Setter를 포함한다
    @AllArgsConstructor // 필드 전체를 파라미터로 받는 생성자를 자동으로 만든다
    static class Result<T> {
        private T data;
    }

    // data 리턴 전용 DTO
    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
    }


    // 회원 등록 API - V1
    // 엔티티를 파라미터로 바로 받는 방식은 좋지 않다
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // @RequestBody를 사용하면 json 요청 내용을 모두 member로 변환
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 회원 등록 API - V2
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

    // 회원 수정 API
    @PutMapping("/api/v2/members/{id}") // id를 pathvariable로 사용
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id); // 새로 조회해서 객체 리턴
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


    @Data
    static class CreateMemberRequest {
        @NotEmpty
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
