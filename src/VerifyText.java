/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
public class VerifyText {
    
    public VerifyText(){
       
    }

    public boolean isAlfabet(char inString) {

        boolean alfabet = false;
        char ch = inString;
         //for(int i = 0; i < inString.length(); i++) {
             //char ch = inString.charAt(i);
             if((ch >= 'a') && (ch <= 'z')) {
                 alfabet = true;
                 
             } else if((ch >= 'A') && (ch <= 'Z')) {
                 alfabet = true;
                 
             } else{
                 alfabet = false;
             }
         //}
        
        return alfabet;
     }
    
    public boolean countNonAlfabet(String inString) {
        boolean alfabet = false;
        int count = 0;
         for(int i = 0; i < inString.length(); i++) {
             char ch = inString.charAt(i);
             if((ch >= 'a') && (ch <= 'z')) {
                 count = count + 0;

             } else if((ch >= 'A') && (ch <= 'Z')) {
                 count = count + 0;

             } else if(ch == ' ') {
                 count = count + 0;

             } else{
                 count = count + 1;
             }
         }

        if (count == 0){
            alfabet = true;
        } else if (count > 0){
            alfabet = false;
        }
        return alfabet;
     }

    public int countAlfabet(String inString) {
        int count = 0;
         for(int i = 0; i < inString.length(); i++) {
             char ch = inString.charAt(i);
             if((ch >= 'a') && (ch <= 'z')) {
                 count = count + 1;

             } else if((ch >= 'A') && (ch <= 'Z')) {
                 count = count + 1;

             } else{
                 //Do nothing
             }
         }

        return count;
     }
    /*
    public String countAlfabet(String inString) {
        String text="";
        int count = 0;
        StringBuffer buffer = new StringBuffer(inString.length());
         for(int i = 0; i < inString.length(); i++) {
             char ch = inString.charAt(i);
             if((ch >= 'a') && (ch <= 'z')) {
                 text += buffer.append(ch).toString();

             } else if((ch >= 'A') && (ch <= 'Z')) {
                 text += buffer.append(ch).toString();

             } else{
                 // Do nothing
             }
         }

        return text;
     }*/
    
}
