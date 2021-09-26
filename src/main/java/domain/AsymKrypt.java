package domain;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AsymKrypt {
    PrivateKey privateKey;
    PublicKey publicKey;
    private static final String KEY_FOLDER_NAME = "keys";
    public static final String KEY_NAME = "userkey";
    private static final String KEY_PUB_SUFF = ".pub";
    private static final String KEY_PRIV_SUFF = ".priv";
    private String keyName;

    public AsymKrypt(String keyName, boolean generateIfNotExists) {

        this.keyName = (keyName != null) ? keyName : KEY_NAME;

        if (isKeyExisting(this.keyName)) {
            this.publicKey = readPublicKey(this.keyName);
            this.privateKey = readPrivateKey(this.keyName);
        } else if (!generateIfNotExists) {
            throw new IllegalArgumentException(String.format("There is no Keyfile '%s'", keyName));
        } else {
            System.out.println("Generating Keys...");
            generateKeys();
            savePrivateKey(this.keyName);
            savePublicKey(this.keyName);
        }
    }

    private static String getKeyNameWithPath(String keyName, boolean isPrivateKey) {
        String returnKeyPath = null;
        String folderWithSeparator = KEY_FOLDER_NAME + File.separatorChar;
        returnKeyPath = folderWithSeparator + keyName + ((isPrivateKey) ? KEY_PRIV_SUFF : KEY_PUB_SUFF);
        return returnKeyPath;
    }

    public static boolean verify(byte[] sigToVerify, byte[] data, PublicKey pub) {
        boolean returnBool = false;
        try {
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pub);
            sig.update(data);
            returnBool = sig.verify(sigToVerify);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            System.err.println("Caught exception " + e.toString());
        }
        return returnBool;
    }

    public void generateKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    public byte[] signData(String data) {
        byte[] returnStr = null;
        try {
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(privateKey);
            byte[] dataByte = data.getBytes();
            dsa.update(dataByte);

            byte[] realSig = dsa.sign();
            returnStr = realSig;

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }

        return returnStr;
    }

    public static boolean isKeyExisting(String keyName) {
        boolean isKeyExisting = false;
        File f = new File(getKeyNameWithPath(keyName, true));
        if (f.exists() && !f.isDirectory()) {
            isKeyExisting = true;
        }
        return isKeyExisting;
    }

    public static byte[] readFromFile(String filename) {
        byte[] encKey = null;
        try {
            FileInputStream keyfis = new FileInputStream(filename);
            encKey = new byte[keyfis.available()];
            keyfis.read(encKey);
            keyfis.close();
        } catch (Exception e) {
            System.out.println(String.format("Can not read file. Is file '%s' existing?", filename));
        }
        return encKey;
    }

    public static PublicKey readPublicKey(String keyPubName) {
        PublicKey pub = null;
        byte[] encKey = readFromFile(getKeyNameWithPath(keyPubName, false));
        try {
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            pub = keyFactory.generatePublic(pubKeySpec);
        } catch (Exception e) {
            System.out.println(String.format("Can not read key '%s'.", keyPubName));

        }
        return pub;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public static PrivateKey readPrivateKey(String keyPrivName) {
        PrivateKey priv = null;
        byte[] encKey = readFromFile(getKeyNameWithPath(keyPrivName, true));
        try {
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(encKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            priv = keyFactory.generatePrivate(privKeySpec);
        } catch (Exception e) {
            System.err.println("Caught exception when reading private key: " + e.toString());
            System.exit(1);
        }
        return priv;
    }

    public void savePublicKey(String keyPubName) {
        /* save the public key in a file */
        byte[] key = publicKey.getEncoded();
        FileOutputStream keyfos = null;
        try {
            keyfos = new FileOutputStream(getKeyNameWithPath(keyPubName, false));
            keyfos.write(key);
            keyfos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Can not write to disk: " + ex);

        } catch (IOException ex) {
            System.out.println("IO error: " + ex);
        } finally {
            if (keyfos != null) {
                //keyfos.close();
            }
        }
    }

    public void savePrivateKey(String keyPrivName) {
        /* save the public key in a file */
        byte[] key = privateKey.getEncoded();
        FileOutputStream keyfos = null;
        try {
            keyfos = new FileOutputStream(getKeyNameWithPath(keyPrivName, true));
            keyfos.write(key);
            keyfos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Can not write to disk: " + ex);
        } catch (IOException ex) {
            System.out.println("IO error: " + ex);
        } finally {
            if (keyfos != null) {
                //keyfos.close();
            }
        }
    }

    public String getKeyName() {
        return keyName;
    }

}
