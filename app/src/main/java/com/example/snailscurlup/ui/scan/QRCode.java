package com.example.snailscurlup.ui.scan;

import android.location.Address;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/** PLEASE REMOVE ALL LINKS TO THIS CODE ASS WE ARE NOT USING IT ANYMORE  */
/** We are using QRInstanceNew and ABstracQR **/
public class QRCode {

    NamesOfQR names = new NamesOfQR();
    private String hash;

    private String data;
    private String URL;
    private String name;
    private int pointsInt;

    private List<Address> scannedadresslist;


    private GeoPoint geoPoint;

    public QRCode(String data) {
        this.data = data;
        setHash(data);
        setURL();
        setName();
        setPointsInt();
        this.scannedadresslist = null;

        //setgeoPoint(latitude,longitude);

    }


    public String setHash(String data) {
        this.hash = getHash256(data);
        return this.hash;
    }

    // Getter and Setter for hash
    public String getHash() {
        return hash;
    }

    public void setURL() {
        BigInteger seed = new BigInteger(this.hash, 16);
        String NewURL = "https://picsum.photos/seed/" + String.valueOf(seed) + "/270";
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
    public int getPointsInt() {
        return this.pointsInt;
    }
    public void setPointsInt() {
        // Get and set points
        int newpointsInt = calculateScore(this.hash);
        this.pointsInt = newpointsInt;
    }

    public void setgeoPoint(Double latitude,Double longitude) {
        this.geoPoint = new GeoPoint(latitude, longitude);
    }

    public GeoPoint getgeoPoint() {
        return this.geoPoint;
    }



    public String getHash256(@NonNull String data) {
        String hash256;
        try {
            // From https://www.baeldung.com/sha-256-hashing-java
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashHex = new StringBuilder(2 * hashBytes.length);
            for (int i = 0; i < hashBytes.length; i++) {
                String hex = Integer.toHexString(0xff & hashBytes[i]);
                if(hex.length() == 1) {
                    hashHex.append('0');
                }
                hashHex.append(hex);
            }
            hash256 = hashHex.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println("Use SHA-256 message digest algorithm");
            hash256 = Integer.toHexString(data.hashCode());
        }
        return hash256;
    }

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

    public void  setscanadresslist(List<Address> listAdress){
            this.scannedadresslist = listAdress;


    }
    public List<Double> getScannedCoordinates(){
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(this.scannedadresslist.get(0).getLatitude());
        coordinates.add(this.scannedadresslist.get(0).getLongitude());
        return coordinates;

    }
    public String getScannedAddress(){
        return this.scannedadresslist.get(0).getAddressLine(0);
    }

    public String getData() {
        return this.data;
    }
}

