import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    final int B_WIDTH = 900;
    final int B_HEIGHT = 900;
    final int DOT_SIZE = 45;
    int gold_count = 0, weight_count = 0;
    int DELAY = 150;
    boolean inGame = false, hint_show = false, play_again = false;
    Timer timer;
    String game_result;
    Image player_image, dust_image, brick_image, fog_image, treasure_image;
    Player player;
    Map map;
    AgentThread agentThread;

    public GamePanel(){
        addKeyListener(new GetKey(this));
        addMouseListener(new GetMouse(this));
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT + 50));
        loadImages();
        initGame();
        
        // Khởi tạo và chạy agent thread
        agentThread = new AgentThread(this);
        agentThread.start();
    }

    private void loadImages() {
        ImageIcon ii = new ImageIcon("dust.png");
        dust_image = ii.getImage();

        ii = new ImageIcon("brick.png");
        brick_image = ii.getImage();

        ii = new ImageIcon("player.png");
        player_image = ii.getImage();

        ii = new ImageIcon("fog.png");
        fog_image = ii.getImage();

        ii = new ImageIcon("treasure.png");
        treasure_image = ii.getImage();
    }

    public void initGame() {
        gold_count = 0;
        inGame = true;
        hint_show = false;
        map = new Map(this);
        if (!play_again)
            player = new Player(this);
        else {
            player.x = DOT_SIZE;
            player.y = DOT_SIZE;
            for (int i = 0; i < player.n; i++) {
                for (int j = 0; j < player.n; j++) {
                    player.fog[i][j] = 1;
                }
            }
        }
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Font small = new Font("Helvetica", Font.BOLD, 13);

        g.setColor(Color.black);
        g.setFont(small);
        for (int i = 0; i < player.n; i++) {
            for (int j = 0; j < player.n; j++) {
                if (player.fog[i][j] == 1 && inGame) {
                    g.drawImage(fog_image, i * DOT_SIZE, j * DOT_SIZE, DOT_SIZE, DOT_SIZE, this);
                } else {
                    Image ii = (map.maze[i][j] == 0) ? brick_image : dust_image;
                    g.drawImage(ii, i * DOT_SIZE, j * DOT_SIZE, DOT_SIZE, DOT_SIZE, this);
                }
            }
        }
        g.drawImage(treasure_image, map.treasure_x, map.treasure_y, DOT_SIZE, DOT_SIZE, this);
        g.drawImage(player_image, player.x, player.y, DOT_SIZE, DOT_SIZE, this);
        Toolkit.getDefaultToolkit().sync();

        if (!inGame){
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 70));
            g.drawString(game_result, getXTextCenter(game_result, g), getYTextCenter(game_result, g));
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();

        // Vẽ nút New Game
        String play = "New Game";
        int playX = getXTextCenter(play, g) / 2;
        int playY = B_HEIGHT + 30;
        int buttonWidth = 80;
        int buttonHeight = 30;
        
        // Vẽ nền nút New Game
        GradientPaint gradient1 = new GradientPaint(
            playX - 5, playY - 15,
            new Color(41, 128, 185),
            playX + buttonWidth - 5, playY + 15,
            new Color(52, 152, 219)
        );
        g2d.setPaint(gradient1);
        g2d.fillRoundRect(playX - 5, playY - 15, buttonWidth, buttonHeight, 15, 15);
        
        // Vẽ viền nút New Game
        g2d.setColor(new Color(41, 128, 185));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(playX - 5, playY - 15, buttonWidth, buttonHeight, 15, 15);
        
        // Vẽ text New Game - căn giữa
        g2d.setColor(Color.WHITE);
        int textX = playX - 5 + (buttonWidth - metrics.stringWidth(play)) / 2;
        int textY = playY - 15 + (buttonHeight + metrics.getAscent()) / 2;
        g2d.drawString(play, textX, textY);

        // Vẽ nút Agent player
        String show_hint = "Agent player";
        int hintX = getXTextCenter(play, g) * 3 / 2;
        int hintY = B_HEIGHT + 30;
        int buttonWidth2 = 90;
        
        // Vẽ nền nút Agent player
        GradientPaint gradient2 = new GradientPaint(
            hintX - 5, hintY - 15,
            new Color(46, 204, 113),
            hintX + buttonWidth2 - 5, hintY + 15,
            new Color(39, 174, 96)
        );
        g2d.setPaint(gradient2);
        g2d.fillRoundRect(hintX - 5, hintY - 15, buttonWidth2, buttonHeight, 15, 15);
        
        // Vẽ viền nút Agent player
        g2d.setColor(new Color(46, 204, 113));
        g2d.drawRoundRect(hintX - 5, hintY - 15, buttonWidth2, buttonHeight, 15, 15);
        
        // Vẽ text Agent player - căn giữa
        g2d.setColor(Color.WHITE);
        int textX2 = hintX - 5 + (buttonWidth2 - metrics.stringWidth(show_hint)) / 2;
        int textY2 = hintY - 15 + (buttonHeight + metrics.getAscent()) / 2;
        g2d.drawString(show_hint, textX2, textY2);

        // Vẽ nút Agent player 2
        String show_hint2 = "Agent player 2";
        int hintX2 = hintX + buttonWidth2 + 20;
        int hintY2 = B_HEIGHT + 30;
        int buttonWidth3 = 100;
        
        // Vẽ nền nút Agent player 2
        GradientPaint gradient3 = new GradientPaint(
            hintX2 - 5, hintY2 - 15,
            new Color(52, 152, 219),
            hintX2 + buttonWidth3 - 5, hintY2 + 15,
            new Color(41, 128, 185)
        );
        g2d.setPaint(gradient3);
        g2d.fillRoundRect(hintX2 - 5, hintY2 - 15, buttonWidth3, buttonHeight, 15, 15);
        
        // Vẽ viền nút Agent player 2
        g2d.setColor(new Color(52, 152, 219));
        g2d.drawRoundRect(hintX2 - 5, hintY2 - 15, buttonWidth3, buttonHeight, 15, 15);
        
        // Vẽ text Agent player 2 - căn giữa
        g2d.setColor(Color.WHITE);
        int textX3 = hintX2 - 5 + (buttonWidth3 - metrics.stringWidth(show_hint2)) / 2;
        int textY3 = hintY2 - 15 + (buttonHeight + metrics.getAscent()) / 2;
        g2d.drawString(show_hint2, textX3, textY3);
    }
    public int  getXTextCenter(String text, Graphics g)
    {
        FontMetrics metrics = g.getFontMetrics();
        int x = (B_WIDTH - metrics.stringWidth(text)) / 2;
        return x;
    }
    
    public int getYTextCenter(String text, Graphics g)
    {
        FontMetrics metrics = g.getFontMetrics();
        int y = (B_HEIGHT - metrics.getHeight()) / 2;
        return y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            player.checkCollision();
        }
        repaint();
    }
}
