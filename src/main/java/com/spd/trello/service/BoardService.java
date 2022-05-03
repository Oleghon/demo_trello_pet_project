package com.spd.trello.service;

import com.spd.trello.domain.resources.Board;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.repository_jpa.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Slf4j
@Service
public class BoardService extends ArchivedResourceService<Board, BoardRepository> {
    private MemberService memberService;
    public BoardService(BoardRepository repository, MemberService memberService) {
        super(repository);
        this.memberService = memberService;
    }

    public Board addMember(UUID id, Member member) {
        Board board = super.readById(id);
        Member checkedMember = memberService.findByUserIdAndRole(member.getUserId(), member.getRole());
        board.getMembers().add(checkedMember.getId());
        log.info("member successfully added to board");
        return super.create(board);
    }
}
