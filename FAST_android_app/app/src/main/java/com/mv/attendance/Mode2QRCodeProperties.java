package com.mv.attendance;

import java.util.Objects;

public class Mode2QRCodeProperties {

    String PRN;
    static String first;
    Lecture lecture;
    Student this_student;
    long time_when_QRCode_Scanned;
    static String Title1;
    static String Teacher1;
    static String division;
    static String subject;

    String QR_CODE_Encoded_Title;
    String QR_CODE_Encoded_trName;
    long QR_CODE_Encoded_time;

    public Mode2QRCodeProperties(String textFromQRCode, long unixTimeStamp) {

        //{}|MAS|Popatrao|1679044524|
        //NEW  -  {}|Title|Teacher_PRN|C|Subject|1679681397|

        String[] strsplit = textFromQRCode.split("\\|");
        first = strsplit[0];
        Title1 = strsplit[1];
        Teacher1 = strsplit[2];
        division = strsplit[3];
        subject = strsplit[4];
        this.QR_CODE_Encoded_time = Long.parseLong(strsplit[5]);
    }

    public static boolean check_textFromQRCode_isCorrect(String textFromQRCode, long unixTimeStamp){
        int count = 0;
        for(int i=0; i<textFromQRCode.length();i++){
            if((textFromQRCode.charAt(i) == '|')){count++;}

        }

        String first_two_digits = textFromQRCode.substring(0, 2);

        if(!first_two_digits.equals("{}")){return false;}
        if(textFromQRCode==null||textFromQRCode.isEmpty()){return false;}
        if(count!=6){return false;}

        return true;


    }

    public static String decodeScannedString(String inputStr, int shiftKey){
        String ALPHABET = "abcdefghijklmnopqrstuvwxyz1234567890|.,{}ABCDEFGHIJKLMNOPQRSTUVWXYZ '[]<>()!@#$%^&*-_";

            // decryptStr to store decrypted data
            String decryptStr = "";

            // use for loop for traversing each character of the input string
            for (int i = 0; i < inputStr.length(); i++)
            {

                // get position of each character of inputStr in ALPHABET
                int pos = ALPHABET.indexOf(inputStr.charAt(i));

                // get decrypted char for each char of inputStr
                int decryptPos = (pos - shiftKey) % ALPHABET.length();

                // if decryptPos is negative
                if (decryptPos < 0){
                    decryptPos = ALPHABET.length() + decryptPos;
                }
                char decryptChar = ALPHABET.charAt(decryptPos);

                // add decrypted char to decrypted string
                decryptStr += decryptChar;
            }
            // return decrypted string
            return decryptStr;
    }

    public boolean checkIfTimeInBuffer(long seconds) {
        long unixTime = System.currentTimeMillis() / 1000L;
        if(unixTime - QR_CODE_Encoded_time <= seconds){
            return true;
        };
        return false;
    }
}
