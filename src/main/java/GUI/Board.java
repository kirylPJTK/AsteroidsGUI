package GUI;

import Models.Comet;
import Models.Live;
import Models.Ship;
import lombok.Getter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Board extends JPanel implements Runnable{
    //todo memory leak comets
    private final Set<Comet> comets;
    private final Set<Live> lives;
    @Getter
    private final Ship ship;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Board() throws IOException {
        Image lifeIamge = ImageIO.read(new File("src/main/java/Images/ship_64.png"));
        Image image = ImageIO.read(new File("src/main/java/Images/ship_64.png"));
        Image cometImage = ImageIO.read(new File("src/main/java/Images/coment_64.png"));

        this.ship = new Ship(image, this, 20,25);
        this.comets = Collections.synchronizedSet(new HashSet<>());

        this.lives = Collections.synchronizedSet(new HashSet<>());
//        this.live = new Live(lifeIamge, this, 200, 200);
        lives.add(new Live(lifeIamge, this, 10, 10));
        lives.add(new Live(lifeIamge, this, 10, 10));
        lives.add(new Live(lifeIamge, this, 10, 10));

        executor.scheduleAtFixedRate(()->{
            Comet comet = new Comet(cometImage, this, (int) (Math.random()*this.getWidth()), 0);
            try {
                this.comets.add(comet);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        this.ship.paintAllLasers(g);

        g.drawImage(this.ship.getImage(), this.ship.getX(), this.ship.getY(), this.ship.getWidth(), this.ship.getHeight(), this);

        for(Comet comet : comets) {
            g.drawImage(comet.getImage(), comet.getX(), comet.getY(), comet.getWidth(), comet.getHeight(), this);
        }

        for(Live live : lives) {
            g.drawImage(live.getImage(), live.getX(), live.getY(), live.getWidth(), live.getHeight(), this);
        }

        // g.drawImage(this.live.getImage(), this.live.getX(), this.live.getY(), this.live.getWidth(), this.live.getHeight(), this);
    }

    @Override
    public void run() {
        this.repaint();
        this.ship.run();
        this.comets.forEach(Comet::run);
    }
}