/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
import javax.microedition.lcdui.*;

public class cp {


    private Form form;
    private StringItem si;
    private Command cmdOK;
    private Command cmdCancel;

    public cp() {
        form = new Form("Confirmation");
        si = new StringItem("","Yakin Keluar ?",Item.PLAIN);
        cmdOK = new Command("OK",Command.OK,1);
        cmdCancel = new Command("Cancel",Command.CANCEL,1);
        form.append(si);
        form.addCommand(cmdOK);
        form.addCommand(cmdCancel);
    }

    public Form getConfirmationPageForm() {
        return form;
    }

    public Command getCommandCancel() {
        return cmdCancel;
    }

    public Command getCommandOK() {
        return cmdOK;
    }
}
