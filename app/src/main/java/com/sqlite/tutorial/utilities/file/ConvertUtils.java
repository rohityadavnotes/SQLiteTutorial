package com.sqlite.tutorial.utilities.file;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * byteArr to hexString
     *
     * For example :
     *              bytes2HexString(new byte[] {0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes byte array
     * @return hexadecimal uppercase string
     */
    public static String bytes2HexString(byte[] bytes) {
        char[] ret = new char[bytes.length << 1];
        for (int i = 0, j = 0; i < bytes.length; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * hexString to byteArr
     *
     * For example :
     *              hexString2Bytes("00A8") returns {0, (byte) 0xA8}
     *
     * @param hexString hexadecimal string
     * @return byte array
     */
    public static byte[] hexString2Bytes(String hexString) {
        int len = hexString.length() + 1;
        String evenHex = len % 2 != 0 ? hexString : ("0" + hexString);
        char[] hexBytes = evenHex.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * hexChar to int
     *
     * @param hexChar hex single byte
     * @return 0..15
     */
    private static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * charArr to byteArr
     *
     * @param chars character array
     * @return byte array
     */
    public static byte[] chars2Bytes(char[] chars) {
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * byteArr to charArr
     *
     * @param bytes byte array
     * @return character array
     */
    public static char[] bytes2Chars(byte[] bytes) {
        int len = bytes.length;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * Bytes to size in unit
     *
     * @param byteNum number of bytes
     * @param unit <ul>
     * <li>{@link MemoryUtils.MemoryUnit#BYTE}: bytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#KB}: kilobytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#MB}: Mega </li>
     * <li>{@link MemoryUtils.MemoryUnit#GB}: GB</li>
     * </ul>
     * @return size in unit
     */
    public static double byte2Size(long byteNum, MemoryUtils.MemoryUnit unit) {
        if (byteNum < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return (double) byteNum / MemoryUtils.BYTE;
            case KB:
                return (double) byteNum / MemoryUtils.KB;
            case MB:
                return (double) byteNum / MemoryUtils.MB;
            case GB:
                return (double) byteNum / MemoryUtils.GB;
        }
    }

    /**
     * The number of bytes converted to size in unit
     *
     * @param size
     * @param unit <ul>
     * <li>{@link MemoryUtils.MemoryUnit#BYTE}: bytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#KB}: kilobytes</li>
     * <li>{@link MemoryUtils.MemoryUnit#MB}: Mega </li>
     * <li>{@link MemoryUtils.MemoryUnit#GB}: GB</li>
     * </ul>
     * @return bytes
     */
    public static long size2Byte(long size, MemoryUtils.MemoryUnit unit) {
        if (size < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return size * MemoryUtils.BYTE;
            case KB:
                return size * MemoryUtils.KB;
            case MB:
                return size * MemoryUtils.MB;
            case GB:
                return size * MemoryUtils.GB;
        }
    }

    /**
     * inputStream to outputStream
     *
     * @param is input stream
     * @return outputStream subclass
     */
    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) return null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[MemoryUtils.KB];
            int len;
            while ((len = is.read(b, 0, MemoryUtils.KB)) != -1) {
                os.write(b, 0, len);
            }
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            FileUtils.closeIO(is);
        }
    }

    /**
     * outputStream to inputStream
     *
     * @param out output stream
     * @return inputStream subclass
     */
    public ByteArrayInputStream output2InputStream(OutputStream out) {
        if (out == null) return null;
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
    }

    /**
     * inputStream to byteArr
     *
     * @param is input stream
     * @return byte array
     */
    public static byte[] inputStream2Bytes(InputStream is) {
        return input2OutputStream(is).toByteArray();
    }

    /**
     * byteArr to inputStream
     *
     * @param bytes byte array
     * @return input stream
     */
    public static InputStream bytes2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * outputStream to byteArr
     *
     * @param out output stream
     * @return byte array
     */
    public static byte[] outputStream2Bytes(OutputStream out) {
        if (out == null) return null;
        return ((ByteArrayOutputStream) out).toByteArray();
    }

    /**
     * outputStream to byteArr
     *
     * @param bytes byte array
     * @return byte array
     */
    public static OutputStream bytes2OutputStream(byte[] bytes) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            os.write(bytes);
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            FileUtils.closeIO(os);
        }
    }

    /**
     * inputStream to string according to encoding
     *
     * @param is input stream
     * @param charsetName encoding format
     * @return string
     */
    public static String inputStream2String(InputStream is, String charsetName) {
        if (is == null || TextUtils.isEmpty(charsetName)) return null;
        try {
            return new String(inputStream2Bytes(is), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string to inputStream according to encoding
     *
     * @param string string
     * @param charsetName encoding format
     * @return input stream
     */
    public static InputStream string2InputStream(String string, String charsetName) {
        if (string == null || TextUtils.isEmpty(charsetName)) return null;
        try {
            return new ByteArrayInputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * outputStream to string according to encoding
     *
     * @param out output stream
     * @param charsetName encoding format
     * @return string
     */
    public static String outputStream2String(OutputStream out, String charsetName) {
        if (out == null) return null;
        try {
            return new String(outputStream2Bytes(out), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string to outputStream according to encoding
     *
     * @param string string
     * @param charsetName encoding format
     * @return input stream
     */
    public static OutputStream string2OutputStream(String string, String charsetName) {
        if (string == null || TextUtils.isEmpty(charsetName)) return null;
        try {
            return bytes2OutputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap to byteArr
     *
     * @param bitmap bitmap object
     * @param format format
     * @return byte array
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byteArr to bitmap
     *
     * @param bytes byte array
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Drawable to bitmap
     *
     * @param drawable drawable object
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * bitmap to drawable
     *
     * @param res resources object
     * @param bitmap bitmap object
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    /**
     * drawable to byteArr
     *
     * @param drawable drawable object
     * @param format format
     * @return byte array
     */
    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
        return bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * byteArr to drawable
     *
     * @param res resources object
     * @param bytes byte array
     * @return drawable
     */
    public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
        return bitmap2Drawable(res, bytes2Bitmap(bytes));
    }

    /**
     * View to Bitmap
     *
     * @param view view
     * @return bitmap
     */
    public static Bitmap view2Bitmap(View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }
}