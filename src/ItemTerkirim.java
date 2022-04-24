/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
import java.io.*;

public class ItemTerkirim {

    private RecordStore recStore;
    private List recList;
    private List recList2;
    private Command cmdCancel, cmdBaca, cmdHapus;
    private Image image;
    private Image image1;
    private String str;
    private int nilai;
    private String isiPesan2;
    private List menubaca;

    public ItemTerkirim() {
        recList = new List("Outbox :", List.IMPLICIT);
        recList2 = new List("Outbox :", List.IMPLICIT);
        cmdCancel = new Command("Cancel", Command.CANCEL, 0);
        cmdBaca = new Command("Read SMS", Command.OK, 0);
        cmdHapus = new Command("Delete", Command.OK, 1);

        recList.addCommand(cmdCancel);
        recList.addCommand(cmdBaca);
        recList.addCommand(cmdHapus);
    }

    public void Tampil() {
        String isiPesan = null;
        String noTelpon = null;
        String id = null;
        recList.deleteAll();
        recList2.deleteAll();
        try {
            recStore = RecordStore.openRecordStore("outbox", true);
            // Mengambil isi dari record store
            RecordEnumeration enumerator = recStore.enumerateRecords(null, null, false);
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
                    int idx3 = item.indexOf("~", idx2 + 1);

                    noTelpon = item.substring(0, idx1);

                    isiPesan = item.substring(idx1 + 1, idx2);
                    if (isiPesan.length() > 20) {
                        isiPesan2 = isiPesan.substring(20);
                    } else {
                        isiPesan2 = isiPesan;
                    }

                    id = item.substring(idx2 + 1, idx3);
                } catch (Exception e) {
                    System.out.println("Kesalahan di sini:" + e);
                }
                image = Image.createImage("/img/outbox.png");
                image1 = Image.createImage("/img/outbox.png");
                String abc = " " + noTelpon.trim() + "\n        " + isiPesan2.trim() + "...";
                if (id == "0" || id.equals("0")) {
                    int y = recList.append(abc, image);
                    int y2 = recList2.append(item, image);
                    System.out.println("id :" + id);
                } else {
                    int y = recList.append(abc, image1);
                    int y2 = recList2.append(item, image1);
                    System.out.println("id1 :" + id);
                }
                dIn.close();
                in.close();
            }
            recStore.closeRecordStore();
        } catch (Exception e) {
            System.out.println("error in here dear : " + e.toString());
        }
    }

    public void Hapus(String strData) {

        System.out.println("sd " + strData);
        try {
            recStore = RecordStore.openRecordStore("outbox", false);

            recStore = RecordStore.openRecordStore("outbox", true);
            // Mengambil isi dari record store
            RecordEnumeration enumerator = recStore.enumerateRecords(null, null, false);
            while (enumerator.hasNextElement()) {
                // Menuju Record selanjutnya
                int recid = enumerator.nextRecordId();

                ByteArrayInputStream in = new ByteArrayInputStream(recStore.getRecord(recid));
                DataInputStream dIn = new DataInputStream(in);
                int count = dIn.readInt();
                String inn = dIn.readUTF();
                System.out.println("se " + inn);
                System.out.println("recid " + recid);
                if (inn.equals(strData)) {
                    recStore.deleteRecord(recid);

                    break;
                }
            }

        } catch (IOException is) {
        } catch (RecordStoreNotOpenException e) {
        } catch (RecordStoreException ex) {
        }
    }

    public List getListOutboxPageList() {
        return recList;
    }

    public Command getCommandCancel() {
        return cmdCancel;
    }

    public Command getCommandBaca() {
        return cmdBaca;
    }

    public Command getCommandHapus() {
        return cmdHapus;
    }

    public String getListAnggotaPageList2Selected() {
        return recList2.getString(recList.getSelectedIndex());
    }
}
