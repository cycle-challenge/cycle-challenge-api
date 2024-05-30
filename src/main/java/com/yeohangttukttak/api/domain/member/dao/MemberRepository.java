package com.yeohangttukttak.api.domain.member.dao;

import com.yeohangttukttak.api.domain.member.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Optional<Member> findByEmail(String email) {
        return em.createQuery("SELECT m FROM Member as m WHERE m.email = :email", Member.class)
                .setParameter("email", email)
                .getResultList()
                .stream().findFirst();
    }

    public Optional<Member> findByNickname(String nickname) {
        return em.createQuery("SELECT m FROM Member as m WHERE m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList()
                .stream().findFirst();
    }

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Optional<Member> find(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.of(member);
    }

    public void delete(Member member) {
        em.remove(member);
    }

}
