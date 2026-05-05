package model;

import java.util.Random;

public class Grid {
    private Tile[][] tiles;
    private int size;
    private Random random = new Random();

    public Grid(int size) {
        this.size = size;
        tiles = new Tile[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                tiles[i][j] = new Tile(0);
        addRandomTile();
        addRandomTile();
    }

    public int move(Direction dir) {
        boolean moved = false;
        int points = 0;
        if (dir == Direction.LEFT) {
            for (int i = 0; i < size; i++) {
                int[] line = new int[size];
                int idx = 0;
                for (int j = 0; j < size; j++) {
                    if (tiles[i][j].getValue() != 0) {
                        line[idx++] = tiles[i][j].getValue();
                    }
                }
                for (int j = 0; j < idx - 1; j++) {
                    if (line[j] == line[j + 1]) {
                        line[j] *= 2;
                        points += line[j];
                        line[j + 1] = 0;
                        moved = true;
                    }
                }
                int[] newLine = new int[size];
                int k = 0;
                for (int j = 0; j < size; j++) {
                    if (line[j] != 0) {
                        newLine[k++] = line[j];
                    }
                }
                for (int j = 0; j < size; j++) {
                    if (tiles[i][j].getValue() != newLine[j]) {
                        moved = true;
                    }
                    tiles[i][j].setValue(newLine[j]);
                }
            }
        } else if (dir == Direction.RIGHT) {
            for (int i = 0; i < size; i++) {
                int[] line = new int[size];
                int idx = size - 1;
                for (int j = size - 1; j >= 0; j--) {
                    if (tiles[i][j].getValue() != 0) {
                        line[idx--] = tiles[i][j].getValue();
                    }
                }
                for (int j = size - 1; j > 0; j--) {
                    if (line[j] == line[j - 1]) {
                        line[j] *= 2;
                        points += line[j];
                        line[j - 1] = 0;
                        moved = true;
                    }
                }
                int[] newLine = new int[size];
                int k = size - 1;
                for (int j = size - 1; j >= 0; j--) {
                    if (line[j] != 0) {
                        newLine[k--] = line[j];
                    }
                }
                for (int j = 0; j < size; j++) {
                    if (tiles[i][j].getValue() != newLine[j]) {
                        moved = true;
                    }
                    tiles[i][j].setValue(newLine[j]);
                }
            }
        } else if (dir == Direction.UP) {
            for (int j = 0; j < size; j++) {
                int[] line = new int[size];
                int idx = 0;
                for (int i = 0; i < size; i++) {
                    if (tiles[i][j].getValue() != 0) {
                        line[idx++] = tiles[i][j].getValue();
                    }
                }
                for (int i = 0; i < idx - 1; i++) {
                    if (line[i] == line[i + 1]) {
                        line[i] *= 2;
                        points += line[i];
                        line[i + 1] = 0;
                        moved = true;
                    }
                }
                int[] newLine = new int[size];
                int k = 0;
                for (int i = 0; i < size; i++) {
                    if (line[i] != 0) {
                        newLine[k++] = line[i];
                    }
                }
                for (int i = 0; i < size; i++) {
                    if (tiles[i][j].getValue() != newLine[i]) {
                        moved = true;
                    }
                    tiles[i][j].setValue(newLine[i]);
                }
            }
        } else if (dir == Direction.DOWN) {
            for (int j = 0; j < size; j++) {
                int[] line = new int[size];
                int idx = size - 1;
                for (int i = size - 1; i >= 0; i--) {
                    if (tiles[i][j].getValue() != 0) {
                        line[idx--] = tiles[i][j].getValue();
                    }
                }
                for (int i = size - 1; i > 0; i--) {
                    if (line[i] == line[i - 1]) {
                        line[i] *= 2;
                        points += line[i];
                        line[i - 1] = 0;
                        moved = true;
                    }
                }
                int[] newLine = new int[size];
                int k = size - 1;
                for (int i = size - 1; i >= 0; i--) {
                    if (line[i] != 0) {
                        newLine[k--] = line[i];
                    }
                }
                for (int i = 0; i < size; i++) {
                    if (tiles[i][j].getValue() != newLine[i]) {
                        moved = true;
                    }
                    tiles[i][j].setValue(newLine[i]);
                }
            }
        }
        if (moved) addRandomTile();
        return moved ? points : -1;
    }

    public void mergeTiles(Direction dir) {
        // TODO: implement merging logic
    }

    public void addRandomTile() {
        int x, y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (!tiles[x][y].isEmpty());
        tiles[x][y].setValue(random.nextInt(10) < 9 ? 2 : 4);
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (tiles[i][j].isEmpty()) return false;
        return true;
    }

    public boolean canMove() {
        // Vérifie s'il existe un mouvement possible (cases adjacentes égales ou case vide)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j].isEmpty()) return true;
                if (i < size - 1 && tiles[i][j].getValue() == tiles[i + 1][j].getValue()) return true;
                if (j < size - 1 && tiles[i][j].getValue() == tiles[i][j + 1].getValue()) return true;
            }
        }
        return false;
    }

    public Tile[][] getTiles() { return tiles; }
}
