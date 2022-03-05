package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final이 붙은 필드만 생성자를 만듬
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


    // 단건 조회
    @Transactional
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional // @Transactional이 있는 상태에서 조회하면 영속성 컨텍스트에서 가져온다
    public void update(Long id, String name) {
        // 영속성 컨텍스트에서 member를 가져오면, 변경 감지에 의해 update 완료
        Member member = memberRepository.findOne(id);
        member.setName(name);
        // 트랜잭션 종료 후 커밋
    }
}
