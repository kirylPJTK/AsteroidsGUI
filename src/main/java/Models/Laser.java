package Models;

import GUI.Board;
import lombok.Data;

import java.awt.*;
@Data
public class Laser implements Runnable{
    private final static int LENGTH = 20;
    private final Board board;

    private int xBack;
    private int yBack;
    private int xFront;
    private int yFront;

    public Laser (Ship ship) {
        this.xBack = ship.getX()+32;
        this.yBack = ship.getY()+32;
        this.xFront = (int) (LENGTH *Math.sin(Math.toRadians(ship.getAngle()))) + xBack;
        this.yFront = (int) (-LENGTH *Math.cos(Math.toRadians(ship.getAngle()))) + yBack;
        this.board = ship.getBoard();
    }

    // Logic for laser
    @Override
    public void run() {
        if (this.isLaserOutOfBounds()){
            this.board.removeLaser(this);
            return;
        }

        final int deltaX = xFront-xBack;
        final int deltaY = yFront-yBack;

        this.xBack += deltaX;
        this.xFront += deltaX;
        this.yBack += deltaY;
        this.yFront += deltaY;

    }

    public void paint(Graphics g) {
        if (g instanceof Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.RED);
            g2d.drawLine(xBack, yBack, xFront, yFront);
        }
    }

    // Check if laser out of board
    public boolean isLaserOutOfBounds(){
        return this.xBack > board.getWidth() || this.yBack > board.getHeight() || this.xBack < 0 || this.yBack < 0;
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
