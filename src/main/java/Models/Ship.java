package Models;

import GUI.Board;
import lombok.Data;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

@Data
public class Ship implements KeyListener, Runnable {

    private final Board board;
    private final Image image;
    private int x;
    private int y;
    private double angle = 0;
    private double velocity = 0;

    public Ship(Image image, Board board, int x, int y) {
        this.image = image;
        this.board = board;
        this.x = x;
        this.y = y;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        System.out.println(e);
        switch (e.getKeyChar()) {
            case 'w' -> {
                this.velocity += 0.5;
            }
            case 's' -> {
                this.velocity -= 0.5;
            }
            case 'd' -> { //dotaje 45 do angle. Rotacja w prawo
                this.angle += 5;
            }
            case 'a' -> { //odejmuje 45 od angle. Rotacja w lewo
                this.angle -= 5;
            }
            case ' ' -> {
                final var laser = new Laser(this);
                synchronized (this.board.getLasers()) {
                    this.board.getLasers().add(laser);
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    @Override
    public void run() {
        this.velocity *= 0.99;
        if (this.velocity < 0.1 && this.velocity > -0.1)
            this.velocity = 0;

        double radians = Math.toRadians(this.angle);
        this.x += this.velocity*Math.sin(radians);
        this.y -= this.velocity*Math.cos(radians);

        if (this.x < 0) this.x = 0;
        if (this.x > this.board.getWidth()-this.getWidth())
            this.x = this.board.getWidth()-this.getWidth();
        if (this.y < 0) this.y = 0;
        if (this.y > this.board.getHeight()-this.getHeight())
            this.y = this.board.getHeight()-this.getHeight();

//        System.out.println(this.velocity);
    }

    public Image getImage() {
        return this.rotate((BufferedImage) this.image, this.angle);
    }

    public boolean isShipColidingWithComet(Comet comet) {
        return (comet.getX() >= this.x && comet.getX() <= (this.x + this.getWidth()) &&
                comet.getY() <= (this.y+this.getHeight()) && comet.getY() >= this.y);
    }

    public BufferedImage rotate(BufferedImage image, Double degrees) {
        // Calculate the new size of the image based on the angle of rotaion

        double radians = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));

        int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

        // Create a new image
        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();
        // Calculate the "anchor" point around which the image will be rotated
        int x = (newWidth - image.getWidth()) / 2;
        int y = (newHeight - image.getHeight()) / 2;
        // Transform the origin point around the anchor point
        AffineTransform at = new AffineTransform();
        at.setToRotation(radians, x + (image.getWidth() / 2), y + (image.getHeight() / 2));
        at.translate(x, y);
        g2d.setTransform(at);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotate;
    }
    public int getWidth() {
        return this.getImage().getWidth(null);
    }
    public int getHeight() {
        return this.getImage().getHeight(null);
    }

}
