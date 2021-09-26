package domain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    public static String calculateHash(String strToHash) {
        String returnHash = "";
        try {
            byte[] bytesOfMessage = strToHash.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] thedigest = md.digest(bytesOfMessage);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < thedigest.length; i++) {
                String hex = Integer.toHexString(0xff & thedigest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            returnHash = hexString.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return returnHash;
    }
}
