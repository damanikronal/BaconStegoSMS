/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.InputStreamReader;
/**
 *
 * @author Ronal Damanik
 */
public class LatinToBacon {

    private String latinf, baconf;
    private String latin=null,bacon=null,id=null;

    public LatinToBacon(String str){
        try {
            readFile("/bacon.txt",str);
        } catch (IOException ex) {
        }
    }

    private void readFile(String filename,String strCari) throws IOException {
        InputStreamReader reader = new InputStreamReader(
            getClass().getResourceAsStream(filename));
        String line = null;
        while ((line = readLine(reader)) != null) {
            int idx1=line.indexOf("~");
            latinf=line.substring(0,idx1);
            baconf=line.substring(idx1+1);
            String low=strCari.toLowerCase();
            if(latinf.equals(low)){
                this.latin=latinf;
                this.bacon=baconf;
                break;
            }else {
                
            }
            //mainForm.append(line + "\n");
        }
        reader.close();
        getBacon();
    }

    public String getBacon(){
        return bacon;
    }

    private String readLine(InputStreamReader reader) throws IOException {
        int readChar = reader.read();
        if (readChar == -1) {
            return null;
        }
        StringBuffer string = new StringBuffer("");
        // Read until end of file or new line
        while (readChar != -1 && readChar != '\n') {
            // Append the read character to the string. Some operating systems
            // such as Microsoft Windows prepend newline character ('\n') with
            // carriage return ('\r'). This is part of the newline character
            // and therefore an exception that should not be appended to the
            // string.
            if (readChar != '\r') {
                string.append((char)readChar);
            }
            // Read the next character
            readChar = reader.read();
        }
        return string.toString();
    }

}
