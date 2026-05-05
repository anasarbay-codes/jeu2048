package view;

import javax.swing.*;
import java.awt.*;
import model.*;
import controller.GameController;

public class GamePanel extends JPanel {
    private JLabel scoreLabel;
    private JLabel bestScoreLabel;
    private GameView gameView;
    private JButton newGameButton;
    private JButton resetButton;
    private JButton saveButton;
    private JButton scoresButton;

    public GamePanel(Game game, GameController controller, DatabaseManager dbManager) {
        setLayout(new BorderLayout());

        // Panneau du haut : score actuel + meilleur score
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        scoreLabel = new JLabel("Score : 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));

        Player top = dbManager.getTopPlayer();
        String bestText = top != null
            ? "Meilleur score : " + top.getScore() + " (" + top.getName() + ")"
            : "Meilleur score : ---";
        bestScoreLabel = new JLabel(bestText, SwingConstants.CENTER);
        bestScoreLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        bestScoreLabel.setForeground(new Color(180, 80, 0));

        topPanel.add(scoreLabel);
        topPanel.add(bestScoreLabel);
        add(topPanel, BorderLayout.NORTH);

        // Grille au centre
        gameView = controller.getView();
        add(gameView, BorderLayout.CENTER);

        // Boutons en bas
        JPanel buttonPanel = new JPanel(new FlowLayout());
        newGameButton = new JButton("Nouveau Jeu");
        resetButton = new JButton("Reset");
        saveButton = new JButton("Sauvegarder");
        scoresButton = new JButton("🏆 Scores");
        buttonPanel.add(newGameButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(scoresButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setScore(int score) {
        scoreLabel.setText("Score : " + score);
    }

    public void updateBestScore(DatabaseManager dbManager) {
        Player top = dbManager.getTopPlayer();
        if (top != null) {
            bestScoreLabel.setText("Meilleur score : " + top.getScore() + " (" + top.getName() + ")");
        }
    }

    public JButton getNewGameButton() { return newGameButton; }
    public JButton getResetButton() { return resetButton; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getScoresButton() { return scoresButton; }
    public GameView getGameView() { return gameView; }
}
