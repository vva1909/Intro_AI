public class AgentThread extends Thread {
    private GamePanel gamePanel;
    private boolean running;
    private static final long DELAY = 200; //

    public AgentThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            if (gamePanel.inGame && gamePanel.hint_show) {
                // Thực hiện di chuyển agent ở đây
                gamePanel.player.moveAgent();
            }
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    public void stopThread() {
        running = false;
    }
} 