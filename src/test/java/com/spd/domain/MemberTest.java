package com.spd.domain;

import com.spd.trello.domain.enums.Role;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.exception.EntityNotFoundException;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberTest extends BaseTest {

    @Mock
    private MemberRepository repository;
    @InjectMocks
    private MemberService service;

    @BeforeEach
    public void createTestMember() {
        member.setId(UUID.fromString("7ee897d3-9065-421d-93bd-7ad5f30c3bd9"));
    }

    @Test
    public void successCreate() {
        when(repository.save(member)).thenReturn(member);
        Member actual = service.create(BaseTest.member);
        assertAll(
                () -> assertNotNull(actual.getCreatedDate()),
                () -> assertNull(actual.getUpdatedDate()),
                () -> assertEquals(BaseTest.member.getRole(), actual.getRole()),
                () -> assertEquals(BaseTest.member.getCreatedBy(), actual.getCreatedBy())
        );

        verify(repository, times(1)).save(member);
    }

    @Test
    public void successUpdate() {
        when(repository.findById(any())).thenReturn(Optional.ofNullable(member));
        when(repository.save(member)).thenReturn(member);

        member.setRole(Role.GUEST);
        Member actual = service.update(member);

        assertAll(
                () -> assertNotNull(actual.getUpdatedDate()),
                () -> assertEquals(member.getRole(), actual.getRole()),
                () -> assertEquals(member.getUpdatedBy(), actual.getUpdatedBy())
        );

        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).save(member);
    }

    @Test
    public void successGetObjects() {
        Page<Member> expected = new PageImpl(List.of(member, member), pageable, 2);

        when(repository.save(member)).thenReturn(member);
        when(repository.findAll(pageable)).thenReturn(expected);

        List<Member> members = service.readAll(pageable).getContent();

        assertEquals(2, members.size());
    }

    @Test
    public void failReadById() {
        assertThrows(EntityNotFoundException.class, () -> service.delete(UUID.randomUUID()));
    }

    @Test
    public void successDelete() {
        when(repository.save(member)).thenReturn(member);
        when(repository.findById(any())).thenReturn(Optional.of(member));
        doNothing().when(repository).deleteById(any());

        member.setId(UUID.randomUUID());
        Member expected = service.create(member);
        Member actual = service.delete(expected.getId());

        assertEquals(expected.getCreatedBy(), actual.getCreatedBy());

        verify(repository, times(1)).save(member);
        verify(repository, times(1)).findById(any());
        verify(repository, times(1)).deleteById(any());
    }
}
