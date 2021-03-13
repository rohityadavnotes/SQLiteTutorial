package com.sqlite.tutorial.utilities.file;

import android.content.Context;
import android.net.Uri;
import androidx.core.content.FileProvider;
import com.intuit.sdp.BuildConfig;
import java.io.File;

public class FileProviderUtils {

    private FileProviderUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    public static Uri getFileUri(Context context, File file) {
        /**
         * Note: We are using getUriForFile(Context, String, File) which returns a content:// URI.
         * For more recent apps targeting Android 7.0 (API level 24) and higher, passing a file:// URI
         * across a package boundary causes a FileUriExposedException.
         * Therefore, we now present a more generic way of storing images using a FileProvider.
         */
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
    }
}
