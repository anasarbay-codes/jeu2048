import javax.swing.*;
import model.*;
import view.*;
import controller.*;

public class LaunchGame {
    public static void launch() throws Exception {
        // 1. Initialiser la base de donnees
        DatabaseManager dbManager = new DatabaseManager();

        // 2. Creer joueur et jeu
        Player player = new Player("Joueur");
        Game game = new Game(player);

        GameView gameView = new GameView(game.getGrid());
        GameController controller = new GameController(game, gameView, dbManager);
        String bestText = buildBestScoreText(dbManager.getTopPlayer());
        GamePanel gamePanel = new GamePanel(game, controller, bestText);

        JFrame frame = new JFrame("Jeu 2048");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(gamePanel);
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Fermeture propre de la BD
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            Main.runSafely(dbManager::close)
        ));

        gamePanel.getGameView().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                switch (evt.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP    -> Main.runSafely(() -> controller.handleKeyPress(Direction.UP));
                    case java.awt.event.KeyEvent.VK_DOWN  -> Main.runSafely(() -> controller.handleKeyPress(Direction.DOWN));
                    case java.awt.event.KeyEvent.VK_LEFT  -> Main.runSafely(() -> controller.handleKeyPress(Direction.LEFT));
                    case java.awt.event.KeyEvent.VK_RIGHT -> Main.runSafely(() -> controller.handleKeyPress(Direction.RIGHT));
                }
                gamePanel.setScore(game.getScore());
            }
        });
        gamePanel.getGameView().requestFocusInWindow();

        // Bouton Nouveau Jeu
        gamePanel.getNewGameButton().addActionListener(e -> Main.runSafely(() -> {
            String name = JOptionPane.showInputDialog(frame, "Entrez le nom du joueur :", player.getName());
            if (name != null && !name.isEmpty()) {
                Player newPlayer = new Player(name);
                Game newGame = new Game(newPlayer);
                GameView newGameView = new GameView(newGame.getGrid());
                GameController newController = new GameController(newGame, newGameView, dbManager);
                String newBestText = buildBestScoreText(dbManager.getTopPlayer());
                GamePanel newPanel = new GamePanel(newGame, newController, newBestText);
                frame.setContentPane(newPanel);
                frame.revalidate();
                frame.repaint();
                newPanel.getGameView().requestFocusInWindow();

                newPanel.getGameView().addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        switch (evt.getKeyCode()) {
                            case java.awt.event.KeyEvent.VK_UP    -> Main.runSafely(() -> newController.handleKeyPress(Direction.UP));
                            case java.awt.event.KeyEvent.VK_DOWN  -> Main.runSafely(() -> newController.handleKeyPress(Direction.DOWN));
                            case java.awt.event.KeyEvent.VK_LEFT  -> Main.runSafely(() -> newController.handleKeyPress(Direction.LEFT));
                            case java.awt.event.KeyEvent.VK_RIGHT -> Main.runSafely(() -> newController.handleKeyPress(Direction.RIGHT));
                        }
                        newPanel.setScore(newGame.getScore());
                    }
                });

                newPanel.getResetButton().addActionListener(ev -> {
                    newGame.start();
                    newGameView.render(newGame.getGrid());
                    newPanel.setScore(newGame.getScore());
                    newPanel.getGameView().requestFocusInWindow();
                });

                newPanel.getSaveButton().addActionListener(ev -> Main.runSafely(() -> {
                    dbManager.saveScore(newPlayer);
                    newPanel.setBestScoreText(buildBestScoreText(dbManager.getTopPlayer()));
                    JOptionPane.showMessageDialog(frame, "Score de " + newPlayer.getName() + " sauvegarde !", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
                    newPanel.getGameView().requestFocusInWindow();
                }));

                newPanel.getScoresButton().addActionListener(ev -> Main.runSafely(() -> {
                    ScoreBoardView.show(
                        frame,
                        dbManager.getHighScores(10),
                        () -> Main.runSafely(dbManager::deleteAllScores)
                    );
                    newPanel.getGameView().requestFocusInWindow();
                }));
            }
        }));

        // Bouton Reset
        gamePanel.getResetButton().addActionListener(e -> {
            game.start();
            gameView.render(game.getGrid());
            gamePanel.setScore(game.getScore());
            gamePanel.getGameView().requestFocusInWindow();
        });

        // Bouton Sauvegarder
        gamePanel.getSaveButton().addActionListener(e -> Main.runSafely(() -> {
            dbManager.saveScore(player);
            gamePanel.setBestScoreText(buildBestScoreText(dbManager.getTopPlayer()));
            JOptionPane.showMessageDialog(frame, "Score de " + player.getName() + " sauvegarde !", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
            gamePanel.getGameView().requestFocusInWindow();
        }));

        // Bouton Scores
        gamePanel.getScoresButton().addActionListener(e -> Main.runSafely(() -> {
            ScoreBoardView.show(
                frame,
                dbManager.getHighScores(10),
                () -> Main.runSafely(dbManager::deleteAllScores)
            );
            gamePanel.getGameView().requestFocusInWindow();
        }));
    }

    private static String buildBestScoreText(Player top) {
        return top != null
            ? "Meilleur score : " + top.getScore() + " (" + top.getName() + ")"
            : "Meilleur score : ---";
    }

    public static void main(String[] args) {
        Main.runSafely(LaunchGame::launch);
    }
}
