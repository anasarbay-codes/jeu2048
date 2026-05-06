package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private Connection connection;

    public DatabaseManager() throws Exception {
        init();
    }

    private void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(DB_URL);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "score INTEGER NOT NULL," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
        }
    }

    public void saveScore(Player player) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
            "INSERT INTO scores (name, score) VALUES (?, ?)"
        )) {
            ps.setString(1, player.getName());
            ps.setInt(2, player.getScore());
            ps.executeUpdate();
        }
    }

    public List<Player> getHighScores() throws SQLException {
        return getHighScores(10);
    }

    public List<Player> getHighScores(int limit) throws SQLException {
        List<Player> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT name, score FROM scores ORDER BY score DESC LIMIT ?"
        )) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Player p = new Player(rs.getString("name"));
                    p.setScore(rs.getInt("score"));
                    list.add(p);
                }
            }
        }
        return list;
    }

    public Player getTopPlayer() throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT name, score FROM scores ORDER BY score DESC LIMIT 1"
             )) {
            if (rs.next()) {
                Player p = new Player(rs.getString("name"));
                p.setScore(rs.getInt("score"));
                return p;
            }
        }
        return null;
    }

    public void deleteAllScores() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM scores");
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
