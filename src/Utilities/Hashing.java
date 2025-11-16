package Utilities;

import java.security.*;
import java.security.MessageDigest;


public class Hashing {
    public static String hashPassword(String password) {
        try {
            MessageDigest pass = MessageDigest.getInstance("SHA-256");
            byte[] binCode = pass.digest(password.getBytes());
            char[] hexCode = binToHex(binCode);
            return new String(hexCode); // the array of chars into String
        } catch(Exception e) {
            System.out.println("Hashing failed; " + e.getMessage());
            return null;
        }
    }

    private static char[] binToHex(byte[] bin) {
        char[] hex = new char[bin.length * 2]; // each byte into 2 hex
        for (int i = 0, j = 0; i < bin.length; i++) {
            int v = bin[i] & 0xFF; // make byte unsigned -> important because >>> works with int and wjen a byte is seen as a negative, its conversion to int is by adding 1s instead of 0s!
            hex[j++] = binToHexMap((byte) (v >>> 4)); // >>> not >> to be unsigned shift (add zeros not 1s if the value is read as negative)
            hex[j++] = binToHexMap((byte) (bin[i] & 0x0F)); // make the first 4 bits 0s to get the last 4 bits
        }
        return hex;
    }

    private static char binToHexMap(byte bin){
        // basically converts a binary to hex
        // binary must be in the format 00000000 to 00001111 (0 to 15)

        if(bin > 15 ||  bin < 0){
            return 'x'; // indicates failure
        }

        if(bin < 10){
            return (char)(bin + '0'); // char same as binary value
        } else{
            return (char)(bin-10 + 'a');
        }
    }

    public static void main(String[] args) {
        // trying a pass hash
        String password = "hello";
        String hash = hashPassword(password);
        System.out.println("Hash: " + hash);
        // known hash-256 for hellow: 2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824

        // testing hex conversions is correct
        System.out.println("Testing binToHexMap:");
        for (byte i = 0; i < 16; i++) {
            System.out.println(i + " -> '" + binToHexMap(i) + "'");
        }
    }
}
