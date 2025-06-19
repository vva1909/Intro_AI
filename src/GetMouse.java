import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GetMouse implements MouseListener {
    GamePanel gp;
    public static int mouseX, mouseY;
    public GetMouse(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (mouseX >= 150 && mouseX <= 240 && mouseY >= 710 && mouseY <= 750) {
            gp.initGame();
            gp.inGame = true;
        }

        if (mouseX >= 460 && mouseX <= 560 && mouseY >= 710 && mouseY <= 750) {
            gp.hint_show = !gp.hint_show;
            gp.player.FindSolution(0);
        }

        if (mouseX >= 570 && mouseX <= 680 && mouseY >= 710 && mouseY <= 750) {
            gp.hint_show = !gp.hint_show;
            gp.player.FindSolution(1);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
