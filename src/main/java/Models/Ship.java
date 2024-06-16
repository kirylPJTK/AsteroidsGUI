package Models;

import GUI.Board;
import lombok.Data;
import lombok.NonNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Data
public class Ship implements KeyListener, Runnable{
    private final int width = 50;
    private final int height = 80;
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
            case 'd' -> {
                if (this.x + this.height >= this.board.getWidth()) {
                    this.x = this.board.getWidth() - this.height;
                } else {
                    this.x += 10;
                }
            }
            case 'a' -> {
                if (this.x - 10 <= 0) {
                    this.x = 0;
                } else {
                    this.x -= 10;
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        this.velocity *= 0.99;

        this.x += this.velocity;

    }
}
