/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.rms.RecordStore;
import javax.wireless.messaging.*;

public class smsout implements Runnable {

    private RecordStore datasms;
    String noTujuan, isiPesan;
    private RecordStore recData, recData2;
    private phone phone = new phone();
    private pesan pesan = new pesan();
    private String str;
    private int nilai;
    Thread thread;
    private Alert al;

    public smsout() {
    }

    public void smsout(String noSender, String isiSMsku) {
        this.noTujuan = noSender;
        this.isiPesan = isiSMsku;
        SimpanSMS(noTujuan, isiPesan);
        al = new Alert("SMS Message", "Message Sent To : \n"
                + "Phone Number : " + noTujuan
                + "Message : " + isiPesan, null, AlertType.INFO);
        al.setTimeout(7000);
        thread = new Thread(this);
        thread.start();
    }

    public void SimpanSMS(String noSender, String isiSms) {

        try {
            recData = RecordStore.openRecordStore("outbox", true);
            String honey = noSender.trim() + "~"
                    + isiSms.trim() + "~" + "0";
            addRecordElement(recData, honey);
        } catch (Exception e) {
            System.out.println("ddd :" + e.toString());
        }
    }

    public void SimpantoDraf(String noSender, String isiSMs) {
        try {
            /*RecordStore.deleteRecordStore("RecordMember");*/

            recData2 = RecordStore.openRecordStore("draf", true);
            String honey = noSender.trim() + "~"
                    + isiSMs.trim() + "~" + "0";
            addRecordElement(recData2, honey);
        } catch (Exception e) {
            System.out.println("draft" + e.toString());
        }
    }

    private void addRecordElement(RecordStore rs, String valueRecord) {
        try {
            rs = RecordStore.openRecordStore(rs.getName().toString(), true);
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

    public Alert getAlertKirim() {
        return al;
    }

    public void run() {
        try {
            //kompresi
              
           //
            String url = "sms://" + noTujuan + ":" + "2009";
            //String url = "sms://"+noTujuan;
            MessageConnection connection = (MessageConnection) Connector.open(url);
            TextMessage msg = (TextMessage) connection.newMessage(MessageConnection.TEXT_MESSAGE);
            msg.setPayloadText(isiPesan);
            connection.send(msg);
            connection.close();

        } catch (Exception ex) {
            System.out.println("kesalahan smsout kirim:");
            ex.printStackTrace();
        }

    }
}

