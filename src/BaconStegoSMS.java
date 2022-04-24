/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Ronal Damanik
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.*;
import javax.microedition.io.Connector;
import javax.microedition.io.PushRegistry;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.wireless.messaging.Message;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessageListener;
import javax.wireless.messaging.TextMessage;

public class BaconStegoSMS extends MIDlet implements CommandListener,
        Runnable, MessageListener {

    private splash wp;
    private Display display;
    //memasukan file
    private cp cp; // Kelas baru dibuat
    private menu menu;
    private help help;
    private Draf draf;
    private Timer tmr;
    private pesan pesan;
    private phone phone;
    private cover cover;
    private BaconCipher bacon;
    private VerifyText verify;
    private smsout smsout;
    private ItemTerkirim outbox;
    private smsin inbox;
    private TugasTimerKu tmrTask; // Kelas baru dibuat
    private Alert alert, newSms = new Alert(null, null, null, AlertType.ALARM);
    private Thread threadSms;
    private MessageConnection conn;
    private String wpesan, senderAddress, str, isiPesan, noPengirim, id, portSms = "2009", pjgPesan;
    private Date waktupesan;
    private int rmsId = 1;
    private Message msg;
    private Image image;
    private Command bacaAlertCommand = new Command("OK", Command.OK, 0);
    private Command keluarAlertCommand = new Command("Cancel", Command.CANCEL, 0);
    private RecordStore recData, recStore;
    private Form fr, fr1;
    private TextField strPesan, strSender, strWaktu, strMsg;
    private Command cmdHapus, cmdBalas, cmdClose, cmd, cmdShowText, cmdForward;
    private List recList;
    String[] connections;
    private String notj;

    public void startApp() {
        startRechiver();
        load();
    }

    public void load() {
        if (display == null) {
            wp = new splash();
            cp = new cp();
            pesan = new pesan();
            cover = new cover();
            bacon = new BaconCipher();
            verify = new VerifyText();
            phone = new phone();
            smsout = new smsout();
            tmr = new Timer();
            menu = new menu();
            outbox = new ItemTerkirim();
            inbox = new smsin();
            draf = new Draf();
            help = new help();
            tmrTask = new TugasTimerKu();

            tmr.schedule(tmrTask, 1100);
            display = Display.getDisplay(this);
            wp.setCommandListener(this);
            menu.getMenuPageList().setCommandListener(this);
            cp.getConfirmationPageForm().setCommandListener(this);
            pesan.getPesanForm().setCommandListener(this);
            phone.getPhoneForm().setCommandListener(this);
            outbox.getListOutboxPageList().setCommandListener(this);
            inbox.getListInboxPageList().setCommandListener(this);
            draf.getListDrafPageList().setCommandListener(this);
        }
        display.setCurrent(wp);
    }

    public void pauseApp() {
        threadSms = null;
    }

    public void destroyApp(boolean unconditional) {
        threadSms = null;
    }

    protected void Quit() {
        destroyApp(true);
        notifyDestroyed();
    }

    public void pecahRecordInbox(String strinbox) {
        int idx1 = strinbox.indexOf("~");
        int idx2 = strinbox.indexOf("~", idx1 + 1);
        int idx3 = strinbox.indexOf("~", idx2 + 1);
        int idx4 = strinbox.indexOf("~", idx3 + 1);

        noPengirim = strinbox.substring(0, idx1);
        isiPesan = strinbox.substring(idx1 + 1, idx2);
        wpesan = strinbox.substring(idx2 + 1, idx3);
    }

    public void pecahRecordOutBoxDraf(String strout) {
        int idx1 = strout.indexOf("~");
        int idx2 = strout.indexOf("~", idx1 + 1);
        int idx3 = strout.indexOf("~", idx2 + 1);

        noPengirim = strout.substring(0, idx1);
        isiPesan = strout.substring(idx1 + 1, idx2);
        wpesan = "";
    }

    private void startRechiver() {
        String addr = "sms://:" + portSms;
        threadSms = null;
        try {
            if (conn == null) {
                conn = (MessageConnection) Connector.open(addr);
                conn.setMessageListener(this);
            }
        } catch (Exception ex) {
            System.out.println("kesalahan buka port" + ex);
        }
        connections = PushRegistry.listConnections(true);
        threadSms = new Thread(this);
        threadSms.start();
    }

    public void notipyIncomingMessage(MessageConnection messageConn) {
        if (threadSms == null) {
            threadSms = new Thread();
            threadSms.start();
        }
    }

    private static String fixNom(String nom) {
        int dowonya = nom.length();
        String nomFix = nom.substring(6, dowonya);
        return nomFix;
    }

    public void tutupKoneksi() {
        try {
            conn.close();
        } catch (IOException e) {
        }
    }

    private void SimpanSMSin(String noSender, String isiSMs, Date waktu) {
        try {
            recData = RecordStore.openRecordStore("inbox", true);
            String honey = noSender.trim() + "~"
                    + isiSMs.trim() + "~" + waktu + "~" + "0";
            addRecordElement(recData, honey);
        } catch (Exception e) {
            System.out.println("ddd :" + e.toString());
        }
    }

    private void addRecordElement(RecordStore rs, String valueRecord) {
        try {
            rs = RecordStore.openRecordStore(rs.getName().toString(), true);
            // Ini adalah String yang akan kita masukkan kedalam record
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dOut = new DataOutputStream(out);
            // Menyimpan sebuah integer
            dOut.writeInt(rs.getNextRecordID() * rs.getNextRecordID());
            // Menyimpan sebuah string
            valueRecord += "~" + rs.getNextRecordID();
            dOut.writeUTF(valueRecord);
            byte[] bytes = out.toByteArray();
            // Menuliskan Record pada Store
            rs.addRecord(bytes, 0, bytes.length);
            dOut.close();
            out.close();
            rs.closeRecordStore();
        } catch (Exception err) {
            System.out.println("here is " + err.toString());
        }
    }

    public void updaterecordSmsout(String rms) {
        String isiPesana = null;
        String ida = null;
        String waktuku = null;
        try {
            recStore = RecordStore.openRecordStore(rms, true);
            // Mengambil isi dari record store
            RecordEnumeration enumerator = recStore.enumerateRecords(null, null, false);
            while (enumerator.hasNextElement()) {
                byte[] recBytes = enumerator.nextRecord();
                ByteArrayInputStream in = new ByteArrayInputStream(recBytes);
                DataInputStream dIn = new DataInputStream(in);
                int count = dIn.readInt();
                String item = dIn.readUTF();

                int idx1 = item.indexOf("~");
                int idx2 = item.indexOf("~", idx1 + 1);
                int idx3 = item.indexOf("~", idx2 + 1);

                isiPesana = item.substring(idx1 + 1, idx2);
                //waktuku=item.substring(idx2+1,idx3);
                ida = item.substring(idx3 + 1);
                if (isiPesana.equals(isiPesan)) {
                    isiPesan = isiPesana;
                    this.id = ida;
                    break;
                }
                dIn.close();
                in.close();
            }
            recStore.closeRecordStore();
        } catch (Exception e) {
            System.out.println("error in here dear : " + e.toString());
        }
        int idh = Integer.parseInt(ida);
        try {
            /*RecordStore.deleteRecordStore("RecordMember");*/
            recData = RecordStore.openRecordStore(rms, true);
            String honey = noPengirim + "~" + isiPesan + "~" + "1" + "~" + idh;
            // Ini adalah String yang akan kita masukkan kedalam record
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dOut = new DataOutputStream(out);
            // Menyimpan sebuah integer
            dOut.writeInt(idh * idh);
            // Menyimpan sebuah string
            dOut.writeUTF(honey);
            byte[] bytes = out.toByteArray();
            recData.setRecord(idh, bytes, 0, bytes.length);
            dOut.close();
            out.close();
            recData.closeRecordStore();
        } catch (Exception e) {
            System.out.println("error hehe" + e.toString());
        }
    }

    public void updaterecordSmsin() {
        String isiPesana = null;
        String ida = null;
        String waktuku = null;
        try {
            recStore = RecordStore.openRecordStore("inbox", true);
            // Mengambil isi dari record store
            RecordEnumeration enumerator = recStore.enumerateRecords(null, null, false);
            while (enumerator.hasNextElement()) {

                byte[] recBytes = enumerator.nextRecord();
                ByteArrayInputStream in = new ByteArrayInputStream(recBytes);
                DataInputStream dIn = new DataInputStream(in);
                int count = dIn.readInt();
                String item = dIn.readUTF();

                int idx1 = item.indexOf("~");
                int idx2 = item.indexOf("~", idx1 + 1);
                int idx3 = item.indexOf("~", idx2 + 1);
                int idx4 = item.indexOf("~", idx3 + 1);

                isiPesana = item.substring(idx1 + 1, idx2);
                waktuku = item.substring(idx2 + 1, idx3);
                ida = item.substring(idx4 + 1);
                if (isiPesana.equals(isiPesan)) {
                    isiPesan = isiPesana;
                    this.id = ida;
                    break;
                }
                dIn.close();
                in.close();
            }
            recStore.closeRecordStore();
        } catch (Exception e) {
            System.out.println("error in here dear : " + e.toString());
        }
        int idh = Integer.parseInt(ida);
        try {
            /*RecordStore.deleteRecordStore("RecordMember");*/
            recData = RecordStore.openRecordStore("inbox", true);
            String honey = noPengirim + "~" + isiPesan + "~" + waktuku + "~" + "1" + "~" + idh;

            // Ini adalah String yang akan kita masukkan kedalam record
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dOut = new DataOutputStream(out);
            // Menyimpan sebuah integer
            dOut.writeInt(idh * idh);
            // Menyimpan sebuah string
            dOut.writeUTF(honey);
            byte[] bytes = out.toByteArray();
            recData.setRecord(idh, bytes, 0, bytes.length);

            dOut.close();
            out.close();
            recData.closeRecordStore();
        } catch (Exception e) {
            System.out.println("error hehe" + e.toString());
        }
    }

    public void bacaSMS() {
        fr = new Form("Read Message");
        cmdBalas = new Command("Reply", Command.OK, 0);
        cmdClose = new Command("Cancel", Command.BACK, 0);
        cmdShowText = new Command("Show", Command.OK, 0);
        strPesan = new TextField("Message:", isiPesan, 160, TextField.ANY);
        strSender = new TextField("Sender:", noPengirim, 160, TextField.PHONENUMBER);
        if (waktupesan == null) {
            wpesan = wpesan;
        } else {
            wpesan = waktupesan.toString();
        }
        strWaktu = new TextField("Date/Time:", wpesan, 150, TextField.ANY);
        fr.append(strPesan);
        fr.append(strSender);
        fr.append(strWaktu);
        fr.addCommand(cmdClose);
        fr.addCommand(cmdBalas);
        fr.addCommand(cmdShowText);
    }

    public void forwardSMS() {
        fr1 = new Form("Forward Message");
        cmdForward = new Command("Forward", Command.OK, 0);
        cmdClose = new Command("Cancel", Command.BACK, 0);
        cmdShowText = new Command("Show", Command.OK, 0);
        strPesan = new TextField("Message:", cover.getCoverTeks().getString(), 160, TextField.ANY);
        //strMsg = new TextField("Secret Text:", "", 160, TextField.UNEDITABLE);
        strSender = new TextField("Destination:", "", 160, TextField.PHONENUMBER);
        if (waktupesan == null) {
            wpesan = wpesan;
        } else {
            wpesan = waktupesan.toString();
        }
        //strWaktu = new TextField("Waktu:", wpesan, 150, TextField.ANY);
        fr1.append(strPesan);
        //fr.append(strMsg);
        fr1.append(strSender);
        //fr.append(strWaktu);
        fr1.addCommand(cmdClose);
        fr1.addCommand(cmdForward);
        fr1.addCommand(cmdShowText);
    }

    public void hideText() {
    }

    public void run() {
        try {
            //msg = (BinaryMessage) conn.receive();
            msg = conn.receive();
            boolean dibaca = false;
            if (msg != null) {
                senderAddress = fixNom(msg.getAddress());

                int idx5 = senderAddress.indexOf(":");
                System.out.println("non:" + idx5);
                if (idx5 > 0) {
                    notj = senderAddress.substring(0, idx5);
                } else {
                    notj = senderAddress;
                }
                Date waktu = msg.getTimestamp();
                if (msg instanceof TextMessage) {
                    noPengirim = notj;
                    isiPesan = ((TextMessage) msg).getPayloadText();
                    waktupesan = waktu;
                    SimpanSMSin(noPengirim, isiPesan, waktupesan);
                    alertSmsIn();
                }
            }
            //      tutupKoneksi();
            startRechiver();
        } catch (IOException e) {
        }
    }

    public void alertSmsIn() {
        try {
            image = Image.createImage("/img/inbox.png");
            newSms.setTitle("Received SMS");
            newSms.setString("Received SMS : "
                    + "\nSMS Size : " + isiPesan.toString().length());
            newSms.setImage(image);
            newSms.addCommand(bacaAlertCommand);
            newSms.addCommand(keluarAlertCommand);
            newSms.setCommandListener(this);
            display = Display.getDisplay(this);
            display.vibrate(1000);
            display.setCurrent(newSms);
        } catch (IOException e) {
        }
    }

    public void notifyIncomingMessage(MessageConnection arg0) {
        if (threadSms == null) {
            threadSms = new Thread(this);
            threadSms.start();

        }
    }

    class TugasTimerKu extends TimerTask {

        public void run() {
            display.setCurrent(menu.getMenuPageList());
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == wp.getCommandExit()) {
            try {
                this.display.setCurrent(cp.getConfirmationPageForm());
                tmr.cancel();
            } catch (Exception e) {
                System.out.println("Error here : On Timer Cancel - " + e.toString());
            }
        } else if (c == keluarAlertCommand) {
            this.display.setCurrent(menu.getMenuPageList());
        } else if (c == bacaAlertCommand) {
            bacaSMS();
            fr.setCommandListener(this);
            this.display.setCurrent(fr);
            updaterecordSmsin();
        } else if (c == cmdBalas) {
            pesan = new pesan();
            pesan.getPesanForm().setCommandListener(this);
            this.display.setCurrent(pesan.getPesanForm());
        } else if (c == cmdForward) {
            if (strSender.getString().length() == 0){
                Alert verifi = new Alert ("Verification!",
                        "Phone number is empty!"
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, fr1);
            } else {
                smsout = new smsout();
                smsout.smsout(strSender.getString(), strPesan.getString());
                menu = new menu();
                menu.getMenuPageList().setCommandListener(this);
                startRechiver();
                this.display.setCurrent(smsout.getAlertKirim(), menu.getMenuPageList());
            }
        } else if (c == draf.getCommandCancelD()) {
            this.display.setCurrent(menu.getMenuPageList());
        } else if (c == draf.getCommandBacaD()) {
            pecahRecordOutBoxDraf(draf.getListDrafPageList2Selected());
            updaterecordSmsout("draf");
            forwardSMS();
            fr1.setCommandListener(this);
            this.display.setCurrent(fr1);
            this.rmsId = 3;
        } else if (c == draf.getCommandHapusD()) {
            draf.Hapus(draf.getListDrafPageList2Selected());
            draf.TampilDraf();
            this.display.setCurrent(draf.getListDrafPageList());
        } else if (c == this.cmd) {
            notifyDestroyed();
            this.display.setCurrent(menu.getMenuPageList());
        } else if (c == cmdClose) {
            if (this.rmsId == 1) {
                inbox.Tampil();
                this.display.setCurrent(inbox.getListInboxPageList());
            } else if (this.rmsId == 2) {
                outbox.Tampil();
                this.display.setCurrent(outbox.getListOutboxPageList());
            } else if (this.rmsId == 3) {
                draf.TampilDraf();
                this.display.setCurrent(draf.getListDrafPageList());
            }

        } else if (c == inbox.getCommandCancel()) {
            this.display.setCurrent(menu.getMenuPageList());
        } else if (c == inbox.getCommandHapus()) {
            inbox.Hapus(inbox.getListInboxPageList2Selected());
            inbox.Tampil();
            this.display.setCurrent(inbox.getListInboxPageList());
        } else if (c == inbox.getCommandBaca()) {
            pecahRecordInbox(inbox.getListInboxPageList2Selected());
            updaterecordSmsin();
            bacaSMS();
            fr.setCommandListener(this);
            this.display.setCurrent(fr);
            this.rmsId = 1;
        } else if (c == outbox.getCommandBaca()) {
            pecahRecordOutBoxDraf(outbox.getListAnggotaPageList2Selected());
            updaterecordSmsout("outbox");
            bacaSMS();
            fr.setCommandListener(this);
            this.display.setCurrent(fr);
            this.rmsId = 2;
        } else if (c == outbox.getCommandCancel()) {
            menu = new menu();
            menu.getMenuPageList().setCommandListener(this);
            this.display.setCurrent(menu.getMenuPageList());
        } else if (c == outbox.getCommandHapus()) {
            outbox.Hapus(outbox.getListAnggotaPageList2Selected());
            outbox.Tampil();
            this.display.setCurrent(outbox.getListOutboxPageList());
        } else if (c == pesan.getCommandCancel()) {
            this.display.setCurrent(menu.getMenuPageList());
        } else if (c == pesan.getCommandKirim()) {
            // VERIFIKASI AGAR INPUT PESAN TEKS HANYA ALFABET DAN SPASI
            if (pesan.getPesanTeks().getString().length() == 0){
                Alert verifi = new Alert ("Verification!",
                        "Secret message must be filled!"
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, pesan.getPesanForm());
            }
            else if(verify.countNonAlfabet(pesan.getPesanTeks().getString()))
            {
                cover = new cover();
                cover.getCoverLength(pesan.getPesanTeks().getString());
                System.out.println(bacon.convertLatinBacon(pesan.getPesanTeks().getString()));
                cover.getCoverForm().setCommandListener(this);
                this.display.setCurrent(cover.getCoverForm());
            } else {
                Alert verifi = new Alert ("Verification!",
                        "Secret message must be alphabet character (a..z or A..Z) with" +
                        " or without space."
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, pesan.getPesanForm());
            }
        } else if (c == cover.getCommandSimpan()){
            if (cover.getCoverTeks().getString().length() == 0){
                Alert verifi = new Alert ("Verification!",
                        "Cover message is empty!"
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, cover.getCoverForm());
            } else{
                smsout = new smsout();
                smsout.SimpantoDraf("00", cover.getCoverTeks().getString());
                this.display.setCurrent(menu.getMenuPageList());
            }
        } else if (c == cover.getCommandKirim()) {
            // VERIFIKASI AGAR PANJANG COVER = BACON
            int cipheredTextLength = cover.getBaconText(pesan.getPesanTeks().getString()).length();
            int textToShowRealLength = verify.countAlfabet(cover.getCoverTeks().getString());
            System.out.println(cipheredTextLength);
            System.out.println(textToShowRealLength);
            int lengthDiff = cipheredTextLength - textToShowRealLength;
            if (lengthDiff > 0) {
                Alert verifi = new Alert ("Verification!",
                        "Cover text is shorter than Bacon text!"
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, cover.getCoverForm());
            } else if (lengthDiff < 0) {
                Alert verifi = new Alert ("Verification!",
                        "Cover text is longer than Bacon text!"
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, cover.getCoverForm());
            } else if (lengthDiff == 0) {
                cover.Encode(cover.getBaconText(pesan.getPesanTeks().getString()),
                cover.getCoverTeks().getString());
                cover.getCoverForm().setCommandListener(this);
                this.display.setCurrent(cover.getCoverForm());
            }
        } else if (c == cover.getCommandKirim1()) {
            // VERIFIKASI SEBELUM KIRIM
            if (cover.getCoverTeks().getString().length() == 0){
                Alert verifi = new Alert ("Verification!",
                        "Cover message is empty!"
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, cover.getCoverForm());
            } else{
                phone = new phone();
                phone.getPhoneForm().setCommandListener(this);
                this.display.setCurrent(phone.getPhoneForm());
            }
        } else if (c == phone.getCommandBatal()) {
            this.display.setCurrent(pesan.getPesanForm());
        } else if (c == phone.getCommandKirim()) {
            // VERIFIKASI SEBELUM PASTI KIRIM
            if (cover.getCoverTeks().getString().length() == 0){
                Alert verifi = new Alert ("Verification!",
                        "Cover message is empty!"
                        ,null,AlertType.INFO);
                display.setCurrent(verifi, cover.getCoverForm());
            } else{
                smsout = new smsout();
                smsout.smsout(phone.getNoTujuan().getString(), cover.getCoverTeks().getString());
                menu = new menu();
                menu.getMenuPageList().setCommandListener(this);
                startRechiver();
                this.display.setCurrent(smsout.getAlertKirim(), menu.getMenuPageList());
            }
        } else if (c == cmdShowText) {
            System.out.println(strPesan.getString());
            this.isiPesan = bacon.decode(strPesan.getString());
            bacaSMS();
            fr.setCommandListener(this);
            display.setCurrent(fr);
        } else if (c == pesan.getCommandHapus()) {
            pesan = new pesan();
            pesan.getPesanForm().setCommandListener(this);
            this.display.setCurrent(pesan.getPesanForm());
        } else if (c == cover.getCommandHapus()) {
            cover = new cover();
            cover.getCoverForm().setCommandListener(this);
            this.display.setCurrent(cover.getCoverForm());
        }else if (c == help.getCommandBatal()) {
            menu = new menu();
            menu.getMenuPageList().setCommandListener(this);
            this.display.setCurrent(menu.getMenuPageList());
        } else if (menu.getSelectedMenu().trim() == "Exit".trim()) {
            Quit();
        } else if (menu.getSelectedMenu().trim() == "Help".trim()) {
            help = new help();
            help.getHelpForm().setCommandListener(this);
            this.display.setCurrent(help.getHelpForm());
        } else if (menu.getSelectedMenu().trim() == "New Message".trim()) {
            pesan = new pesan();
            pesan.getPesanForm().setCommandListener(this);
            this.display.setCurrent(pesan.getPesanForm());
        } else if (menu.getSelectedMenu().trim() == "Inbox".trim()) {
            inbox.Tampil();
            this.display.setCurrent(inbox.getListInboxPageList());
        } else if (menu.getSelectedMenu().trim() == "Outbox".trim()) {
            outbox.Tampil();
            this.display.setCurrent(outbox.getListOutboxPageList());
        } else if (menu.getSelectedMenu().trim() == "Draf".trim()) {
            draf.TampilDraf();
            this.display.setCurrent(draf.getListDrafPageList());
        }

    }
}
