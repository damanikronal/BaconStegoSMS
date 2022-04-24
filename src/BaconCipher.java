/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ronal Damanik
 */
public class BaconCipher {

    private LatinToBacon latinTobacon;
    private BaconToLatin baconTolatin;
    private VerifyText verify;

    public BaconCipher(){
        verify = new VerifyText();
    }

    /*
     *  METHOD MENGGANTI KARAKTER DALAM STRING
     */
    public static String replace(String _text, String _searchStr, String _replacementStr) {
        // String buffer to store str
        StringBuffer sb = new StringBuffer();

        // Search for search
        int searchStringPos = _text.indexOf(_searchStr);
        int startPos = 0;
        int searchStringLength = _searchStr.length();

        // Iterate to add string
        while (searchStringPos != -1) {
            sb.append(_text.substring(startPos, searchStringPos)).append(_replacementStr);
            startPos = searchStringPos + searchStringLength;
            searchStringPos = _text.indexOf(_searchStr, startPos);
        }

        // Create string
        sb.append(_text.substring(startPos, _text.length()));

        return sb.toString();
    }

    /*
     *  METHOD MENNGHAPUS KARAKTER DALAM STRING
     */
    public static String removeCharacters(String text, String charsToKeep) {
         StringBuffer buffer = new StringBuffer();
         for(int i = 0; i < text.length(); i++) {
             char ch = text.charAt(i);
             if(charsToKeep.indexOf(ch) > -1) {
                 buffer.append(ch);
             }
         }
         return buffer.toString();
    }
    
    /*
     *  KONVERSI HURUF LATIN KE HURUF BACON
     */
    public String convertLatinBacon(String textToHide){
        int hideLength = textToHide.length();
        String cipheredText = "";
        for (int pos = 0; pos < hideLength; ++pos) {
            latinTobacon = new LatinToBacon(String.valueOf(textToHide.charAt(pos)));
            cipheredText += latinTobacon.getBacon();
        }
        cipheredText = BaconCipher.replace(cipheredText, "null", "");
        cipheredText = BaconCipher.replace(cipheredText, "___", "");
        return cipheredText;
    }

    public String encode(String baconText, String textToShow) {
        String cipheredText;
        cipheredText = baconText;

        /*
         * Okay, now our textToShow is the same length as the cipher, without
         * any punctuation. Now we need to encode textToShow based on the
         * cipher...
         */
        String encodedMessage = "";

        int cipherPos = 0;
        int textPos = 0;
        do {
            if (verify.isAlfabet(textToShow.charAt(textPos)) == false) {
                encodedMessage += textToShow.charAt(textPos++);
                continue;
            }

            if (cipheredText.charAt(cipherPos++) == 'A') {
                encodedMessage += String.valueOf(textToShow.charAt(textPos++)).toUpperCase();
            } else {
                encodedMessage += String.valueOf(textToShow.charAt(textPos++)).toLowerCase();
            }

        } while (cipherPos < cipheredText.length());

        return encodedMessage;
    }

    public String decode(String textToDecipher) {

        String baconedText = "";
        for (int pos = 0; pos < textToDecipher.length(); ++pos) {
            char currentChar = textToDecipher.charAt(pos);
            /* Uses ASCII values */
            if (currentChar > 64 && currentChar < 91) {
                baconedText += "A";
            } else if (currentChar > 96 && currentChar < 123) {
                baconedText += "B";
            }
            System.out.println(baconedText);
        }
        /* Okay, let's re-assemble the secret message */
        
        String decodedMessage = "";
        int baconLength = baconedText.length();
        for (int pos = 0; pos < baconLength; pos += 5) {
            String block = baconedText.substring(pos, pos + 5);

            /*
             * We add a space between each letter because we don't know where
             * the real word breaks are, and it's easier to read with a spacer
             * than all bunched up...
             */
            baconTolatin = new BaconToLatin(block);
            decodedMessage += (baconTolatin.getLatin() + " ");
        }

        return decodedMessage;
    }

}
