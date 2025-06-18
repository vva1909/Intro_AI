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
        System.out.println(mouseX + " " + mouseY);
        if (mouseX >= 200 && mouseX <= 290 && mouseY >= 910 && mouseY <= 950) {
            gp.initGame();
            gp.inGame = true;
        }

        if (mouseX >= 610 && mouseX <= 700 && mouseY >= 910 && mouseY <= 950) {
            gp.hint_show = !gp.hint_show;
            gp.player.FindSolution(0);
        }

        if (mouseX >= 720 && mouseX <= 830 && mouseY >= 910 && mouseY <= 950) {
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
