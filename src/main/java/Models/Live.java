package Models;

import GUI.Board;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Data
public class Live extends JPanel {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, x, y, this);
    }
}
