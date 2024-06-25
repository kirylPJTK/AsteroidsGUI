package Models;

import GUI.Board;
import lombok.Data;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

@Data
public class Comet implements Runnable {
    private final Board board;
    private final Image image;
    private int x;
    private int y;
    private double angle = 360 * Math.random();
    private double velocity = 5;
    private Random rand = new Random();

    public Comet(Image image, Board board, int x, int y) {
        this.image = image;
        this.board = board;
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        if (this.velocity < 0.1 && this.velocity > -0.1) {
            this.velocity = 0;
        }

        // Adding random acceleration to simulate gravity-like effect
        double acceleration = (rand.nextDouble() * 2 - 1) * 0.1; // Random acceleration between -0.1 and 0.1
        this.velocity += acceleration;

        // Adding random change to the angle
        double angleChange = (rand.nextDouble() * 2 - 1) * 5; // Random angle change between -5 and 5 degrees
        this.angle += angleChange;

        // Update position based on velocity and angle
        double radians = Math.toRadians(this.angle);
        this.x += this.velocity * Math.sin(radians);
        this.y -= this.velocity * Math.cos(radians);

        // Implement screen wrapping or boundary conditions
        if (this.x < 0) this.x = 0;
        if (this.x > this.board.getWidth() - this.getWidth())
            this.x = this.board.getWidth() - this.getWidth();
        if (this.y < 0) this.y = 0;
        if (this.y > this.board.getHeight() - this.getHeight())
            this.y = this.board.getHeight() - this.getHeight();

        System.out.println(this.velocity);
    }

    public Image getImage() {
        return this.rotate((BufferedImage) this.image, this.angle);
    }

    public BufferedImage rotate(BufferedImage image, Double degrees) {
        // Calculate the new size of the image based on the angle of rotation
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