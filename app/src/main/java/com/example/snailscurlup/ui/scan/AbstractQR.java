package com.example.snailscurlup.ui.scan;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * AbstractQR
 * <p>
 * Represent  Single Qrcode object type, has unique hash,name picture, and number of points based on its data
 * A single AbstractQr code can be used in instatiation of many QrCodeInstanceNew, represent instance of Abstract Qr being scanned
 *
 * @author AyanB123
 */
public class AbstractQR implements Serializable {


    // represents the list of all the names of the Qr codes that randomly assigned to each New AbtractQr
    NamesOfQR names = new NamesOfQR();


    // Class properties important to each AbstractQR
    private String hash;

    private String URL;
    private String name;
    private Integer pointsInt;


    private ArrayList<AbstractQRComment> QRcomments;


    // Class costructor

    /**
     * AbstractQR() constructor
     * <p>
     * Default Constructor: uses data define other properties for Qr code
     *
     * @author AyanB123
     */
    public AbstractQR(String data) {
        setHash(data);
        setURL();
        setName();
        setPointsInt();
        this.QRcomments = new ArrayList<>();


    }


    /**
     * AbstractQR() constructor
     * <p>
     * Overloaded Constructor: uses data define other properties for Qr code
     *
     * @author AyanB123
     */
    public AbstractQR(String hash, String name, Integer Points, String URL) {
        this.hash = hash;
        this.name = name;
        this.pointsInt = Points;
        this.QRcomments = new ArrayList<>();
    }

    /**
     * getHash256Ins() method
     * <p>
     * Returns the SHA-256 hash of the given data
     * This is Static method used outisde the class
     * <p>
     * Adds a comment to the QR code
     *
     * @author AyanB123
     */
    public static String getHash256Ins(String data) {
        String hash256;
        try {
            // From https://www.baeldung.com/sha-256-hashing-java
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashHex = new StringBuilder(2 * hashBytes.length);
            for (int i = 0; i < hashBytes.length; i++) {
                String hex = Integer.toHexString(0xff & hashBytes[i]);
                if (hex.length() == 1) {
                    hashHex.append('0');
                }
                hashHex.append(hex);
            }
            hash256 = hashHex.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Use SHA-256 message digest algorithm");
            hash256 = Integer.toHexString(data.hashCode());
        }
        return hash256;

    }

    // Getter and Setter for hash
    public String getHash() {
        return hash;
    }

    // Setters and Getter for class properties
    public void setHash(String data) {
        this.hash = getHash256(data);
    }

    /**
     * setURL() method
     * <p>
     * Uses hash to set URL of Qr code
     *
     * @author MAl-l3R
     */
    public void setURL() {
        BigInteger seed = new BigInteger(this.hash, 16);
        String NewURL = "https://picsum.photos/seed/" + seed + "/270";
        this.URL = NewURL;
    }

    // Getter and Setter for URL
    public String getURL() {
        return URL;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    /**
     * setName() method
     * <p>
     * Uses hash to set name of Qr code
     *
     * @author MAl-l3R (original Code moved from ScanFragment to here by AyanB123)
     */
    // setter for name uses names list to randomly assign a name to each Qr code
    public void setName() {
        long seed1 = Long.parseLong(this.hash.substring(0, 15), 16);
        Random rand = new Random(seed1);
        int adjIndex = rand.nextInt(names.adjectives.length);
        BigInteger seed2 = new BigInteger(hash, 16);
        Random random = new Random(seed2.longValue());
        int nounIndex = random.nextInt(names.nouns.length);
        String name = names.adjectives[adjIndex] + " " + names.nouns[nounIndex];
        this.name = name;
    }

    // Getter and Setter for pointsInt
    public Integer getPointsInt() {
        return this.pointsInt;
    }

    /**
     * setPointsInt() method
     * <p>
     * Uses hash to set points of Qr code
     *
     * @author AyanB123
     */
    public void setPointsInt() {
        // Get and set points
        int newpointsInt = calculateScore(this.hash);
        this.pointsInt = newpointsInt;
    }


    // method used set points of a Qr code based on its hash

    /**
     * getHash256
     * <p>
     * Returns the SHA-256 hash of the given data
     *
     * @author MAl-l3R (original Code moved from ScanFragment to here by AyanB123)
     */
    public String getHash256(@NonNull String data) {
        String hash256;
        try {
            // From https://www.baeldung.com/sha-256-hashing-java
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashHex = new StringBuilder(2 * hashBytes.length);
            for (int i = 0; i < hashBytes.length; i++) {
                String hex = Integer.toHexString(0xff & hashBytes[i]);
                if (hex.length() == 1) {
                    hashHex.append('0');
                }
                hashHex.append(hex);
            }
            hash256 = hashHex.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Use SHA-256 message digest algorithm");
            hash256 = Integer.toHexString(data.hashCode());
        }
        return hash256;
    }

    // getter and setter for comments

    /**
     * getHash256
     * <p>
     * Returns the SHA-256 hash of the given data
     *
     * @author MAl-l3R (original Code moved from ScanFragment to here by AyanB123)
     */
    public int calculateScore(@NonNull String hex) {
        int score = 0;
        Map<Integer, Integer> digitCount = new HashMap<>();

        for (int i = 0; i < hex.length(); i++) {
            String hexValue = String.valueOf(hex.charAt(i));
            int digit = Integer.parseInt(hexValue, 16);
            // If this is a new digit, add it to the map with a count of 1
            if (!digitCount.containsKey(digit)) {
                digitCount.put(digit, 1);
            }
            // Otherwise, increment the count for this digit
            else {
                digitCount.put(digit, digitCount.get(digit) + 1);
            }
        }


        for (int i = 0; i < digitCount.size(); i++) {
            int digit = (int) digitCount.keySet().toArray()[i];
            int count = digitCount.get(digit);

            if (count >= 2) {
                int repeatScore = (int) Math.pow(2, count - 1);
                score += repeatScore;
            }
        }
        return score;
    }

    /**
     * addComment() method
     * <p>
     * Adds a comment to the QR code
     *
     * @author AyanB123
     */
    public void addComment(String username, String message, Timestamp commentTimeStamp) {

        // If QRcomments is null, create a new ArrayList ensure since had AbstractQr exist before we made the comments
        if (QRcomments == null) {
            QRcomments = new ArrayList<>();
        }

        AbstractQRComment newSingleComment = new AbstractQRComment(username, message, commentTimeStamp);

        QRcomments.add(newSingleComment);


    }

    public ArrayList<AbstractQRComment> getQRcomments() {
        return QRcomments;
    }

    public String getGenUrl(){
        BigInteger seed = new BigInteger(this.hash, 16);
        String NewURL = "https://picsum.photos/seed/" + seed + "/270";
       return NewURL;


    }

}

