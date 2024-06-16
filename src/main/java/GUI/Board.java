package GUI;

import Models.Ship;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Board extends JPanel implements Runnable{
    private final Ship ship;
    public Board() throws IOException {

        Image image = ImageIO.read(new File("src/main/java/Images/ship.png"));
        this.ship = new Ship(image, this, 20,25);


        this.setPreferredSize(new Dimension(1200, 800));
        this.setBackground(new Color(3, 3, 19));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this.ship);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(this.ship.getImage(), this.ship.getX(), this.ship.getY(), this.ship.getWidth(), this.ship.getHeight(), this);

    }

    @Override
    public void run() {
//        this.ship.setX(this.ship.getX() + 1);
        this.repaint();
        this.ship.run();
    }
}
