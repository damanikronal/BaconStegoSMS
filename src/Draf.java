/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class Draf {

    private RecordStore recStored;
    private List recListd;
    private List recListd2;
    private Command cmdCancel;
    private Image image;
    private Image image1;
    private String str;
    private int nilai;
    private String isiPesan2;
    private List menubaca;
    String isiPesan = null;
    String noTelpon = null;
    String id = null;
    private Command cmdBaca;
    private Command cmdHapus;

    public Draf() {
        recListd = new List("Draf :", List.IMPLICIT);
        recListd2 = new List("Draf :", List.IMPLICIT);
        cmdCancel = new Command("Cancel", Command.CANCEL, 0);
        cmdBaca = new Command("Read SMS", Command.OK, 0);
        cmdHapus = new Command("Delete", Command.OK, 1);

        recListd.addCommand(cmdCancel);
        recListd.addCommand(cmdBaca);
        recListd.addCommand(cmdHapus);
    }

    public void TampilDraf() {

        recListd.deleteAll();
        recListd2.deleteAll();
        try {
            recStored = RecordStore.openRecordStore("draf", true);
            // Mengambil isi dari record store
            RecordEnumeration enumerator = recStored.enumerateRecords(null, null, false);
            while (enumerator.hasNextElement()) {
                // Menuju Record selanjutnya
                byte[] recBytes = enumerator.nextRecord();
                ByteArrayInputStream in = new ByteArrayInputStream(recBytes);
                DataInputStream dIn = new DataInputStream(in);
                int count = dIn.readInt();
                String item = dIn.readUTF();
                try {
                    int idx1 = item.indexOf("~");
                    int idx2 = item.indexOf("~", idx1 + 1);


                    noTelpon = item.substring(0, idx1);

                    isiPesan = item.substring(idx1 + 1, idx2);
                    if (isiPesan.length() > 20) {
                        isiPesan2 = isiPesan.substring(20);
                    } else {
                        isiPesan2 = isiPesan;
                    }
                    id = item.substring(idx2 + 1);
                } catch (Exception e) {
                    System.out.println("eror di sini" + e);
                }
                image = Image.createImage("/img/draf.png");
                image1 = Image.createImage("/img/draf.png");
                String abc = isiPesan2.trim() + "...";
                if (id.equals("0")) {
                    int y = recListd.append(abc, image);
                    int y2 = recListd2.append(item, image);
                } else {
                    int y = recListd.append(abc, image1);
                    int y2 = recListd2.append(item, image1);
                }
                dIn.close();
                in.close();
            }
            recStored.closeRecordStore();
        } catch (Exception e) {
            System.out.println("error in here dear : " + e.toString());
        }
    }

    public void Hapus(String strData) {

        System.out.println("sd " + strData);
        try {
            recStored = RecordStore.openRecordStore("draf", false);

            recStored = RecordStore.openRecordStore("draf", true);
            // Mengambil isi dari record store
            RecordEnumeration enumerator = recStored.enumerateRecords(null, null, false);
            while (enumerator.hasNextElement()) {
                // Menuju Record selanjutnya
                int recid = enumerator.nextRecordId();

                ByteArrayInputStream in = new ByteArrayInputStream(recStored.getRecord(recid));
                DataInputStream dIn = new DataInputStream(in);
                int count = dIn.readInt();
                String inn = dIn.readUTF();
                if (inn.equals(strData)) {
                    recStored.deleteRecord(recid);

                    break;
                }
            }

        } catch (IOException is) {
        } catch (RecordStoreNotOpenException e) {
        } catch (RecordStoreException ex) {
        }
    }

    public List getListDrafPageList() {
        return recListd;
    }

    public Command getCommandCancelD() {
        return cmdCancel;
    }

    public Command getCommandBacaD() {
        return cmdBaca;
    }

    public Command getCommandHapusD() {
        return cmdHapus;
    }

    public String getListDrafPageList2Selected() {
        return recListd2.getString(recListd.getSelectedIndex());
    }
}
