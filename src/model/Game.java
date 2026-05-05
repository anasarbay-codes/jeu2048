package model;

public class Game {
    private Grid grid;
    private Player player;
    private int score;

    public Game(Player player) {
        this.player = player;
        this.grid = new Grid(4);
        this.score = 0;
    }

    public void start() { grid = new Grid(4); score = 0; player.setScore(0); }

    public boolean checkWin() {
        Tile[][] tiles = grid.getTiles();
        for (Tile[] row : tiles)
            for (Tile t : row)
                if (t.getValue() == 2048) return true;
        return false;
    }

    public boolean checkLose() {
        return grid.isFull() && !grid.canMove();
    }

    public void updateScore(int points) { score += points; player.setScore(score); }
    public int getScore() { return score; }
    public Grid getGrid() { return grid; }
    public Player getPlayer() { return player; }
}
