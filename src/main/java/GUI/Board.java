package GUI;

import Models.Comet;
import Models.Ship;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Board extends JPanel implements Runnable{


    private final List<Comet> comets;
    private final Ship ship;


    public Board() throws IOException {

        Image image = ImageIO.read(new File("src/main/java/Images/ship_64.png"));
        this.ship = new Ship(image, this, 20,25);
        this.comets = new LinkedList<>();
        Image cometImage = ImageIO.read(new File("src/main/java/Images/coment_64.png"));

        comets.add(new Comet(cometImage, this, 0, 0));
        comets.add(new Comet(cometImage, this, 0, 0));
        comets.add(new Comet(cometImage, this, 0, 0));
        comets.add(new Comet(cometImage, this, 0, 0));
        comets.add(new Comet(cometImage, this, 0, 0));
        comets.add(new Comet(cometImage, this, 0, 0));


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

        for(Comet comet : comets) {
            g.drawImage(comet.getImage(), comet.getX(), comet.getY(), comet.getWidth(), comet.getHeight(), this);
        }

    }

    @Override
    public void run() {
        this.repaint();
        this.ship.run();
        this.comets.forEach(Comet::run);
    }
}
