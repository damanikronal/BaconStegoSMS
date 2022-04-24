/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
/**
 *
 * @author Ronal Damanik
 */
public class help {

    private Command cmdCancel;
    private Form f;
    private String help = "BaconStegoSMS \n"+
                "1. How to use \n "+
                "   > Input message you want to hide \n "+
                "   > Input cover text as a place  \n "+
                "     for hiding message \n "+
                "   > Hide : for hiding message to cover  \n "+
                "     Show : for reveal message from cover \n "+
                "     "+
                "2. About \n "+
                "   > BaconStegoSMS \n "+
                "     Bacon Steganography SMS \n"+
                "   > Developer : Ronal Damanik \n "+
                "     Web site  : kroseva.wordpress.com \n "+
                "   > Thanks to : \n "+
                "     - Google for resource of Bacon Cipher \n "+
                "     - Oracle or Sun for Java SDK \n "+
                "     - Netbeans for IDE Netbeans 6.9" +
                "     - All of you, who use this application :) ";

    public help(){
        f = new Form("Help");
        cmdCancel = new Command("Cancel", Command.BACK, 0);
        f.append(help);
        f.addCommand(cmdCancel);
    }

    public Command getCommandBatal() {
        return cmdCancel;
    }

    public Form getHelpForm() {
        return f;
    }

}
