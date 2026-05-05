package controller;

import view.MenuView;

public class MenuController {
    private MenuView menuView;

    public MenuController(MenuView menuView) {
        this.menuView = menuView;
    }

    public void startGame() {
        menuView.showMenu();
    }

    public void showScores() {
        System.out.println("Affichage des scores...");
    }
}
