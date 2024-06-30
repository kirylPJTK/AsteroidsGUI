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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private int seconds = 0;
    private String timeDisplay = "00:00";
    private final Timer timer;
    private int score = 0;
    private boolean gameOver = false;
    private boolean paused = false;
    private JButton newGameButton;

    public Board() throws IOException {
        //Images for ship icon
        Image image = ImageIO.read(new File("src/main/java/Images/ship_64.png"));
        Image scaledImage = image.getScaledInstance(32, 32, Image.SCALE_SMOOTH);

        this.ship = new Ship(image, this, 20, 25);
        this.comets = Collections.synchronizedSet(new HashSet<>());
        this.lives = Collections.synchronizedList(new LinkedList<>());

        int xPosition = 10;
        int y = this.getHeight() + 900;
        int spacing = 40;

        // Life icon adding
        lives.add(new Live(scaledImage, this, xPosition, y));
        lives.add(new Live(scaledImage, this, xPosition + spacing, y));
        lives.add(new Live(scaledImage, this, xPosition + 2 * spacing, y));

        // All things for JPanel
        this.setPreferredSize(new Dimension(1600, 1000));
        this.setBackground(new Color(3, 3, 19));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    paused = !paused;
                }
            }
        });
        this.addKeyListener(this.ship);
        this.lasers = Collections.synchronizedSet(new HashSet<>());

        // Adding timer for my game
        timer = new Timer(1000, e -> {
            seconds++;
            int minutes = seconds / 60;
            int sec = seconds % 60;
            timeDisplay = String.format("%02d:%02d", minutes, sec);
            repaint();
        });
        timer.start();

        // Adding "New Game" button
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 20));
        newGameButton.setBounds(getWidth()/2-100, getHeight()/2+100,200,50);
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                restartGame();
            }
        });
    }

    // Removing life icons
    private void onShipCometColide() {

        if (!this.lives.isEmpty())
            this.lives.remove(this.lives.size() - 1);
        if (this.lives.isEmpty()) {
            gameOver = true;
            timer.stop();
            newGameButton.setVisible(true);
        }
    }

    // Romoving Comet
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

        // Check is gameOver, if so, showing all details
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("GAME OVER !!!", getWidth() / 2 - 150, getHeight() / 2 - 50);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("YOUR TIME: " + timeDisplay, getWidth() / 2 - 150, getHeight() / 2);
            g.drawString("YOUR SCORE: " + score, getWidth() / 2-150, getHeight() / 2 + 50);
            return;
        }
        // Same for paused
        if (paused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("PAUSED", this.getWidth() / 2 - 100, getHeight() / 2);
            return;
        }

        this.lasers.forEach(l -> l.paint(g));
        g.drawImage(this.ship.getImage(), this.ship.getX(), this.ship.getY(), this.ship.getWidth(), this.ship.getHeight(), this);
        this.comets.forEach(comet -> g.drawImage(comet.getImage(), comet.getX(), comet.getY(), comet.getWidth(), comet.getHeight(), this));
        lives.forEach(live -> g.drawImage(live.getImage(), live.getX(), live.getY(), live.getWidth(), live.getHeight(), this));

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(timeDisplay, getWidth() - 100, 30);

        String scoreDisoplay = "Score: " +  score;
        g.drawString(scoreDisoplay, getWidth() - 100, getHeight()-30);

    }

    @Override
    public void run() {
        if (paused) return;

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
        for (Laser laser : new LinkedList<>(this.lasers)) {
            for (Comet comet : new LinkedList<>(this.comets)){
                if (comet.isCometColidingWithLaser(laser)){
                    score++;
                    this.removeComet(comet);
                    this.removeLaser(laser);
                    break;
                }
            }
        }
    }
    private void restartGame() {

    }
}