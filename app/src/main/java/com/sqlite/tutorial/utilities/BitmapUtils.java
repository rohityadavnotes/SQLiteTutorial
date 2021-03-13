package com.sqlite.tutorial.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapUtils {

    private BitmapUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * Convert bitmap to Byte
     *
     * @param bitmap source Bitmap
     * @return Byte array
     */
    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, byteArrayOutputStream);
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); /* Quality compression method, here 100 means no compression, store the compressed data in the BIOS */
        byte[] photoByteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return photoByteArray;
    }

    /**
     * Get a bitmap of a specified size
     *
     * @param bytes Byte array
     * @return Bitmap
     */
    public static Bitmap getBitmapFromByteArray(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     * Get the number of bytes of the bitmap.
     *
     * @param bitmap
     * @return The number of bytes of the bitmap.
     */
    private static int byteSizeOfBitmap(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getAllocationByteCount();
        }
    }

    /**
     * Calculate an inSampleSize for use in a {@link android.graphics.BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link android.graphics.BitmapFactory}. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and height.
     *
     * @param options An options object
     * @param requiredWidth The requested width of the resulting bitmap
     * @param requiredHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        final int height    = options.outHeight;
        final int width     = options.outWidth;

        int inSampleSize    = 1;

        if (height > requiredHeight || width > requiredWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                /*
                * Calculate the largest inSampleSize value that is a power of 2 and keeps both
                * height and width larger than the requested height and width.
                */
                while ((halfHeight / inSampleSize) > requiredHeight && (halfWidth / inSampleSize) > requiredWidth) {
                    inSampleSize *= 2;
                }

            return inSampleSize;
        }
        return inSampleSize;
    }

    /**
     * This method is used for convert uri to bitmap
     *
     * @param context       The current context
     * @param selectedImageUri The Image URI
     * @param requiredWidth example : int MAX_WIDTH = 1024;
     * @param requiredHeight example : int MAX_HEIGHT = 1024;
     *
     * @return Bitmap image results
     * @throws IOException
     */
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImageUri, int requiredWidth, int requiredHeight) throws IOException {

        int MAX_WIDTH   = requiredWidth;
        int MAX_HEIGHT  = requiredHeight;

        /* Set Default Required Size */
        if (MAX_WIDTH == 0)
        {
            MAX_WIDTH = 200;
        }

        if (MAX_HEIGHT == 0)
        {
            MAX_HEIGHT = 200;
        }

        /* First decode with inJustDecodeBounds = true to check dimensions */
        BitmapFactory.Options optionsOne = new BitmapFactory.Options();
        optionsOne.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri), null, optionsOne);

        BitmapFactory.Options optionsTwo = new BitmapFactory.Options();
        optionsTwo.inSampleSize = calculateInSampleSize(optionsOne, MAX_WIDTH, MAX_HEIGHT);

        return rotateImage(BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri), null, optionsTwo), 90);
    }

    public static Bitmap rotateImage(Bitmap bitmap, int degree) {
        Bitmap rotatedBitmap = bitmap;

        /* Getting width & height of the given image. */
        int w = rotatedBitmap.getWidth();
        int h = rotatedBitmap.getHeight();

        if (w > h) {
            Matrix matrix = new Matrix();
            /* Setting post rotate to 90 */
            matrix.postRotate(degree);
            rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, w, h, matrix, true);
        }
        return rotatedBitmap;
    }
}