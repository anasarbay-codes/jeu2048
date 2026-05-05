package view;

import model.Tile;

public class TileView {
    public void display(Tile tile) {
        System.out.print(tile.getValue() + "\t");
    }
}
