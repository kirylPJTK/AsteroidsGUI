package Models;

import lombok.Data;

import java.awt.*;
@Data
public class Laser implements Runnable{
    private int xBack;
    private int yBack;
    private int xFront;
    private int yFront;
    private final static int l = 20;


    public Laser (Ship ship) {//todo do zmiany

        this.xBack = ship.getX()+20;
        this.yBack = ship.getY()+20;
        this.xFront = (int) (l*Math.sin(Math.toRadians(ship.getAngle()))) + xBack;
        this.yFront = (int) (-l*Math.cos(Math.toRadians(ship.getAngle()))) + yBack;
    }


    @Override
    public void run() {
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
}
