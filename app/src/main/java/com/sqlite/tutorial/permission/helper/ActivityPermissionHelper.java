package com.sqlite.tutorial.permission.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.sqlite.tutorial.BuildConfig;
import com.sqlite.tutorial.R;
import com.sqlite.tutorial.permission.PermissionGroups;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityPermissionHelper {

    private Activity activity;
    private Context context;

    /**
     * @param activity ( this )
     * @param context ( application context )
     *
     * call
     *       activityPermissionHelper = new ActivityPermissionHelper(this,context);
     */
    public ActivityPermissionHelper(Activity activity, Context context) {
        this.activity               = activity;
        this.context                = context;
    }

    /**
     * @return true if this class object is created, otherwise false
     */
    public boolean isNotNull() {
        return activity!=null && context!=null;
    }

    /**
     * क्या यह 6.0 या ऊपर है
     */
    public boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * क्या यह 8.0 या इससे ऊपर है
     */
    public boolean isOverOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * Is there permission for floating window
     */
    public boolean isHasOverlaysPermission(Context context) {
        if (isOverMarshmallow()) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    /**
     * Do you have installation permissions
     */
    public boolean isHasInstallPermission(Context context) {
        if (isOverOreo()) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * Returns list of the added permissions from the manifest file
     */
    public List<String> getListOfAddedPermissionInManifest(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try
        {
            return Arrays.asList(packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Check whether the permission is added or not in the manifest file
     *
     * @param listOfPermissions    list of permissions
     */
    public void checkPermissionsAvailableInManifestFile(List<String> listOfPermissions) {
        List<String> listOfAddedPermissionInManifest = getListOfAddedPermissionInManifest(activity);
        if (listOfAddedPermissionInManifest != null && listOfAddedPermissionInManifest.size() != 0)
        {
            for (String permission : listOfPermissions)
            {
                if (!listOfAddedPermissionInManifest.contains(permission))
                {
                    throw new RuntimeException("you must add this permission:"+permission+" to AndroidManifest");
                }
            }
        }
    }

    /**
     * @param singlePermission ( Manifest.permission.CAMERA )
     * @return true if permission is granted, otherwise false
     */
    public boolean hasSinglePermission(String singlePermission) {
        /*
         * Marshmallow +
         * If api level greater than or equal to 23, then all permission is
         * granted at runtime time.
         */
        if (isNotNull() && singlePermission != null && isOverMarshmallow())
        {
            /*
             * It returns an integer value of PERMISSION_GRANTED or PERMISSION_DENIED.
             */
            int result = ContextCompat.checkSelfPermission(activity, singlePermission);
            if(result == PackageManager.PERMISSION_GRANTED)
            {
                /*
                 * If permission is granted returning true
                 */
                return true;
            }
            else
            {
                /*
                 * If permission is not granted returning false
                 */
                return false;
            }
        }
        /*
         * Pre-Marshmallow
         * If api level less than 23, then all permission is
         * granted at install time in google play store.
         */
        else
        {
            /*
             * Below api level 23, always true
             */
            return true;
        }
    }

    /**
     * @param arrayOfPermissions Array of permissions to check
     * @return true if user allowed all permissions, otherwise false
     */
    public boolean hasMultiplePermissions(String[] arrayOfPermissions) {
        if (isNotNull() && arrayOfPermissions != null && isOverMarshmallow())
        {
            for (String permission : arrayOfPermissions)
            {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param permission Array of permissions to check
     * @return True if user allowed all permissions, false otherwise
     */
    public void askSinglePermission(String permission, int requestCode) {
        if (!hasSinglePermission(permission)) {
            ActivityCompat.requestPermissions(activity,new String[]{permission}, requestCode);
        }
    }

    /**
     * @param arrayOfPermissions Array of permissions to check
     * @return true if user allowed all permissions, false otherwise
     */
    public void askMultiplePermission(String[] arrayOfPermissions, int multiplePermissionRequestCode) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String permissions:arrayOfPermissions)
        {
            result = ContextCompat.checkSelfPermission(context,permissions);
            if (result != PackageManager.PERMISSION_GRANTED)
            {
                listPermissionsNeeded.add(permissions);
            }
        }

        if(!listPermissionsNeeded.isEmpty())
        {
            String[] multiplePermission = listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]);
            ActivityCompat.requestPermissions(activity, multiplePermission, multiplePermissionRequestCode);
        }
    }

    public void shouldShowRationale(String permission, int requestCode, int permissionIcon, String permissionName, String denied_WITHOUT_NEVER_ASK_AGAIN_CHECKED_Explain, String denied_WITH_NEVER_ASK_AGAIN_CHECKED_Explain) {
        if (isNotNull() && isOverMarshmallow())
        {
            /*
             * showRationale = false If permission is denied (and never ask again is checked), otherwise true
             * shouldShowRequestPermissionRationale will return false if user clicks Never Ask Again, otherwise true
             */
            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity,permission);
            if (showRationale)
            {
                System.out.println("================= Permission Denied Without Never Ask Again Checked =================");
                dialogWhenPermissionDenied_WITHOUT_NEVER_ASK_AGAIN_CHECKED(permission, requestCode, permissionIcon, permissionName, denied_WITHOUT_NEVER_ASK_AGAIN_CHECKED_Explain);
            }
            else
            {
                System.out.println("================= Permission Denied With Never Ask Again Checked =================");
                dialogWhenPermissionDenied_WITH_NEVER_ASK_AGAIN_CHECKED(permission, requestCode, permissionIcon, permissionName, denied_WITH_NEVER_ASK_AGAIN_CHECKED_Explain);
            }
        }
    }

    /**
     * Check that all given permissions have been granted by verifying that one entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @param grantResults all the permission request results
     * @return if permission granted
     */
    public boolean verifyPermission(int[] grantResults)
    {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @param grantResults all the permission request results
     * @return if permission granted
     */
    public static boolean verifyPermissions(int[] grantResults)
    {
        /*
         * Verify that each required permission has been granted, otherwise return false.
         */
        for (int result : grantResults)
        {
            if (result != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    public void dialogWhenPermissionDenied_WITHOUT_NEVER_ASK_AGAIN_CHECKED(String permission, int requestCode, int permissionIcon, String permissionName, String denied_WITHOUT_NEVER_ASK_AGAIN_CHECKED_Explain) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.permission_denied_then_alert_dialog, viewGroup, false);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ImageView permissionIconImageView           = dialogView.findViewById(R.id.permissionIconImageView);
        TextView permissionNameTextView             = dialogView.findViewById(R.id.permissionNameTextView);
        TextView explainWhyThisRequireTextView      = dialogView.findViewById(R.id.explainWhyThisRequireTextView);
        TextView closeButton                        = dialogView.findViewById(R.id.closeButtonTextView);
        TextView okButton                           = dialogView.findViewById(R.id.okButtonTextView);

        permissionIconImageView.setImageResource(permissionIcon);
        permissionNameTextView.setText(permissionName);
        explainWhyThisRequireTextView.setText(denied_WITHOUT_NEVER_ASK_AGAIN_CHECKED_Explain);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //activity.finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                askSinglePermission(permission, requestCode);
            }
        });
    }

    public void dialogWhenPermissionDenied_WITH_NEVER_ASK_AGAIN_CHECKED(String permission, int requestCode, int permissionIcon, String permissionName, String denied_WITH_NEVER_ASK_AGAIN_CHECKED_Explain) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.permission_never_ask_again_alert_dialog, viewGroup, false);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ImageView permissionIconImageView           = dialogView.findViewById(R.id.permissionIconImageView);
        TextView permissionNameTextView             = dialogView.findViewById(R.id.permissionNameTextView);
        TextView explainWhyThisRequireTextView      = dialogView.findViewById(R.id.explainWhyThisRequireTextView);
        TextView closeButton                        = dialogView.findViewById(R.id.closeButtonTextView);
        TextView settingButton                      = dialogView.findViewById(R.id.settingButtonTextView);

        permissionIconImageView.setImageResource(permissionIcon);
        permissionNameTextView.setText(permissionName);
        explainWhyThisRequireTextView.setText(denied_WITH_NEVER_ASK_AGAIN_CHECKED_Explain);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                //activity.finish();
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (isOverMarshmallow()) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                    activity.startActivityForResult(intent, PermissionGroups.PERMISSIONS_FROM_SETTING_REQUEST_CODE);
                }
            }
        });
    }
}

