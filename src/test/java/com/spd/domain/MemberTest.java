package com.spd.domain;

import com.spd.trello.domain.Member;
import com.spd.trello.domain.Role;
import com.spd.trello.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest extends BaseTest {

    private static MemberService memberService;


    public MemberTest() {
        memberService = new MemberService();
    }

    @BeforeEach
    public void createTestMember() {
        member.setId(UUID.randomUUID());
        member = memberService.create(member);
    }

    @Test
    public void successCreate() {
        Member actual = memberService.create(member.getUser(), member.getRole(), member.getCreatedBy());
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(member.getRole(), actual.getRole()),
                () -> assertEquals(member.getCreatedBy(), actual.getCreatedBy())
        );
    }

    @Test
    public void successUpdate() {
        member.setUpdatedBy("test");
        member.setRole(Role.GUEST);
        Member actual = memberService.update(member.getId(), member);
        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(member.getRole(), actual.getRole()),
                () -> assertEquals(member.getUpdatedBy(), actual.getUpdatedBy())
        );
    }

    @Test
    public void successGetObjects() {
        List<Member> members = memberService.readAll();
        assertNotEquals(0, members.size());
    }

    @Test
    public void failDelete() {
        assertFalse(memberService.delete(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        assertTrue(memberService.delete(member.getId()));
    }
}
