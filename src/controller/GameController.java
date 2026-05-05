package controller;

import javax.swing.JOptionPane;
import model.*;
import view.GameView;

public class GameController {
    private Game game;
    private GameView view;
    private DatabaseManager dbManager;

    public GameController(Game game, GameView view, DatabaseManager dbManager) {
        this.game = game;
        this.view = view;
        this.dbManager = dbManager;
    }

    public GameView getView() { return view; }
    public Game getGame() { return game; }

    public void updateScore(int points) {
        game.updateScore(points);
    }

    public void handleKeyPress(Direction dir) {
        int res = game.getGrid().move(dir);
        if (res >= 0) {
            if (res > 0) updateScore(res);
            view.render(game.getGrid());
        }
        if (game.checkWin()) {
            dbManager.saveScore(game.getPlayer());
            JOptionPane.showMessageDialog(view, "Bravo " + game.getPlayer().getName() + ", tu as gagné avec " + game.getScore() + " points !", "Victoire", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        if (game.checkLose()) {
            dbManager.saveScore(game.getPlayer());
            JOptionPane.showMessageDialog(view, "Game Over ! Score final : " + game.getScore(), "Défaite", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        view.requestFocusInWindow();
    }
}
