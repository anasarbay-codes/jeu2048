package controller;

import model.DatabaseManager;
import model.Player;
import java.util.List;
import java.sql.SQLException;

public class DatabaseController {
    private DatabaseManager dbManager;

    public DatabaseController(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void save(Player player) throws SQLException {
        dbManager.saveScore(player);
    }

    public List<Player> loadScores() throws SQLException {
        return dbManager.getHighScores();
    }
}
