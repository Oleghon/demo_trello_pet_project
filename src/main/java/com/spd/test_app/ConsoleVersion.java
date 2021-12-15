package com.spd.test_app;

import com.spd.trello.domain.Resource;
import com.spd.trello.service.AbstractService;
import com.spd.trello.service.BoardService;
import com.spd.trello.service.CardListService;
import com.spd.trello.service.CardService;

import java.util.Scanner;

public class ConsoleVersion {
    static CardListService cardListService = new CardListService();
    static BoardService boardService = new BoardService();
    static CardService cardService = new CardService();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int marker = 1;
        System.out.println("Welcome to \"Console Demo Trello\"");
        while (marker == 1) {
            System.out.println("Input number of action:\n" +
                    "#1: create new Board;\n" +
                    "#2: update Board;\n" +
                    "#3: show Board;\n" +
                    "#4: create new CardList;\n" +
                    "#5: update CardList;\n" +
                    "#6: show CardList;\n" +
                    "#7: create new Card;\n" +
                    "#8: update Card;\n" +
                    "#9: show Card;");
            int codeAction = scanner.nextInt();
            chooseAction(codeAction);
            System.out.println("if you want to choose other action input '1'");
            marker = scanner.nextInt();
        }
    }

    static void chooseAction(int codeAction) {
        switch (codeAction) {
            case 1:
                createItem(boardService);
                break;
            case 2:
                updateItem(boardService);
                break;
            case 3:
                showItem(boardService);
                break;
            case 4:
                createItem(cardListService);
                break;
            case 5:
                updateItem(cardListService);
                break;
            case 6:
                showItem(cardListService);
                break;
            case 7:
                createItem(cardService);
                break;
            case 8:
                updateItem(cardService);
                break;
            case 9:
                showItem(cardService);
                break;
            default:
                System.out.println("incorrect number of action");
        }
    }

    private static void createItem(AbstractService service) {
        service.create(true);
        System.out.println("Create successful");
    }

    private static void updateItem(AbstractService service) {
        service.showItems();
        System.out.println("Choose item to update:");
        int index = scanner.nextInt();
        Resource resource = service.create(false);
        service.update(index, resource);
        System.out.println("update successful");
    }

    private static void showItem(AbstractService service) {
        service.show();
    }
}
