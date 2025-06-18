import javax.swing.*;
import java.awt.*;

public class Play extends JFrame {
    public Play() {
        add(new GamePanel());
        setResizable(false);
        pack();
        setTitle("Tim Duong Me Cung");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Play();
            ex.setVisible(true);
        });
    }
}