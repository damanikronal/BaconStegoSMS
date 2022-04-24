/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Font;

public class splash extends Canvas {

    private Image img;
    private Command cmdExit;

    public splash() {
        cmdExit = new Command("Exit", Command.EXIT, 1);
        addCommand(cmdExit);

    }

    protected void paint(Graphics g) {
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0, 0, 255);
        g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
        g.drawString("Bacon Steganography SMS", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.TOP);

    }

    public Command getCommandExit() {
        return cmdExit;
    }
}
