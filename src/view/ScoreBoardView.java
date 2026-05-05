package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.DatabaseManager;
import model.Player;

public class ScoreBoardView extends JDialog {

    public ScoreBoardView(JFrame parent, DatabaseManager dbManager) {
        super(parent, " Meilleurs Scores - Top 10", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Titre
        JLabel title = new JLabel(" Classement des Meilleurs Joueurs", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Tableau
        String[] columns = {"#", "Joueur", "Score"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Player> scores = dbManager.getHighScores(10);
        for (int i = 0; i < scores.size(); i++) {
            Player p = scores.get(i);
            String rank = (i == 0) ? "1" : (i == 1) ? "2" : (i == 2) ? "3" : String.valueOf(i + 1);
            model.addRow(new Object[]{rank, p.getName(), p.getScore()});
        }

        if (scores.isEmpty()) {
            model.addRow(new Object[]{"---", "Aucun score enregistré", "---"});
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);

        // Boutons bas
        JPanel bottom = new JPanel(new FlowLayout());
        JButton closeBtn = new JButton("Fermer");
        JButton clearBtn = new JButton("Effacer tous les scores");
        clearBtn.setForeground(Color.RED);

        closeBtn.addActionListener(e -> dispose());
        clearBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment effacer tous les scores ?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dbManager.deleteAllScores();
                model.setRowCount(0);
                model.addRow(new Object[]{"---", "Aucun score enregistré", "---"});
            }
        });

        bottom.add(closeBtn);
        bottom.add(clearBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    public static void show(JFrame parent, DatabaseManager dbManager) {
        new ScoreBoardView(parent, dbManager).setVisible(true);
    }
}
