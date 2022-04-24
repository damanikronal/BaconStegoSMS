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
import javax.microedition.lcdui.TextField;

public class phone {

    private TextField txtPhone;
    private Command cmdCancel, cmdPilihan, cmdBatal, cmdkirim;
    private Form f;

    public phone() {

        //display untuk no telp
        f = new Form("Destination");
        txtPhone = new TextField("Phone Number", "", 25, TextField.PHONENUMBER);
        cmdBatal = new Command("Cancel", Command.BACK, 0);
        cmdkirim = new Command("OK", Command.OK, 0);
        f.append(txtPhone);
        f.addCommand(cmdBatal);
        f.addCommand(cmdkirim);
    }

    public Command getCommandBatal() {
        return cmdBatal;
    }

    public Command getCommandKirim() {
        return cmdkirim;
    }

    public TextField getNoTujuan() {
        return txtPhone;
    }

    public Form getPhoneForm() {
        return f;
    }
}
