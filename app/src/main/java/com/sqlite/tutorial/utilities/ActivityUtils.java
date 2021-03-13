package com.sqlite.tutorial.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;

public class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /*
     * Method to check activity exists or not
     *
     * @param context           - context
     * @param parentPackageName - package name e.g., "android.java"
     * @param activityName      - activity name with full path e.g., "android.java.MainActivity"
     * @return true if exists, false if not exists
     *
     * Example :
     *              ActivityUtil.isActivityExists(getApplicationContext(),"android.java","android.java.NextActivity")
     *              ActivityUtil.isActivityExists(getApplicationContext(),getPackageName(),"android.java.NextActivity")
     */
    public static boolean isActivityExists(Context context,
                                           @NonNull final String parentPackageName,
                                           @NonNull final String activityName) {
        Intent intent = new Intent();
        intent.setClassName(parentPackageName, activityName);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * Method to launch activity
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     */
    public static void launchActivity(@NonNull final Activity currentActivity,
                                      @NonNull final Class<? extends Activity> nextActivityClass) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(goToNextActivity);
    }

    /**
     * Method to launch activity with animation
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     * @param enterAnim
     * @param exitAnim
     *
     * Example :
     *              ActivityUtil.launchActivityWithAnimation(NextActivity.this,MainActivity.class, R.anim.animate_swipe_left_enter, R.anim.animate_swipe_left_exit);
     */
    public static void launchActivityWithAnimation(@NonNull final Activity currentActivity,
                                                   @NonNull final Class<? extends Activity> nextActivityClass,
                                                   @AnimRes final int enterAnim,
                                                   @AnimRes final int exitAnim) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(goToNextActivity);
        currentActivity.overridePendingTransition(enterAnim, exitAnim);
    }

    /**
     * Method to launch activity with bundle
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     * @param bundle            - Pass bundle from one class to another
     */
    public static void launchActivityWithBundle(@NonNull final Activity currentActivity,
                                                @NonNull final Class<? extends Activity> nextActivityClass,
                                                @Nullable final Bundle bundle) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        goToNextActivity.putExtra("BUNDLE", bundle);
        currentActivity.startActivity(goToNextActivity);
    }

    /**
     * Method to launch activity with bundle and animation
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     * @param bundle            - Pass bundle from one class to another
     * @param enterAnim
     * @param exitAnim
     */
    public static void launchActivityWithBundleAndAnimation(@NonNull final Activity currentActivity,
                                                            @NonNull final Class<? extends Activity> nextActivityClass,
                                                            @Nullable final Bundle bundle,
                                                            @AnimRes final int enterAnim,
                                                            @AnimRes final int exitAnim) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        goToNextActivity.putExtra("BUNDLE", bundle);
        currentActivity.startActivity(goToNextActivity);
        currentActivity.overridePendingTransition(enterAnim, exitAnim);
    }

    public static void finishActivity(@NonNull final Activity nextActivityClass) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nextActivityClass.finishAfterTransition();
        } else {
            nextActivityClass.finish();
        }
    }

    /**
     * Method to launch activity with finish current activity
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     */
    public static void launchActivityWithFinish(@NonNull final Activity currentActivity,
                                                @NonNull final Class<? extends Activity> nextActivityClass) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(goToNextActivity);
        finishActivity(currentActivity);
    }

    /**
     * Method to launch activity with bundle and finish current activity
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     */
    public static void launchActivityWithBundleAndFinish(@NonNull final Activity currentActivity,
                                                         @NonNull final Class<? extends Activity> nextActivityClass,
                                                         @Nullable final Bundle bundle) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        goToNextActivity.putExtra("BUNDLE", bundle);
        currentActivity.startActivity(goToNextActivity);
        finishActivity(currentActivity);
    }

    /**
     * Method to launch activity with finish all BackStack activity
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     */
    public static void launchActivityWithClearBackStack(@NonNull final Activity currentActivity,
                                                        @NonNull final Class<? extends Activity> nextActivityClass) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        goToNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentActivity.startActivity(goToNextActivity);
        finishActivity(currentActivity);
    }

    /**
     * Method to launch activity with bundle and finish all BackStack activity
     *
     * @param currentActivity   - Current activity
     * @param nextActivityClass - Activity class that should be launched
     * @param bundle            - Pass bundle from one class to another
     */
    public static void launchActivityWithClearBackStack(@NonNull final Activity currentActivity,
                                                        @NonNull final Class<? extends Activity> nextActivityClass,
                                                        @Nullable final Bundle bundle) {
        Intent goToNextActivity = new Intent(currentActivity, nextActivityClass);
        goToNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        goToNextActivity.putExtra("BUNDLE", bundle);
        currentActivity.startActivity(goToNextActivity);
        finishActivity(currentActivity);
    }

    /**
     * Method to get previous activity bundle data
     *
     * @param currentActivity - Current activity
     */
    public static Bundle getDataFromPreviousActivity(@NonNull final Activity currentActivity) {
        Intent data = currentActivity.getIntent();
        return data.getBundleExtra("BUNDLE");
    }

    public static <T> void putObjectInBundle(String key, T data, Intent intent) {
        Bundle bundle = new Bundle();

        try {

            if (data instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) data);
            } else if (data instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) data);
            }
            intent.putExtras(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to launch next activity with bundle value data as model class (i.e startIntent)
     *
     * @param currentActivity      - Context of the activity
     * @param nextActivityClass    - Activity class that should be launched
     * @param closeCurrentActivity - Pass 'true' to close current activity
     */
    public static <T> void launchActivity(@NonNull final Activity currentActivity, @NonNull final Class<? extends Activity> nextActivityClass, boolean closeCurrentActivity, String key, T data) {
        Intent intent = new Intent(currentActivity, nextActivityClass);

        if (data != null) {
            putObjectInBundle(key, data, intent);
        }
        currentActivity.startActivity(intent);
        if (closeCurrentActivity) {
            currentActivity.finish();
        }
    }

    public static <T> T getBundleParecelableExtra(Activity activity, String key) {
        return activity.getIntent().getParcelableExtra(key);
    }

    public static <T> T getBundleSerializableExtra(Activity activity, String key) {
        return (T) activity.getIntent().getSerializableExtra(key);
    }
}
