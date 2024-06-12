import javax.swing.*;

import GUI.Board;
import GUI.MyJFrame;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MyJFrame(new Board());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
