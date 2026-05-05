package view;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import model.Grid;
import model.Tile;

public class GameView extends JPanel {
    private Grid grid;
    private static final Color EMPTY_TILE_COLOR = Color.LIGHT_GRAY;
    private static final Color TEXT_DARK = new Color(0x776E65);
    private static final Color TEXT_LIGHT = new Color(0xF9F6F2);

    private static final Color COLOR_2 = new Color(0xEEE4DA);
    private static final Color COLOR_4 = new Color(0xEDE0C8);
    private static final Color COLOR_8 = new Color(0xF2B179);
    private static final Color COLOR_16 = new Color(0xF59563);
    private static final Color COLOR_32 = new Color(0xF67C5F);
    private static final Color COLOR_64 = new Color(0xF65E3B);
    private static final Color COLOR_128 = new Color(0xEDCF72);
    private static final Color COLOR_256 = new Color(0xEDCC61);
    private static final Color COLOR_512 = new Color(0xEDC850);
    private static final Color COLOR_1024 = new Color(0xEDC53F);
    private static final Color COLOR_2048 = new Color(0xEDC22E);

    public GameView(Grid grid) {
        this.grid = grid;
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Tile[][] tiles = grid.getTiles();
        int tileSize = 80;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                int value = tiles[i][j].getValue();
                int x = j * tileSize;
                int y = i * tileSize;
                g.setColor(getTileBackgroundColor(value));
                g.fillRect(x, y, tileSize, tileSize);
                g.setColor(getTileBorderColor(value));
                g.drawRect(x, y, tileSize, tileSize);
                if (value != 0) {
                    g.setColor(getTileTextColor(value));
                    g.setFont(new Font("Arial", Font.BOLD, 24));
                    String text = String.valueOf(value);
                    int textWidth = g.getFontMetrics().stringWidth(text);
                    int textHeight = g.getFontMetrics().getAscent();
                    g.drawString(text, x + (tileSize - textWidth) / 2, y + (tileSize + textHeight) / 2 - 5);
                }
            }
        }
    }

    private Color getTileBackgroundColor(int value) {
        switch (value) {
            case 0:
                return EMPTY_TILE_COLOR;
            case 2:
                return COLOR_2;
            case 4:
                return COLOR_4;
            case 8:
                return COLOR_8;
            case 16:
                return COLOR_16;
            case 32:
                return COLOR_32;
            case 64:
                return COLOR_64;
            case 128:
                return COLOR_128;
            case 256:
                return COLOR_256;
            case 512:
                return COLOR_512;
            case 1024:
                return COLOR_1024;
            case 2048:
                return COLOR_2048;
            default:
                return Color.DARK_GRAY;
        }
    }

    private Color getTileTextColor(int value) {
        if (value == 2 || value == 4) {
            return TEXT_DARK;
        }
        return TEXT_LIGHT;
    }

    private Color getTileBorderColor(int value) {
        if (value == 0) {
            return Color.GRAY;
        }
        return getTileBackgroundColor(value).darker();
    }

    public void render(Grid grid) {
        this.grid = grid;
        repaint();
    }
}
