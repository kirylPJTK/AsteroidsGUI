package GUI;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyJFrame extends JFrame {
    //todo zmienić nazwę
    private final Board board;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    public MyJFrame(Board board) {
        this.board = board;
        this.add(board);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
        this.setResizable(false);
        this.executor.scheduleAtFixedRate(this.board, 0, 16, TimeUnit.MILLISECONDS);
    }


//    private void nextFrame() {
//
//        var el =
//
//
//
//
//    }







}
