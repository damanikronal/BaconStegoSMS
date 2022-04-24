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

public class smsin {

    private RecordStore recStore;
    private List recList;
    private List recList2;
    private Command cmdCancel, cmdBalas, cmdHapus, cmdSimpan, cmdBaca;
    private Image image;
    private Image image1;
    private String str;
    private int nilai;
    private String isiPesan2;
    private List menubaca;
    String isiPesan = null;
    String noTelpon = null;
    String id = null;

    public smsin() {

        recList = new List("Inbox :", List.IMPLICIT);
        recList2 = new List("Inbox :", List.IMPLICIT);
        cmdBaca = new Command("Read SMS", Command.OK, 0);
        cmdCancel = new Command("Cancel", Command.CANCEL, 1);
        //cmdBalas= new Command(Library.MnKirimBalas[nilai], Command.OK, 1);
        //cmdSimpan=new Command(Library.mnPesanSimpan[nilai], Command.OK, 2);
        cmdHapus = new Command("Delete", Command.OK, 2);

        recList.addCommand(cmdCancel);
        recList.addCommand(cmdBaca);
        //recList.addCommand(cmdBalas);
        //recList.addCommand(cmdSimpan);
        recList.addCommand(cmdHapus);
    }

    public void Tampil() {

        recList.deleteAll();
        recList2.deleteAll();
        try {
            recStore = RecordStore.openRecordStore("inbox", true);
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
                    int idx4 = item.indexOf("~", idx3 + 1);

                    noTelpon = item.substring(0, idx1);

                    isiPesan = item.substring(idx1 + 1, idx2);
                    if (isiPesan.length() > 20) {
                        isiPesan2 = isiPesan.substring(20);
                    } else {
                        isiPesan2 = isiPesan;
                    }

                    id = item.substring(idx3 + 1, idx4);
                } catch (Exception e) {
                    System.out.println("Kesalahan di sini:" + e);
                }
                image = Image.createImage("/img/inbox.png");
                image1 = Image.createImage("/img/inbox.png");
                String abc = " " + noTelpon.trim() + "\n        " + isiPesan2.trim() + "...";
                //String abc=noTelpon;
                if (id == "0" || id.equals("0")) {
                    int y = recList.append(abc, image);
                    int y2 = recList2.append(item, image);
                } else {
                    int y = recList.append(abc, image1);
                    int y2 = recList2.append(item, image1);
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
        try {
            recStore = RecordStore.openRecordStore("inbox", false);

            recStore = RecordStore.openRecordStore("inbox", true);
            // Mengambil isi dari record store
            RecordEnumeration enumerator = recStore.enumerateRecords(null, null, false);
            while (enumerator.hasNextElement()) {
                // Menuju Record selanjutnya
                int recid = enumerator.nextRecordId();

                ByteArrayInputStream in = new ByteArrayInputStream(recStore.getRecord(recid));
                DataInputStream dIn = new DataInputStream(in);
                int count = dIn.readInt();
                String inn = dIn.readUTF();
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

    public Command getCommandCancel() {
        return cmdCancel;
    }

    public Command getCommandBalas() {
        return cmdBalas;
    }

    public Command getCommandHapus() {
        return cmdHapus;
    }

    public Command getCommandSimpan() {
        return cmdSimpan;
    }

    public Command getCommandBaca() {
        return cmdBaca;
    }

    public List getListInboxPageList() {
        return recList;
    }

    public String getListInboxPageList2Selected() {
        return recList2.getString(recList.getSelectedIndex());
    }
}

