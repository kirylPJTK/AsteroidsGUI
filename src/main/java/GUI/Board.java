package GUI;

import Models.Comet;
import Models.Laser;
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

public class Board extends JPanel implements Runnable {
    private final static Image COMET_IMAGE;

    static {
        try {
            COMET_IMAGE = ImageIO.read(new File("src/main/java/Images/coment_64.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //todo memory leak comets

    private final Map<Integer, Comet> comets;
    @Getter
    private final Map<Integer, Laser> lasers;
    private final Set<Live> lives;

    @Getter
    private final Ship ship;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Board() throws IOException {

        Image image = ImageIO.read(new File("src/main/java/Images/ship_64.png"));


        this.ship = new Ship(image, this, 20,25);
        this.comets = Collections.synchronizedMap(new HashMap<>());
        this.lives = Collections.synchronizedSet(new HashSet<>());
//        this.live = new Live(lifeIamge, this, 200, 200);
        lives.add(new Live(image, this, 10, 10));
        lives.add(new Live(image, this, 10, 10));
        lives.add(new Live(image, this, 10, 10));


//        executor.scheduleAtFixedRate(()->{
//            synchronized (this.comets) {
//                Comet comet = new Comet(cometImage, this, (int) (Math.random() * this.getWidth()), 0);
//                this.comets.add(comet);
//            }
//        }, 0, 800, TimeUnit.MILLISECONDS);



        this.setPreferredSize(new Dimension(1600, 1200));
        this.setBackground(new Color(3, 3, 19));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this.ship);
        this.lasers = Collections.synchronizedMap(new HashMap<>());
    }
    public void removeComet(Comet comet) {
        this.comets.remove(comet.getCometId());
        System.out.println("Usunieto");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        synchronized (this.lasers) {
            this.lasers.values().forEach(l -> l.paint(g));
        }
        g.drawImage(this.ship.getImage(), this.ship.getX(), this.ship.getY(), this.ship.getWidth(), this.ship.getHeight(), this);
        this.comets.values().forEach(comet -> g.drawImage(comet.getImage(), comet.getX(), comet.getY(), comet.getWidth(), comet.getHeight(), this));
        for(Live live : lives) {
            g.drawImage(live.getImage(), live.getX(), live.getY(), live.getWidth(), live.getHeight(), this);
        }


    }

    private int frameCount = 0;

    @Override
    public void run() {
        if ((this.frameCount ++ ) % 50 == 0) {
            final var comet = new Comet(COMET_IMAGE, this, (int) (Math.random() * this.getWidth()), 0);
            this.comets.put(comet.getCometId(), comet);
        }
        this.repaint();
        this.ship.run();
        this.comets.values().forEach(Comet::run);
        synchronized (this.getLasers()) {
            this.lasers.values().forEach(Laser::run);
        }





    }
}