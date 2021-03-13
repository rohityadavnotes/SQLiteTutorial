package com.sqlite.tutorial.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.sqlite.tutorial.utilities.LogcatUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class SQLiteImporterExporter {

    private static final String TAG = SQLiteImporterExporter.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static SQLiteImporterExporter instance;

    public static SQLiteImporterExporter getInstance(Context context, String DB_NAME) {
        if (instance == null) {
            synchronized (SQLiteImporterExporter.class) {
                if (instance == null) {
                    instance = new SQLiteImporterExporter(context, DB_NAME);
                }
            }
        }
        return instance;
    }

    private Context context;

    public SQLiteDatabaseHelper sqLiteDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private String DATABASE_PATH;
    private String DATABASE_NAME;

    public ImportListener importListener;
    public ExportListener exportListener;

    @SuppressLint("SdCardPath")
    public SQLiteImporterExporter(Context context, String databaseName) {
        this.context                = context;
        this.sqLiteDatabaseHelper   = new SQLiteDatabaseHelper(context);
        this.DATABASE_PATH          = "/data/data/" + context.getPackageName() + "/databases/";
        this.DATABASE_NAME          = databaseName;
    }

    public void open(){
        sqLiteDatabase = sqLiteDatabaseHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteDatabase.close();
    }

    public void setOnImportListener(ImportListener importListener) {
        this.importListener = importListener;
    }

    public void setOnExportListener(ExportListener exportListener) {
        this.exportListener = exportListener;
    }

    public boolean isDataBaseExists() {
        File databaseFile = new File(DATABASE_PATH + DATABASE_NAME);
        return databaseFile.exists();
    }

    public void importDataBaseFromAssets() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try
        {
            this.open();

            inputStream = context.getAssets().open("databases/"+DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
            {
                outputStream.write(buffer, 0, length);
            }

            if (importListener != null)
                importListener.onSuccess("Successfully Imported");
        }
        catch (Exception e) {
            if (importListener != null)
                importListener.onFailure(e);
        }
        finally
        {
            try
            {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                this.close();
            }
            catch (IOException iOException)
            {
                if (importListener != null)
                    importListener.onFailure(iOException);
            }
        }
    }

    /**
     * Import SQLite file on database folder
     *
     * @param sourceUri
     *            - here currently SQLite file exist, Existing DB Path
     *            - e.g., Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
     */
    public void importDataBase(Uri sourceUri) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        ParcelFileDescriptor parcelFileDescriptor;
        try
        {
            this.open();

            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(sourceUri, "r");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            inputStream = new FileInputStream(fileDescriptor);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            if (importListener != null)
                importListener.onSuccess("Successfully Imported");
        }
        catch (Exception e)
        {
            if (importListener != null)
                importListener.onFailure(e);
        }
        finally {
            try
            {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                this.close();
            }
            catch (IOException iOException)
            {
                if (importListener != null)
                    importListener.onFailure(iOException);
            }
        }
    }

    /**
     * Export SQLite file on database folder
     *
     * @param sourceUri
     *            - where to export, Copy DB Path
     *            - e.g., Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
     */
    public void exportDataBase(Uri sourceUri) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        ParcelFileDescriptor parcelFileDescriptor;
        try
        {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(sourceUri, "w");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

            String inFileName = DATABASE_PATH + DATABASE_NAME;
            inputStream = new FileInputStream(inFileName);
            //String outFileName = destinationPath + "/"+ DATABASE_NAME;
            outputStream = new FileOutputStream(fileDescriptor);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            if (exportListener != null)
                exportListener.onSuccess("Successfully Exported");
        }
        catch (Exception e)
        {
            if (exportListener != null)
                exportListener.onFailure(e);
        }
        finally
        {
            try
            {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
            catch (Exception ex)
            {
                if (exportListener != null)
                    exportListener.onFailure(ex);
            }
        }
    }

    public interface ImportListener {
        void onSuccess(String message);
        void onFailure(Exception exception);
    }

    public interface ExportListener {
        void onSuccess(String message);
        void onFailure(Exception exception);
    }

    /**
     * Import SQLite file on database folder
     *
     * @param context
     * @param databaseNameWithExtension e.g., database.sqlite
     * @param sourcePath
     *            - here currently SQLite file exist, Existing DB Path
     *            - e.g., Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
     *
     * @param destinationPath
     *            - where to export, Copy DB Path
     *            - e.g., "/data/data/{package_name}/databases/"
     *
     *            - EXAMPLE : "/data/data/" + context.getPackageName() + "/databases/";
     */
    public void importSQLite(Context context, String databaseNameWithExtension, String sourcePath, String destinationPath) throws IOException {

        File sourceFile         = new File(sourcePath, databaseNameWithExtension);
        File destinationFile    = new File(destinationPath, databaseNameWithExtension);

        LogcatUtils.informationMessage(TAG, "SOURCE : "+sourceFile.toString());
        LogcatUtils.informationMessage(TAG, "DESTINATION : "+destinationFile.toString());

        if(sourceFile.exists())
        {
            FileInputStream sourceFileInputStream           = new FileInputStream(sourceFile);
            FileOutputStream destinationFileOutputStream    = new FileOutputStream(destinationFile);

            copy(sourceFileInputStream, destinationFileOutputStream);
        }
    }

    /**
     * Export SQLite file on specific folder
     *
     * @param context
     * @param databaseNameWithExtension e.g., database.sqlite
     * @param sourcePath
     *            - here currently SQLite file exist, Existing DB Path
     *            - e.g., "/data/data/{package_name}/databases/"
     *
     *            - EXAMPLE : "/data/data/" + context.getPackageName() + "/databases/";
     *
     * @param destinationPath
     *            - where to export, Copy DB Path
     *            - e.g., Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
     */
    public void exportSQLite(Context context, String databaseNameWithExtension, String sourcePath, String destinationPath) throws IOException {

        File sourceFile         = new File(sourcePath, databaseNameWithExtension);
        File destinationFile    = new File(destinationPath, databaseNameWithExtension);

        LogcatUtils.informationMessage(TAG, "SOURCE : "+sourceFile.toString());
        LogcatUtils.informationMessage(TAG, "DESTINATION : "+destinationFile.toString());

        if(sourceFile.exists())
        {
            FileInputStream sourceFileInputStream           = new FileInputStream(sourceFile);
            FileOutputStream destinationFileOutputStream    = new FileOutputStream(destinationFile);

            copy(sourceFileInputStream, destinationFileOutputStream);
        }
    }

    public void copy(FileInputStream sourceFileInputStream, FileOutputStream destinationFileOutputStream) throws IOException {
        FileChannel sourceChannel       = null;
        FileChannel destinationChannel  = null;

        try
        {
            sourceChannel       = sourceFileInputStream.getChannel();
            destinationChannel  = destinationFileOutputStream.getChannel();

            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
        finally
        {
            try
            {
                if (sourceChannel != null)
                {
                    sourceChannel.close();
                }
            }
            finally
            {
                if (destinationChannel != null)
                {
                    destinationChannel.close();
                }
            }
        }
    }
}
