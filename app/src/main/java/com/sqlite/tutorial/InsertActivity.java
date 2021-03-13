package com.sqlite.tutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sqlite.tutorial.constants.RequestCodeConstants;
import com.sqlite.tutorial.customimageview.CircleImageView;
import com.sqlite.tutorial.permission.PermissionGroups;
import com.sqlite.tutorial.permission.helper.ActivityPermissionHelper;
import com.sqlite.tutorial.sqlite.SQLiteDatabaseOperation;
import com.sqlite.tutorial.sqlite.model.Student;
import com.sqlite.tutorial.utilities.ActivityUtils;
import com.sqlite.tutorial.utilities.BitmapUtils;
import com.sqlite.tutorial.utilities.LogcatUtils;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class InsertActivity extends AppCompatActivity {

    public static final String TAG = InsertActivity.class.getSimpleName();

    private CircleImageView circleImageView;
    private FloatingActionButton selectPictureFloatingActionButton;

    private TextInputLayout firstNameTextInputLayout, lastNameTextInputLayout, rollNumberTextInputLayout;
    private TextInputEditText firstNameTextInputEditText, lastNameTextInputEditText, rollNumberTextInputEditText;

    private MaterialButton insertMaterialButton;

    private String firstNameString;
    private String lastNameString;
    private String rollNumberString;
    private byte[] pictureByteArray;

    private ActivityPermissionHelper activityPermissionHelper;
    private SQLiteDatabaseOperation sqLiteDatabaseOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        initializeView();
        initializeObject();
        onTextChangedListener();
        initializeEvent();
    }

    protected void initializeView() {
        circleImageView                         = findViewById(R.id.circleImageView);
        selectPictureFloatingActionButton       = findViewById(R.id.selectPictureFloatingActionButton);

        firstNameTextInputLayout                = findViewById(R.id.firstNameTextInputLayout);
        firstNameTextInputEditText              = findViewById(R.id.firstNameTextInputEditText);

        lastNameTextInputLayout                 = findViewById(R.id.lastNameTextInputLayout);
        lastNameTextInputEditText               = findViewById(R.id.lastNameTextInputEditText);

        rollNumberTextInputLayout               = findViewById(R.id.rollNumberTextInputLayout);
        rollNumberTextInputEditText             = findViewById(R.id.rollNumberTextInputEditText);

        insertMaterialButton                    = findViewById(R.id.insertMaterialButton);
    }

    protected void initializeObject() {
        activityPermissionHelper = new ActivityPermissionHelper(this,getApplicationContext());
        sqLiteDatabaseOperation = SQLiteDatabaseOperation.getInstance(getApplicationContext());
    }

    protected void onTextChangedListener() {
        firstNameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    firstNameTextInputLayout.setErrorEnabled(true);
                    firstNameTextInputLayout.setError("Please enter your first name !");
                }
                else if(text.length() > 0)
                {
                    firstNameTextInputLayout.setError(null);
                    firstNameTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastNameTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    lastNameTextInputLayout.setErrorEnabled(true);
                    lastNameTextInputLayout.setError("Please enter your last name !");
                }
                else if(text.length() > 0)
                {
                    lastNameTextInputLayout.setError(null);
                    lastNameTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rollNumberTextInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if(text.length() < 1)
                {
                    rollNumberTextInputLayout.setErrorEnabled(true);
                    rollNumberTextInputLayout.setError("Please enter your roll number !");
                }
                else if(text.length() > 0)
                {
                    rollNumberTextInputLayout.setError(null);
                    rollNumberTextInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    protected void initializeEvent() {
        selectPictureFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(activityPermissionHelper.hasSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    openFileManager(InsertActivity.this,
                            "image/*",
                            true,
                            false,
                            RequestCodeConstants.SELECT_FILE_REQUEST_CODE);
                }
                else
                {
                    activityPermissionHelper.askSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE, PermissionGroups.STORAGE_GROUP_REQUEST_CODE);
                }
            }
        });

        insertMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameString         = Objects.requireNonNull(firstNameTextInputEditText.getText()).toString();
                lastNameString          = Objects.requireNonNull(lastNameTextInputEditText.getText()).toString();
                rollNumberString       = Objects.requireNonNull(rollNumberTextInputEditText.getText()).toString();

                if (insertValidation())
                {
                    insertFirstWay(firstNameString, lastNameString, rollNumberString, pictureByteArray);
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
                    openFileManager(InsertActivity.this,
                            "image/*",
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
                            Manifest.permission.READ_EXTERNAL_STORAGE,
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
                if(RequestCodeConstants.SELECT_FILE_REQUEST_CODE == requestCode)
                {
                    if(resultData.getData() != null) {
                        Uri uri = resultData.getData();
                        Bitmap bitmap;
                        try {
                            bitmap = BitmapUtils.handleSamplingAndRotationBitmap(InsertActivity.this, uri, 0, 0);
                            circleImageView.setImageBitmap(bitmap);
                            pictureByteArray = BitmapUtils.getByteArrayFromBitmap(bitmap);
                        } catch (IOException iOException) {
                            Toast.makeText(getApplicationContext(), iOException.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            iOException.printStackTrace();
                        }
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
            if(activityPermissionHelper.hasSinglePermission(PermissionGroups.READ_EXTERNAL_STORAGE))
            {
                openFileManager(InsertActivity.this,
                        "image/*",
                        true,
                        false,
                        RequestCodeConstants.SELECT_FILE_REQUEST_CODE);
            }
            else
            {
                activityPermissionHelper.askSinglePermission(PermissionGroups.READ_EXTERNAL_STORAGE, PermissionGroups.STORAGE_GROUP_REQUEST_CODE);
            }
        }
    }

    public boolean insertValidation()
    {
        if (pictureByteArray == null)
        {
            String message = "Please select profile picture !";
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            return false;
        }
        if (Objects.requireNonNull(firstNameTextInputEditText.getText()).toString().length() < 1)
        {
            String message = "Please enter your first name !";
            firstNameTextInputLayout.setError(message);
            return false;
        }
        if (Objects.requireNonNull(lastNameTextInputEditText.getText()).toString().length() < 1)
        {
            String message = "Please enter your last name !";
            lastNameTextInputLayout.setError(message);
            return false;
        }
        if (Objects.requireNonNull(rollNumberTextInputEditText.getText()).toString().length() < 1)
        {
            String message = "Please enter your roll number !";
            rollNumberTextInputLayout.setError(message);
            return false;
        }
        return true;
    }

    private void insertFirstWay(String firstName, String lastName, String rollNumber, byte[] pictureByteArray) {
        Student student = new Student(firstName, lastName, rollNumber, pictureByteArray);
        boolean isInsert = sqLiteDatabaseOperation.insertFirstWay(student);
        if(isInsert)
        {
            ActivityUtils.launchActivity(InsertActivity.this, SQLiteActivity.class);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Not insert",Toast.LENGTH_LONG).show();
        }
    }

    private void insertSecondWay(String firstName, String lastName, String rollNumber, byte[] pictureByteArray) {
        Student student = new Student(firstName, lastName, rollNumber, pictureByteArray);
        sqLiteDatabaseOperation.insertSecondWay(student);
        ActivityUtils.launchActivity(InsertActivity.this, SQLiteActivity.class);
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

    public String generateInt() {
        Random rand = new Random();
        int randomInt = rand.nextInt(100000);
        return String.valueOf(randomInt);
    }

    public String generateString() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int length = 7;

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            stringBuilder.append(randomChar);
        }

        String randomString = stringBuilder.toString();
        return randomString;
    }
}