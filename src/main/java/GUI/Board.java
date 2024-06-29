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


    private final Set<Comet> comets;
    @Getter
    private final Set<Laser> lasers;
    private final List<Live> lives;


    @Getter
    private final Ship ship;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public Board() throws IOException {
        Image image = ImageIO.read(new File("src/main/java/Images/ship_64.png"));
        Image scaledImage = image.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        this.ship = new Ship(image, this, 20, 25);
        this.comets = Collections.synchronizedSet(new HashSet<>());
        this.lives = Collections.synchronizedList(new LinkedList<>());

        int xPosition = 10;
        int y = this.getHeight() + 800;
        int spacing = 40;

        lives.add(new Live(scaledImage, this, xPosition, y));
        lives.add(new Live(scaledImage, this, xPosition + spacing, y));
        lives.add(new Live(scaledImage, this, xPosition + 2 * spacing, y));




        this.setPreferredSize(new Dimension(1600, 1000));
        this.setBackground(new Color(3, 3, 19));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this.ship);
        this.lasers = Collections.synchronizedSet(new HashSet<>());

    }

    private void onShipCometColide() {
        if (this.lives.isEmpty()) {
            //todo?
            //todo co się ma dziać jak nie ma już żyć?
            return;
        }
        this.lives.removeLast();
    }

    public void removeComet(Comet comet) {
        this.comets.remove(comet);
    }

    public void removeLaser(Laser laser) {
        this.lasers.remove(laser);
    }
    public void addComet(){
        this.comets.add(new Comet(COMET_IMAGE, this, (int) (Math.random() * this.getWidth()), 0));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.lasers.forEach(l -> l.paint(g));
        g.drawImage(this.ship.getImage(), this.ship.getX(), this.ship.getY(), this.ship.getWidth(), this.ship.getHeight(), this);
        this.comets.forEach(comet -> g.drawImage(comet.getImage(), comet.getX(), comet.getY(), comet.getWidth(), comet.getHeight(), this));
        lives.forEach(live -> g.drawImage(live.getImage(), live.getX(), live.getY(), live.getWidth(), live.getHeight(), this));
    }

    @Override
    public void run() {
        this.repaint();
        this.ship.run();
        final var comets = new LinkedList<>(this.comets);
        comets.forEach(Comet::run);
        new LinkedList<>(this.lasers).forEach(Laser::run);

        comets.forEach(c -> {
            if (c.isCometColidingWithSip(this.ship)) {
                this.onShipCometColide();
                this.removeComet(c);
            }
        });
    }
}