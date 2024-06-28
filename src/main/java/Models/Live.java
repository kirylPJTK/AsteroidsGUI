package Models;

import GUI.Board;
import lombok.Data;
import java.awt.*;

@Data
public class Live {
    private final Board board;
    private final Image image;
    private int x;
    private int y;

    public Live(Image image, Board board, int x, int y) {
        this.board = board;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return this.getImage().getWidth(null);
    }

    public int getHeight() {
        return this.getImage().getHeight(null);
    }
}
