package com.spd.trello.service;

import com.spd.trello.domain.Board;
import com.spd.trello.domain.CardList;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardService extends AbstractService<Board> {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public Board create(boolean addToList) {
        Board board = new Board();
        System.out.println("Input name of Board");
        board.setName(scanner.nextLine());
        System.out.println("Input description of Board:");
        board.setDescription(scanner.nextLine());

        if (addToList) {
            System.out.println("Do you want to add cardList? Y/N");
            if (scanner.next().equals("Y"))
                board.setCardLists(addCardLists());
            items.add(board);
        }

        return board;
    }

    private List<CardList> addCardLists() {
        List<CardList> cardLists = new ArrayList<>();
        CardListService cardListService = new CardListService();
        int mark = 1;
        while (mark == 1) {
            cardLists.add(cardListService.create(true));
            System.out.println("If you want to add one more cardList press '1' else press '0' ");
            mark = scanner.nextInt();
        }
        return cardLists;
    }

    @Override
    public Board update(int index, Board obj) {
        Board boardToUpdate = items.get(index);
        boardToUpdate.setName(obj.getName());
        boardToUpdate.setDescription(obj.getDescription());
        return boardToUpdate;
    }
}
