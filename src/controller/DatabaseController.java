package controller;

import model.DatabaseManager;
import model.Player;
import java.util.List;

public class DatabaseController {
    private DatabaseManager dbManager;

    public DatabaseController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void save(Player player) {
        dbManager.saveScore(player);
    }

    public List<Player> loadScores() {
        return dbManager.getHighScores();
    }
}
