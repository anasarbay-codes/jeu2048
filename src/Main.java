import javax.swing.*;
import model.*;
import view.*;
import controller.*;

public class Main {
    public static void main(String[] args) {
        // 1. Initialiser la base de données
        DatabaseManager dbManager = new DatabaseManager();

        // 2. Créer joueur et jeu
        Player player = new Player("Joueur");
        Game game = new Game(player);

        GameView gameView = new GameView(game.getGrid());
        GameController controller = new GameController(game, gameView, dbManager);
        GamePanel gamePanel = new GamePanel(game, controller, dbManager);

        JFrame frame = new JFrame("Jeu 2048");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(gamePanel);
        frame.setSize(430, 560);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Fermeture propre de la BD
        Runtime.getRuntime().addShutdownHook(new Thread(dbManager::close));

        // Touches clavier
        gamePanel.getGameView().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                switch (evt.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP    -> controller.handleKeyPress(Direction.UP);
                    case java.awt.event.KeyEvent.VK_DOWN  -> controller.handleKeyPress(Direction.DOWN);
                    case java.awt.event.KeyEvent.VK_LEFT  -> controller.handleKeyPress(Direction.LEFT);
                    case java.awt.event.KeyEvent.VK_RIGHT -> controller.handleKeyPress(Direction.RIGHT);
                }
                gamePanel.setScore(game.getScore());
            }
        });
        gamePanel.getGameView().requestFocusInWindow();

        // Bouton Nouveau Jeu
        gamePanel.getNewGameButton().addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Entrez le nom du joueur :", player.getName());
            if (name != null && !name.isEmpty()) {
                Player newPlayer = new Player(name);
                Game newGame = new Game(newPlayer);
                GameView newGameView = new GameView(newGame.getGrid());
                GameController newController = new GameController(newGame, newGameView, dbManager);
                GamePanel newPanel = new GamePanel(newGame, newController, dbManager);
                frame.setContentPane(newPanel);
                frame.revalidate();
                frame.repaint();
                newPanel.getGameView().requestFocusInWindow();

                newPanel.getGameView().addKeyListener(new java.awt.event.KeyAdapter() {
                    @Override
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        switch (evt.getKeyCode()) {
                            case java.awt.event.KeyEvent.VK_UP    -> newController.handleKeyPress(Direction.UP);
                            case java.awt.event.KeyEvent.VK_DOWN  -> newController.handleKeyPress(Direction.DOWN);
                            case java.awt.event.KeyEvent.VK_LEFT  -> newController.handleKeyPress(Direction.LEFT);
                            case java.awt.event.KeyEvent.VK_RIGHT -> newController.handleKeyPress(Direction.RIGHT);
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

                newPanel.getSaveButton().addActionListener(ev -> {
                    dbManager.saveScore(newPlayer);
                    newPanel.updateBestScore(dbManager);
                    JOptionPane.showMessageDialog(frame, "Score de " + newPlayer.getName() + " sauvegardé !", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
                    newPanel.getGameView().requestFocusInWindow();
                });

                newPanel.getScoresButton().addActionListener(ev -> {
                    ScoreBoardView.show(frame, dbManager);
                    newPanel.getGameView().requestFocusInWindow();
                });
            }
        });

        // Bouton Reset
        gamePanel.getResetButton().addActionListener(e -> {
            game.start();
            gameView.render(game.getGrid());
            gamePanel.setScore(game.getScore());
            gamePanel.getGameView().requestFocusInWindow();
        });

        // Bouton Sauvegarder
        gamePanel.getSaveButton().addActionListener(e -> {
            dbManager.saveScore(player);
            gamePanel.updateBestScore(dbManager);
            JOptionPane.showMessageDialog(frame, "Score de " + player.getName() + " sauvegardé !", "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
            gamePanel.getGameView().requestFocusInWindow();
        });

        // Bouton Scores
        gamePanel.getScoresButton().addActionListener(e -> {
            ScoreBoardView.show(frame, dbManager);
            gamePanel.getGameView().requestFocusInWindow();
        });
    }
}
