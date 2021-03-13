package com.sqlite.tutorial.saf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import com.sqlite.tutorial.constants.RequestCodeConstants;
import com.sqlite.tutorial.utilities.LogcatUtils;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SAFUtils {

    public static final String TAG = SAFUtils.class.getSimpleName();

    public static final String SAF_SHARED_PREFERENCES_FILE_NAME     = "SafPreferenceFile";
    public static final String ALLOW_DIRECTORY                      = "allow_directory";

    private SAFUtils() {
        throw new UnsupportedOperationException("Should not create instance of Util class. Please use as static..");
    }

    /**
     * Open File using Intent.ACTION_GET_CONTENT  ( Added in API level 1 )
     *
     * @param currentActivity
     * @param mimeType
     *                              e.g.,   "image/*",
     *                                      "video/*",
     *                                      "text/plain" or "text/*",
     *                                      "application/pdf"
     * @param readRequestCode
     *
     * Example : SAFUtils.openFileUsingActionGetContent(MainActivity.this, MimeUtils.guessMimeTypeFromExtension("txt"),true, RequestConstants.READ_REQUEST_CODE);
     */
    public static void openFileUsingActionGetContent(Activity currentActivity,
                                                     String mimeType,
                                                     boolean showCloudStorage,
                                                     int readRequestCode) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);

        /* The MIME data type filter */
        intent.setType(mimeType);

        /*
         * Used to indicate that an intent only wants URIs that can be opened with
         * {@link ContentResolver#openFileDescriptor(Uri, String)}. Openable URIs
         * must support at least the columns defined in {@link OpenableColumns} when
         * queried.
         *
         * Only return URIs that can be opened with ContentResolver
         *
         *  ( Added in API level 1 )
         */
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        /**
         * Extra used to indicate that an intent should only return data that is on
         * the local device. This is a boolean extra; the default is false. If true,
         * an implementation should only allow the user to select data that is
         * already on the device, not requiring it be downloaded from a remote
         * service when opened.
         *
         * @see #ACTION_GET_CONTENT
         * @see #ACTION_OPEN_DOCUMENT
         * @see #ACTION_OPEN_DOCUMENT_TREE
         * @see #ACTION_CREATE_DOCUMENT
         *
         * Disable Google Drive, Box, OneDrive, Dropbox, if it is true
         *
         *  ( Added in API level 11 )
         */
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        currentActivity.startActivityForResult(Intent.createChooser(intent, "Select a file"), readRequestCode);
    }

    /**
     * Open File using Intent.ACTION_OPEN_DOCUMENT ( Added in API level 19 )
     *
     * @param currentActivity
     * @param mimeType
     *                              e.g.,   "image/*",
     *                                      "video/*",
     *                                      "text/plain" or "text/*",
     *                                      "application/pdf"
     * @param readRequestCode
     *
     * Example : SAFUtils.openFileUsingActionOpenDocument(MainActivity.this, MimeUtils.guessMimeTypeFromExtension("txt"),true, RequestConstants.READ_REQUEST_CODE);
     */
    public static void openFileUsingActionOpenDocument(Activity currentActivity,
                                                       String mimeType,
                                                       boolean showCloudStorage,
                                                       int readRequestCode) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        currentActivity.startActivityForResult(intent, readRequestCode);
    }

    /**
     * Create File using Intent.ACTION_CREATE_DOCUMENT ( Added in API level 19 )
     *
     * @param currentActivity
     * @param mimeType
     *                              e.g.,   "image/*",
     *                                      "video/*",
     *                                      "text/plain" or "text/*",
     *                                      "application/pdf"
     * @param fileNameWithExtension
     *                              e.g.,
     *                                      "Untitled.txt",
     *                                      "Untitled.png"
     * @param createRequestCode
     *
     * Example : SAFUtils.createFile(MainActivity.this, MimeUtils.guessMimeTypeFromExtension("txt"),"Untitled.txt",true, RequestConstants.CREATE_REQUEST_CODE);
     */
    public static void createFile(Activity currentActivity,
                                  String mimeType,
                                  String fileNameWithExtension,
                                  boolean showCloudStorage,
                                  int createRequestCode) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileNameWithExtension);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        currentActivity.startActivityForResult(intent, createRequestCode);
    }

    /**
     * Open File for update
     *
     * @param currentActivity
     * @param mimeType
     *                              e.g.,   "image/*",
     *                                      "video/*",
     *                                      "text/plain" or "text/*",
     *                                      "application/pdf"
     * @param updateRequestCode
     *
     * Example : SAFUtils.openFileForUpdate(MainActivity.this, MimeUtils.guessMimeTypeFromExtension("txt"),true, RequestConstants.UPDATE_REQUEST_CODE);
     */
    public static void openFileForUpdate(Activity currentActivity,
                                         String mimeType,
                                         boolean showCloudStorage,
                                         int updateRequestCode) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        currentActivity.startActivityForResult(intent, updateRequestCode);
    }

    /**
     * Open File for delete
     *
     * @param currentActivity
     * @param mimeType
     *                              e.g.,   "image/*",
     *                                      "video/*",
     *                                      "text/plain" or "text/*",
     *                                      "application/pdf"
     * @param deleteRequestCode
     *
     * Example : SAFUtils.openFileForDelete(MainActivity.this, MimeUtils.guessMimeTypeFromExtension("txt"),true, RequestConstants.DELETE_REQUEST_CODE);
     */
    public static void openFileForDelete(Activity currentActivity,
                                         String mimeType,
                                         boolean showCloudStorage,
                                         int deleteRequestCode) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        currentActivity.startActivityForResult(intent, deleteRequestCode);
    }

    /**
     * Select Folder
     *
     * @param currentActivity
     * @param selectFolderRequestCode
     *
     * Example : SAFUtils.selectFolder(MainActivity.this, RequestConstants.SELECT_FOLDER_REQUEST_CODE);
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void selectFolder(Activity currentActivity, int selectFolderRequestCode) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
        currentActivity.startActivityForResult(intent, selectFolderRequestCode);
    }

    /**
     * Get root directory
     *
     * @param context
     * @param uri
     */
    public static DocumentFile getRootDirectory(Context context, Uri uri) {
        DocumentFile root = DocumentFile.fromTreeUri(context, uri);
        LogcatUtils.informationMessage(TAG, "Uri: " + root.getUri() + "\n" + "Name: " + root.getName());
        return root;
    }

    /**
     * Create directory inside root directory
     *
     * @param rootDirectory
     * @param directoryName
     */
    public static DocumentFile createDirectory(DocumentFile rootDirectory, String directoryName) {
        DocumentFile directory = rootDirectory.createDirectory(directoryName);
        return directory;
    }

    /**
     * Create file
     *
     * @param directoryName
     * @param mimeType
     * @param displayName
     */
    public static DocumentFile createFile(DocumentFile directoryName, String mimeType, String displayName) {
        DocumentFile file = directoryName.createFile(mimeType,displayName);
        return file;
    }

    /**
     * Get file uri
     *
     * @param file
     */
    public static Uri getUri(DocumentFile file) {
        Uri uri = file.getUri();
        return uri;
    }

    /**
     * Perform Write Operation
     *
     * @param context
     * @param uri
     * @param data
     *
     * Note that the file descriptor is opened in "w" mode. This indicates that the file is to be opened for write.
     *
     * Example : SAFUtils.performWriteOperation(getApplicationContext(),uri,"data");
     */
    public static boolean performWriteOperation(Context context, Uri uri, String data) {

        boolean writeStatus;

        FileOutputStream fileOutputStream = null;
        ParcelFileDescriptor parcelFileDescriptor = null;

        try
        {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "w");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            fileOutputStream = new FileOutputStream(fileDescriptor);
            fileOutputStream.write(data.getBytes());

            fileOutputStream.flush();

            writeStatus = true;
        }
        catch (IOException iOException)
        {
            LogcatUtils.debuggingMessage(TAG, "Could not write file 1 : " + iOException.getMessage());
            writeStatus = false;
        }
        finally
        {
            if (fileOutputStream  != null)
            {
                try
                {
                    fileOutputStream .close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "Could not write file 2 : " + iOException.getMessage());
                }
            }

            if (parcelFileDescriptor  != null)
            {
                try
                {
                    parcelFileDescriptor.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "Could not write file 3 : " + iOException.getMessage());
                }
            }
        }
        return writeStatus;
    }

    /**
     * Perform Read Operation
     *
     * @param context
     * @param uri
     *
     * Note that the file descriptor is opened in "r" mode. This indicates that the file is to be opened for read.
     *
     * Example : SAFUtils.performReadOperation(getApplicationContext(),uri);
     */
    public static StringBuilder performReadOperation(Context context, Uri uri) {

        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;

        try
        {
            inputStream = context.getContentResolver().openInputStream(uri);
            assert inputStream != null;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String readLine;
            while ((readLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(readLine);
            }
        }
        catch (IOException iOException)
        {
            LogcatUtils.debuggingMessage(TAG, "Could not read file 1 : " + iOException.getMessage());
            stringBuilder = null;
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "Could not read file 2 : " + iOException.getMessage());
                }
            }

            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "Could not read file 3 : " + iOException.getMessage());
                }
            }
        }
        return stringBuilder;
    }

    /**
     * Perform Update Operation
     *
     * @param context
     * @param uri
     * @param data
     *
     * Note that the file descriptor is opened in "w" mode. This indicates that the file is to be opened for write.
     *
     * Example : SAFUtils.performUpdateOperation(getApplicationContext(),uri,"data");
     */
    public static boolean performUpdateOperation(Context context, Uri uri, String data) {

        boolean updateStatus;

        FileOutputStream fileOutputStream = null;
        ParcelFileDescriptor parcelFileDescriptor = null;

        try
        {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "w");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            fileOutputStream = new FileOutputStream(fileDescriptor);
            fileOutputStream.write(data.getBytes());

            fileOutputStream.flush();

            updateStatus = true;
        }
        catch (IOException iOException)
        {
            LogcatUtils.debuggingMessage(TAG, "Could not update file 1 : " + iOException.getMessage());
            updateStatus = false;
        }
        finally
        {
            if (fileOutputStream  != null)
            {
                try
                {
                    fileOutputStream .close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "Could not update file 2 : " + iOException.getMessage());
                }
            }

            if (parcelFileDescriptor  != null)
            {
                try
                {
                    parcelFileDescriptor.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "Could not update file 3 : " + iOException.getMessage());
                }
            }
        }
        return updateStatus;
    }

    /**
     * Perform Delete Operation
     *
     * @param context
     * @param uri
     *
     * Example : SAFUtils.performDeleteOperation(getApplicationContext(),uri);
     */
    public static boolean performDeleteOperation(Context context, Uri uri) {

        boolean deleteStatus;

        try
        {
            deleteStatus = DocumentsContract.deleteDocument(context.getContentResolver(), uri);
        }
        catch (FileNotFoundException fileNotFoundException)
        {
            deleteStatus = false;
            LogcatUtils.debuggingMessage(TAG, "Could not delete file 1 : " + fileNotFoundException.getMessage());
        }

        return deleteStatus;
    }

    /**
     * Get bitmap from uri
     *
     * @param context
     * @param uri
     *
     * Example : SAFUtils.getBitmapFromUri(getApplicationContext(),uri);
     *
     * An image file can be assigned to a Bitmap object by extracting the file descriptor from the Uri object and then decoding the image into a BitmapFactory instance.
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {

        Bitmap bitmap = null;
        ParcelFileDescriptor parcelFileDescriptor = null;

        try
        {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        }
        catch (IOException iOException)
        {
            LogcatUtils.debuggingMessage(TAG, "Could not get bitmap 1 : " + iOException.getMessage());
        }
        finally
        {
            if (parcelFileDescriptor  != null)
            {
                try
                {
                    parcelFileDescriptor.close();
                }
                catch (IOException iOException)
                {
                    LogcatUtils.debuggingMessage(TAG, "Could not get bitmap 2 : " + iOException.getMessage());
                }
            }
        }
        return bitmap;
    }

    /**
     * Get file details from uri
     *
     * @param context
     * @param uri
     *
     * Example : SAFUtils.getFileDetailsFromUri(getApplicationContext(),uri);
     */
    public static void getFileDetailsFromUri(Context context, Uri uri) {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null);

        try
        {
            /* moveToFirst() returns false if the cursor has 0 rows. */
            if(cursor != null && cursor.moveToFirst())
            {
                String[] columnNames = cursor.getColumnNames();
                for (String columnName : columnNames)
                {
                    LogcatUtils.informationMessage(TAG, "Column Name : "+ columnName);
                }

                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                LogcatUtils.errorMessage(TAG, "Display Name : "+ displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                /*
                 * If the size is unknown, the value stored is null. But since an
                 * int can't be null in Java, the behavior is implementation-specific,
                 * which is just a fancy term for "unpredictable". So as a rule,
                 * check if it's null before assigning to an int. This will
                 * happen often :  The storage API allows for remote files, whose
                 * size might not be locally known.
                 */
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    /*
                     * Technically the column stores an int, but cursor.getString()
                     * will do the conversion automatically.
                     */
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                LogcatUtils.errorMessage(TAG, "Size: " + size);
            }
        }
        finally
        {
            assert cursor != null;
            cursor.close();
        }
    }

    /**
     * Get DocumentFile that allow read/write access permission
     *
     * @param context
     * @param currentActivity
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static DocumentFile takeRootDirectoryWithPermission(Context context, Activity currentActivity) {

        DocumentFile documentFile = null;

        SharedPreferences sharedPreferences = context.getSharedPreferences(SAF_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String allowDirectory = sharedPreferences.getString(ALLOW_DIRECTORY,"");

        if (TextUtils.isEmpty(allowDirectory))
        {
            selectFolder(currentActivity, RequestCodeConstants.SELECT_FOLDER_REQUEST_CODE);
        }
        else
        {
            try
            {
                Uri allowUri = Uri.parse(allowDirectory);

                final int takeFlags = currentActivity.getIntent().getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                context.getContentResolver().takePersistableUriPermission(allowUri, takeFlags);

                documentFile = SAFUtils.getRootDirectory(context,allowUri);
            }
            catch (SecurityException securityException)
            {
                LogcatUtils.debuggingMessage(TAG, "Take : "+ securityException.getMessage());
                SAFUtils.selectFolder(currentActivity, RequestCodeConstants.SELECT_FOLDER_REQUEST_CODE);
            }
        }

        return documentFile;
    }

    /**
     * Release read/write access permission
     *
     * @param context
     * @param currentActivity
     */
    public static void releaseRootDirectoryWithPermission(Context context, Activity currentActivity) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SAF_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        String allowDirectory = sharedPreferences.getString(ALLOW_DIRECTORY,"");

        if (!TextUtils.isEmpty(allowDirectory))
        {
            try
            {
                Uri allowUri = Uri.parse(allowDirectory);

                final int releaseFlags = currentActivity.getIntent().getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                context.getContentResolver().releasePersistableUriPermission(allowUri, releaseFlags);

                /* or */
                currentActivity.revokeUriPermission(allowUri, releaseFlags);

                /* Restart will take effect, so you can clear allowUri */
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putString(ALLOW_DIRECTORY, "");
                sharedPreferencesEditor.apply();
            }
            catch (SecurityException securityException)
            {
                LogcatUtils.debuggingMessage(TAG, "Release : "+ securityException.getMessage());
            }
        }
    }
}
