package com.example.omnia.easytripplanner.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * convert from bitmap to byte and vice versa
 * Created by EslamWaheed on 3/12/2018.
 */

public class Converter {

    /**
     * convert bitmap to byte
     *
     * @param bitmap
     * @return byte[]
     */
    public static byte[] convertBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * convert byte to bitmap
     *
     * @param bytes
     * @return Bitmap
     */
    public static Bitmap convertByteToBitmap(byte[] bytes) {
        ByteArrayInputStream imageStream = new ByteArrayInputStream(bytes);
        return BitmapFactory.decodeStream(imageStream);
    }
}
