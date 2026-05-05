package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    private Connection connection;

    public DatabaseManager() {
        init();
    }

    private void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS scores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "score INTEGER NOT NULL," +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            stmt.close();
        } catch (Exception e) {
            System.err.println("Erreur initialisation BD: " + e.getMessage());
        }
    }

    public void saveScore(Player player) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO scores (name, score) VALUES (?, ?)"
            );
            ps.setString(1, player.getName());
            ps.setInt(2, player.getScore());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde: " + e.getMessage());
        }
    }

    public List<Player> getHighScores() {
        return getHighScores(10);
    }

    public List<Player> getHighScores(int limit) {
        List<Player> list = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT name, score FROM scores ORDER BY score DESC LIMIT ?"
            );
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Player p = new Player(rs.getString("name"));
                p.setScore(rs.getInt("score"));
                list.add(p);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Erreur chargement scores: " + e.getMessage());
        }
        return list;
    }

    public Player getTopPlayer() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT name, score FROM scores ORDER BY score DESC LIMIT 1"
            );
            if (rs.next()) {
                Player p = new Player(rs.getString("name"));
                p.setScore(rs.getInt("score"));
                rs.close();
                stmt.close();
                return p;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur top joueur: " + e.getMessage());
        }
        return null;
    }

    public void deleteAllScores() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM scores");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur suppression scores: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture BD: " + e.getMessage());
        }
    }
}
