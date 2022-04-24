/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
import java.io.IOException;
import javax.microedition.lcdui.*;

public class menu {

    private List lst;
    private Image image;
    private String str;
    private int nilai = 0;
    private List menubaca;
    private List menubacad;

    public menu() {
        menuku();
    }

    public void menuku() {


        try {
            image = Image.createImage("/img/pesan.png");
            lst = new List("Main Menu", List.IMPLICIT);
            lst.append("New Message", image);
            image = Image.createImage("/img/inbox.png");
            lst.append("Inbox", image);
            image = Image.createImage("/img/outbox.png");
            lst.append("Outbox", image);
            image = Image.createImage("/img/draf.png");
            lst.append("Draf", image);
            image = Image.createImage("/img/about.png");
            lst.append("Help", image);
            image = Image.createImage("/img/keluar.png");
            lst.append("Exit", image);
            Ticker ticker = new Ticker("Bacon Steganography SMS");
            lst.setTicker(ticker);
        } catch (IOException ex) {
            System.out.println("error menu\n");
            ex.printStackTrace();
        }
    }

    public List getMenuPageList() {
        return lst;
    }

    public String getSelectedMenu() {
        System.out.println(lst.getSelectedIndex());
        return lst.getString(lst.getSelectedIndex());
    }
}

