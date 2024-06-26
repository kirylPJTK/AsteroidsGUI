package GUI;

import Models.Comet;
import Models.Ship;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Board extends JPanel implements Runnable{

    Timer timer;
    private int delay = 5;
    //todo memory leak comets
    private final Set<Comet> comets;
    private final Ship ship;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Board() throws IOException {

        Image image = ImageIO.read(new File("src/main/java/Images/ship_64.png"));
        Image cometImage = ImageIO.read(new File("src/main/java/Images/coment_64.png"));

        this.ship = new Ship(image, this, 20,25);
        this.comets = new HashSet<>();
        
        executor.scheduleAtFixedRate(()->{
            Comet comet = new Comet(cometImage, this, (int) (Math.random()*this.getWidth()), 0);
            this.comets.add(comet);
        }, 0, 800, TimeUnit.MILLISECONDS);



        this.setPreferredSize(new Dimension(1600, 1200));
        this.setBackground(new Color(3, 3, 19));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this.ship);
    }
    public void removeComet(Comet comet) {
        this.comets.remove(comet);
        System.out.println("Usunieto");
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