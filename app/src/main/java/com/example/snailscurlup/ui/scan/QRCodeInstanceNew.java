package com.example.snailscurlup.ui.scan;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.snailscurlup.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;


/**
 * Represent lke Single Qrcode object type, has unique hasg,name picture, and number of points based on its data
 * A single Qr code can hav emany instance of it
 */
public class QRCodeInstanceNew {

    private final  AbstractQR QRCodeAbstractType;

    private final Timestamp scanQRLogTimeStamp;

    private Bitmap scanQRLogImage;

    private Context context;


    private String scanQRLogLocation;

    private User scanQRInstanceUser;

    public QRCodeInstanceNew(AbstractQR scannedAbstractQRCodeType, User user, Bitmap scannedQRLogImage, Timestamp scanndQRLogTimeStamp, String ScanedQRLogLocation) {
        this.QRCodeAbstractType = scannedAbstractQRCodeType;
        this.scanQRLogImage = scannedQRLogImage;
        this.scanQRInstanceUser = user;
        this.scanQRLogTimeStamp = scanndQRLogTimeStamp;

        // call extra method handle IOException e with try and catch block
        setScanQRComponent(ScanedQRLogLocation, scannedQRLogImage);
    }

    public QRCodeInstanceNew(String data, User user) {
        // Alternate constructor that builds it with the data
        AbstractQR QRType = new AbstractQR(data);
        this.QRCodeAbstractType = QRType;
        this.scanQRLogImage = null;
        this.scanQRInstanceUser = user;
        this.scanQRLogTimeStamp = null;
    }


    public void setScanQRComponent(String ScanedQRLogLocation, Bitmap scannedQRLogImage) {

        try {
            // call a method that may throw an IOException
            setScanQRLogLocation(ScanedQRLogLocation);
            setScanQRLogImage(scannedQRLogImage);
        } catch (IOException e) {
            // handle the exception
            System.err.println("An error occurred: " + e.getMessage());
        }

    }


    public Timestamp getScanQRLogTimeStamp() {
        return scanQRLogTimeStamp;
    }

    public Bitmap getScanQRLogImage() {
        return scanQRLogImage;
    }


    public void setScanQRLogImage(Bitmap QRLogImage) throws IOException {
        if (scanQRLogImage == null) {
            try {
                AssetManager assetManager = context.getApplicationContext().getAssets();


                InputStream inputStream = assetManager.open("defalt_QR_logphoto");
                // Decode the input stream into a Bitmap object
                scanQRLogImage = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            this.scanQRLogImage = QRLogImage;
        }
    }


    public void setScanQRLogLocation(String QRLoglocation) {
        if (QRLoglocation == null) {
            this.scanQRLogLocation = "Not Saved";
        } else {
            this.scanQRLogLocation = QRLoglocation;
        }
    }

    public String getScanQRLogLocation() {
        return this.scanQRLogLocation;
    }


    // get basic iformation about QR code
    public AbstractQR getQRTypeDetail() {
        return QRCodeAbstractType;
    }


    public User getScanQRInstanceUser() {
        return scanQRInstanceUser;
    }

    public void setScanQRInstanceUser(User scanQRInstanceUser) {
        this.scanQRInstanceUser = scanQRInstanceUser;
    }

   /*****************New code to imp new abstract QR*****************/
   public String AbstractQRHash(){
       return QRCodeAbstractType.getHash();

   }

    public String getName(){
         return QRCodeAbstractType.getName();
    }

    public Integer getPointsInt(){
         return QRCodeAbstractType.getPointsInt();
    }

    public String getURL(){
         return QRCodeAbstractType.getURL();
    }

    public AbstractQR getAbstractQR(){
         return QRCodeAbstractType;
    }


}







