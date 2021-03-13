package com.sqlite.tutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.sqlite.tutorial.baseadapter.listener.OnRecyclerViewItemChildClick;
import com.sqlite.tutorial.baseadapter.listener.OnRecyclerViewItemClick;
import com.sqlite.tutorial.constants.RequestCodeConstants;
import com.sqlite.tutorial.permission.PermissionGroups;
import com.sqlite.tutorial.permission.helper.ActivityPermissionHelper;
import com.sqlite.tutorial.saf.SAFUtils;
import com.sqlite.tutorial.sqlite.SQLiteDatabaseConstants;
import com.sqlite.tutorial.sqlite.SQLiteDatabaseOperation;
import com.sqlite.tutorial.sqlite.SQLiteImporterExporter;
import com.sqlite.tutorial.sqlite.model.Student;
import com.sqlite.tutorial.utilities.ActivityUtils;
import com.sqlite.tutorial.utilities.LayoutManagerUtils;
import com.sqlite.tutorial.utilities.LogcatUtils;
import com.sqlite.tutorial.utilities.file.FileUtils;
import java.util.ArrayList;

public class SQLiteActivity extends AppCompatActivity {

    public static final String TAG = SQLiteActivity.class.getSimpleName();

    private Toolbar toolbar;
    private SearchView searchView;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageView notFoundImageView;

    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Student> studentArrayList;

    private ActivityPermissionHelper activityPermissionHelper;

    private SQLiteDatabaseOperation sqLiteDatabaseOperation;
    private SQLiteImporterExporter sqLiteImporterExporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_lite);

        initializeView();
        setSupportActionBar(toolbar);
        initializeObject();
        onTextChangedListener();
        initializeEvent();
    }

    protected void initializeView() {
        toolbar             = findViewById(R.id.toolbar);
        recyclerView        = findViewById(R.id.recyclerView);
        progressBar         = findViewById(R.id.progressBar);
        notFoundImageView   = findViewById(R.id.notFoundImageView);
    }

    protected void initializeObject() {
        activityPermissionHelper    = new ActivityPermissionHelper(this,getApplicationContext());

        sqLiteDatabaseOperation     = SQLiteDatabaseOperation.getInstance(getApplicationContext());
        sqLiteImporterExporter      = new SQLiteImporterExporter(getApplicationContext(), SQLiteDatabaseConstants.SQLite_DATABASE_NAME);
        studentArrayList            = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(LayoutManagerUtils.getLinearLayoutManagerVertical(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerViewAdapter.addArrayList(studentArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

        readFirstWay();
    }

    protected void onTextChangedListener() {
        sqLiteImporterExporter.setOnImportListener(new SQLiteImporterExporter.ImportListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sqLiteImporterExporter.setOnExportListener(new SQLiteImporterExporter.ExportListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void initializeEvent() {
        recyclerViewAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick<Student>() {
            @Override
            public void OnItemClick(View itemView, Student student, int position) {
            }
        });

        recyclerViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Student>() {
            @Override
            public void OnItemChildClick(View viewChild, Student student, int position) {
                switch (viewChild.getId()) {
                    case R.id.updateButtonTextView:
                        Bundle bundle = new Bundle();
                        bundle.putString("roll_number", student.getRollNumber());
                        ActivityUtils.launchActivityWithBundle(SQLiteActivity.this, UpdateActivity.class, bundle);
                        break;
                    case R.id.deleteButtonTextView:
                        AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteActivity.this);
                        builder.setMessage("Are you sure you want to delete ? : "+student.getFirstName())
                                .setCancelable(false)
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        boolean isDelete = deleteFirstWay(student.getRollNumber());
                                        if(isDelete)
                                        {
                                            recyclerViewAdapter.removeSingleItem(student);

                                            Toast.makeText(getApplicationContext(), "Student detail deleted successfully", Toast.LENGTH_LONG).show();
                                            dialog.cancel();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Student detail not deleted. Something wrong!",Toast.LENGTH_LONG).show();
                                            dialog.cancel();
                                        }
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * Callback received when a permissions request has been completed.
     * This method will be called when the user will tap on allow or deny
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case PermissionGroups.STORAGE_GROUP_REQUEST_CODE :
                /*
                 * If request is cancelled, the grantResults arrays are empty.
                 */
                if(activityPermissionHelper.verifyPermission(grantResults))
                {
                    /*
                     * Permission is granted. Continue the action or workflow in your app.
                     */
                    openFileManager(SQLiteActivity.this,
                            "*/*",
                            true,
                            false,
                            RequestCodeConstants.SELECT_FILE_REQUEST_CODE);
                }
                else
                {
                    /*
                     * Explain to the user that the feature is unavailable because the features requires a permission that the user has denied.
                     * At the same time, respect the user's decision. Don't link to system settings in an effort to convince the user to change their decision.
                     */
                    activityPermissionHelper.shouldShowRationale(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            PermissionGroups.STORAGE_GROUP_REQUEST_CODE,
                            R.drawable.permission_ic_storage,
                            "Storage Permission",
                            "Kindly allow Storage Permission, without this permission the app is unable to provide file read write feature.",
                            "Kindly allow Storage Permission from Settings, without this permission the app is unable to provide file read write feature."+"\n\n"+"Please turn on permissions at [Setting] -> [Permissions]"
                    );
                }
                break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if(resultCode == Activity.RESULT_OK)
        {
            if (resultData != null)
            {
                Uri uri = resultData.getData();

                if (uri != null)
                {
                    if(RequestCodeConstants.SELECT_FOLDER_REQUEST_CODE == requestCode)
                    {
                        /* Save the obtained directory permissions */
                        final int takeFlags = resultData.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        getContentResolver().takePersistableUriPermission(uri, takeFlags);

                        /* Save uri */
                        SharedPreferences sharedPreferences = getSharedPreferences(SAFUtils.SAF_SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString(SAFUtils.ALLOW_DIRECTORY, uri.toString());
                        sharedPreferencesEditor.apply();

                        DocumentFile rootDirectory   = SAFUtils.getRootDirectory(getApplicationContext(),uri);
                        createFileOwnDirectory(rootDirectory, "exportDirectory");
                    }

                    if(RequestCodeConstants.SELECT_FILE_REQUEST_CODE == requestCode)
                    {
                        importFromSdCard(uri);
                    }
                }
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            LogcatUtils.informationMessage(TAG, "Activity canceled");
        }
        else
        {
            LogcatUtils.informationMessage(TAG, "Something want wrong");
        }

        if (requestCode == PermissionGroups.PERMISSIONS_FROM_SETTING_REQUEST_CODE)
        {
            if(activityPermissionHelper.hasSinglePermission(PermissionGroups.WRITE_EXTERNAL_STORAGE))
            {
                openFileManager(SQLiteActivity.this,
                        "*/*",
                        true,
                        false,
                        RequestCodeConstants.SELECT_FILE_REQUEST_CODE);
            }
            else
            {
                activityPermissionHelper.askSinglePermission(PermissionGroups.WRITE_EXTERNAL_STORAGE, PermissionGroups.STORAGE_GROUP_REQUEST_CODE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_bar_menu_items, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                search(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search)
        {
            return true;
        }
        else if (id == R.id.action_insert)
        {
            ActivityUtils.launchActivity(SQLiteActivity.this, InsertActivity.class);
            return true;
        }
        else if (id == R.id.action_import_from_assets)
        {
            importFromAssets();
            return true;
        }
        else if (id == R.id.action_import_from_sdcard)
        {
            if(activityPermissionHelper.hasSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                openFileManager(SQLiteActivity.this,
                        "*/*",
                        true,
                        false,
                        RequestCodeConstants.SELECT_FILE_REQUEST_CODE);
            }
            else
            {
                activityPermissionHelper.askSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionGroups.STORAGE_GROUP_REQUEST_CODE);
            }
            return true;
        }
        else if (id == R.id.action_export)
        {
            DocumentFile rootDirectory   = SAFUtils.takeRootDirectoryWithPermission(getApplicationContext(),SQLiteActivity.this);
            if(rootDirectory != null)
            {
                createFileOwnDirectory(rootDirectory, "exportDirectory");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /* close search view on back button pressed */
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    public void openFileManager(Activity currentActivity,
                                String mimeType,
                                boolean showCloudStorage,
                                boolean allowMultipleSelect,
                                int readRequestCode) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, !showCloudStorage);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultipleSelect);

        try
        {
            currentActivity.startActivityForResult(Intent.createChooser(intent, "Please select file"), readRequestCode);
        }
        catch (ActivityNotFoundException activityNotFoundException) {
            activityNotFoundException.printStackTrace();
        }
    }

    /*
     ***********************************************************************************************
     ********************************** CRUD OPERATION HELPER METHOD *******************************
     ***********************************************************************************************
     */
    private void readFirstWay() {
        studentArrayList.clear();
        recyclerViewAdapter.clearAllItem();
        studentArrayList = sqLiteDatabaseOperation.getAllRowFirstWay();

        if (studentArrayList.size() == 0)
        {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
            }

            if (notFoundImageView.getVisibility() == View.GONE)
            {
                notFoundImageView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if (recyclerView.getVisibility() == View.GONE) {
                recyclerView.setVisibility(View.VISIBLE);
                if (notFoundImageView.getVisibility() == View.VISIBLE)
                {
                    notFoundImageView.setVisibility(View.GONE);
                }
            }

            recyclerViewAdapter.addArrayList(studentArrayList);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void readSecondWhy() {
        studentArrayList.clear();
        recyclerViewAdapter.clearAllItem();
        studentArrayList = sqLiteDatabaseOperation.getAllRowSecondWay();

        if (studentArrayList.size() == 0)
        {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
            }

            if (notFoundImageView.getVisibility() == View.GONE)
            {
                notFoundImageView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if (recyclerView.getVisibility() == View.GONE) {
                recyclerView.setVisibility(View.VISIBLE);
                if (notFoundImageView.getVisibility() == View.VISIBLE)
                {
                    notFoundImageView.setVisibility(View.GONE);
                }
            }

            recyclerViewAdapter.addArrayList(studentArrayList);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void readSingleFirstWhy(String rollNumber) {
        Student student = sqLiteDatabaseOperation.getSingleRowFirstWay(rollNumber);
        if(student == null)
        {
            System.out.println("Not Found");
        }
        else
        {
            System.out.println(""+student.toString());
        }
    }

    private void readSingleSecondWhy(String rollNumber) {
        Student student = sqLiteDatabaseOperation.getSingleRowSecondWay(rollNumber);

        if(student == null)
        {
            System.out.println("Not Found");
        }
        else
        {
            System.out.println(""+student.toString());
        }
    }

    private boolean deleteFirstWay(String rollNumber) {
        boolean isDelete = sqLiteDatabaseOperation.deleteFirstWay(rollNumber);
        return isDelete;
    }

    private void deleteSecondWay(String rollNumber) {
        sqLiteDatabaseOperation.deleteSecondWay(rollNumber);
    }

    private void delete(int id, String firstName) {
        sqLiteDatabaseOperation.deleteWithMatchTwoField(id, firstName);
    }

    private boolean tableEmptyFirstWhy() {
        boolean isDelete = sqLiteDatabaseOperation.setTableEmptyFirstWay(SQLiteDatabaseConstants.TABLE_1);
        return isDelete;
    }

    private void tableEmptySecondWhy() {
        sqLiteDatabaseOperation.setTableEmptySecondWay(SQLiteDatabaseConstants.TABLE_1);
    }

    private void search(String firstName) {
        studentArrayList.clear();
        recyclerViewAdapter.clearAllItem();

        studentArrayList = sqLiteDatabaseOperation.search(firstName);

        if (studentArrayList.size() == 0)
        {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
            }

            if (notFoundImageView.getVisibility() == View.GONE)
            {
                notFoundImageView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if (recyclerView.getVisibility() == View.GONE) {
                recyclerView.setVisibility(View.VISIBLE);
                if (notFoundImageView.getVisibility() == View.VISIBLE)
                {
                    notFoundImageView.setVisibility(View.GONE);
                }
            }

            recyclerViewAdapter.addArrayList(studentArrayList);
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private int numberOfRowFirstWhy() {
        int numberOfRow = sqLiteDatabaseOperation.getNumberOfRowsFirstWay(SQLiteDatabaseConstants.TABLE_1);;
        return numberOfRow;
    }

    private int numberOfRowSecondWhy() {
        int numberOfRow = sqLiteDatabaseOperation.getNumberOfRowsSecondWay(SQLiteDatabaseConstants.TABLE_1);;
        return numberOfRow;
    }

    private int getLastInsertedRowId() {
        int id = sqLiteDatabaseOperation.findLastId();
        return id;
    }

    /*
     ***********************************************************************************************
     *************************************** IMPORT EXPORT HELPER METHOD ***************************
     ***********************************************************************************************
     */
    private void createFileOwnDirectory(DocumentFile rootDirectory, String childDirectoryName) {
        DocumentFile childDirectory;

        if(rootDirectory.findFile(childDirectoryName) == null)
        {
            childDirectory  = SAFUtils.createDirectory(rootDirectory, childDirectoryName);
        }
        else
        {
            childDirectory = rootDirectory.findFile(childDirectoryName);
        }

        String fileNameWithExtension    = SQLiteDatabaseConstants.SQLite_DATABASE_NAME;
        String mimeType                 = "application/x-sqlite3 ";
        String fileNameWithoutExtension = FileUtils.getFileNameNoExtension(fileNameWithExtension);

        DocumentFile file = SAFUtils.createFile(childDirectory, mimeType, fileNameWithoutExtension);
        Uri SDCardRealPathWhereToStoreDBFile = SAFUtils.getUri(file);
        export(SDCardRealPathWhereToStoreDBFile);
    }

    private void export(Uri SDCardRealPathWhereToStoreDBFile) {
        if (sqLiteImporterExporter.isDataBaseExists())
        {
            try {
                sqLiteImporterExporter.exportDataBase(SDCardRealPathWhereToStoreDBFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "DB Doesn't Exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void importFromAssets() {
        if (sqLiteImporterExporter.isDataBaseExists())
        {
            try {
                sqLiteImporterExporter.importDataBaseFromAssets();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "DB Doesn't Exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void importFromSdCard(Uri SDCardUriWhereToGetDBFile) {
        if (sqLiteImporterExporter.isDataBaseExists())
        {
            try {
                sqLiteImporterExporter.importDataBase(SDCardUriWhereToGetDBFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "DB Doesn't Exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void importSQLiteFromSdCard() {
        if (sqLiteImporterExporter.isDataBaseExists())
        {
            try {
                /**
                 * SOURCE : /storage/emulated/0/RetrofitKitLocalDB.sqlite
                 * DESTINATION : /data/data/com.retrofit.kit/databases/RetrofitKitLocalDB.sqlite
                 */
                String source = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                String destination = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
                sqLiteImporterExporter.importSQLite(getApplicationContext(), SQLiteDatabaseConstants.SQLite_DATABASE_NAME, source, destination);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "DB Doesn't Exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void exportSQLiteToSdCard() {
        if (sqLiteImporterExporter.isDataBaseExists())
        {
            try
            {
                /**
                 * DESTINATION : /data/data/com.retrofit.kit/databases/RetrofitKitLocalDB.sqlite
                 * SOURCE : /storage/emulated/0/RetrofitKitLocalDB.sqlite
                 */
                String source = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
                String destination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
                sqLiteImporterExporter.exportSQLite(getApplicationContext(), SQLiteDatabaseConstants.SQLite_DATABASE_NAME, source, destination);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "DB Doesn't Exists", Toast.LENGTH_SHORT).show();
        }
    }
}