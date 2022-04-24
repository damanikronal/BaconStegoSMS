/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;

public class pesan {

    private TextField txtPesan;
    private Command cmdCancel, cmdKirim, cmdHapus;
    private Image image;
    private Form f;

    public pesan() {

        f = new Form("Secret Message");
        //display untuk pesan
        cmdCancel = new Command("Cancel", Command.CANCEL, 0);
        cmdKirim = new Command("Next", Command.OK, 0);
        cmdHapus = new Command("Delete", Command.OK, 4);

        txtPesan = new TextField("Secret Text", "", 160, TextField.ANY);
        
        f.append(txtPesan);
        f.addCommand(cmdCancel);
        f.addCommand(cmdKirim);
        f.addCommand(cmdHapus);


    }

    public Command getCommandCancel() {
        return cmdCancel;
    }

    public Command getCommandKirim() {
        return cmdKirim;
    }

    public Command getCommandHapus() {
        return cmdHapus;
    }

    public TextField getPesanTeks() {
        return txtPesan;
    }

    public Form getPesanForm() {
        return f;
    }
}

