import GUI.Board;
import GUI.MyJFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame frame = new MyJFrame(new Board());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");

        fileMenu.add(newGameItem);
        fileMenu.add(loadGameItem);
        fileMenu.add(saveGameItem);

        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);

        newGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic for starting a new game
                // For instance, reset the game state
                ((MyJFrame) frame).resetGame();
            }
        });

        loadGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic for loading a game
                // For instance, read game state from a file
                ((MyJFrame) frame).loadGame();
            }
        });

        saveGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic for saving the current game
                // For instance, write game state to a file
                ((MyJFrame) frame).saveGame();
            }
        });
    }
}
