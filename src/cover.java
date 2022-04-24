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

public class cover {

    private BaconCipher bacon;
    private pesan pesan;
    private TextField txtCover;
    private Command cmdCancel, cmdKirim, cmdKirim1, cmdSimpan, cmdHapus, cmdShow;
    private Image image;
    private Form f;
    private int CoverLength;
    private String baconText,baconTexts;

    public cover() {

        f = new Form("Cover Message");
        //display untuk pesan
        cmdCancel = new Command("Cancel", Command.CANCEL, 0);
        cmdKirim = new Command("Hide", Command.OK, 0);
        cmdKirim1 = new Command("Send", Command.OK, 0);
        cmdSimpan = new Command("Save", Command.OK, 3);
        cmdHapus = new Command("Delete", Command.OK, 4);

        txtCover = new TextField("CoverText:", "", 160, TextField.ANY);
        
        f.append(txtCover);
        f.addCommand(cmdCancel);
        f.addCommand(cmdKirim);
        f.addCommand(cmdKirim1);
        f.addCommand(cmdSimpan);
        f.addCommand(cmdHapus);


    }

    public void Encode(String text1, String text2){
        bacon = new BaconCipher();
        baconTexts = bacon.encode(text1, text2);
        this.getCoverTeks().setString(baconTexts);
    }

    public String getBaconText(String text1){
        bacon = new BaconCipher();
        System.out.println(text1);
        baconText = bacon.convertLatinBacon(text1);
        return baconText;
    }

    public void getCoverLength(String text1){
        bacon = new BaconCipher();
        CoverLength = bacon.convertLatinBacon(text1).length();
        this.getCoverTeks().setLabel("CoverText (Must contain alfabet: " + CoverLength + " Char)");
    }
    
    public Command getCommandCancel() {
        return cmdCancel;
    }

    public Command getCommandKirim() {
        return cmdKirim;
    }

    public Command getCommandShow() {
        return cmdShow;
    }

    public Command getCommandKirim1() {
        return cmdKirim1;
    }

    public Command getCommandSimpan() {
        return cmdSimpan;
    }

    public Command getCommandHapus() {
        return cmdHapus;
    }

    public TextField getCoverTeks() {
        return txtCover;
    }

    public Form getCoverForm() {
        return f;
    }
}
