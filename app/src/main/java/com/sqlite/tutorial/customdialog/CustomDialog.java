package com.sqlite.tutorial.customdialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends Dialog implements View.OnClickListener {

  private Activity activity;
  private int layoutResId;
  private int[] viewIds = new int[]{};
  private int animationResId = 0;
  private boolean isCancelable = true;
  private boolean isCanceledOnTouchOutside = true;
  private int showPosition;
  private List<View> setOnClickListenerOnThisViews = new ArrayList<>();
  private OnCustomDialogItemClickListener customDialogItemClickListener;

  public void setOnDialogItemClickListener(OnCustomDialogItemClickListener customDialogItemClickListener) {
    this.customDialogItemClickListener = customDialogItemClickListener;
  }

  /**
   * @param activity e.g, ActivityName.this
   * @param themeResId e.g,  R.style.Custom_Dialog_Style
   * @param layoutResId e.g, R.layout.loading_dialog
   * @param isCancelable e.g, true or false
   * @param isCanceledOnTouchOutside e.g, true or false
   */
  public CustomDialog(@NonNull Activity activity, @StyleRes int themeResId, @LayoutRes int layoutResId, @NonNull boolean isCancelable, @NonNull boolean isCanceledOnTouchOutside) {
    super(activity, themeResId);

    this.activity                 = activity;
    this.layoutResId              = layoutResId;
    this.isCancelable             = isCancelable;
    this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
  }

  /**
   * @param activity e.g, ActivityName.this
   * @param themeResId e.g,  R.style.Custom_Dialog_Style
   * @param layoutResId e.g, R.layout.loading_dialog
   * @param animationResId e.g, R.style.UpDownAnimation
   * @param isCancelable e.g, true or false*
   * @param isCanceledOnTouchOutside e.g, true or false
   * @param showPosition e.g, Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM, Gravity.LEFT, Gravity.RIGHT
   */
  public CustomDialog(@NonNull Activity activity, @StyleRes int themeResId, @LayoutRes int layoutResId, @StyleRes int animationResId, @NonNull boolean isCancelable, @NonNull boolean isCanceledOnTouchOutside, @NonNull int showPosition) {
    super(activity, themeResId);

    this.activity                 = activity;
    this.layoutResId              = layoutResId;
    this.animationResId           = animationResId;
    this.isCancelable             = isCancelable;
    this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
    this.showPosition             = showPosition;
  }

  /**
   * @param activity e.g, ActivityName.this
   * @param themeResId e.g,  R.style.Custom_Dialog_Style
   * @param layoutResId e.g, R.layout.loading_dialog
   * @param viewIds e.g, new int[]{R.id.progressBar, R.id.progressBar}
   * @param animationResId e.g, R.style.UpDownAnimation
   * @param isCancelable e.g, true or false*
   * @param isCanceledOnTouchOutside e.g, true or false
   * @param showPosition e.g, Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM, Gravity.LEFT, Gravity.RIGHT
   */
  public CustomDialog(@NonNull Activity activity, @StyleRes int themeResId, @LayoutRes int layoutResId, int[] viewIds, @StyleRes int animationResId, @NonNull boolean isCancelable, @NonNull boolean isCanceledOnTouchOutside, @NonNull int showPosition) {
    super(activity, themeResId);

    this.activity                 = activity;
    this.layoutResId              = layoutResId;
    this.viewIds                  = viewIds;
    this.animationResId           = animationResId;
    this.isCancelable             = isCancelable;
    this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
    this.showPosition             = showPosition;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(layoutResId);

    Window window = getWindow();
    WindowManager.LayoutParams layoutParams = window.getAttributes();
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    layoutParams.gravity = Gravity.CENTER;
    getWindow().setAttributes(layoutParams);

   /* Window window = getWindow();
    WindowManager windowManager             = (activity).getWindowManager();
    Display display                         = windowManager.getDefaultDisplay();
    WindowManager.LayoutParams layoutParams = window.getAttributes();
    layoutParams.width                      = display.getWidth() * 4 / 5; *//* Set the dialog width to 4/5 of the screen *//*
    getWindow().setAttributes(layoutParams);*/

    /* Traverse the control id, add click events, add resources to the collection */
    for (int id : viewIds)
    {
      View view = findViewById(id);
      view.setOnClickListener(this);
      setOnClickListenerOnThisViews.add(view);
    }

    if (animationResId != 0)
    {
      window.setWindowAnimations(animationResId); /* Add custom animation */
    }

    setCancelable(isCancelable);
    setCanceledOnTouchOutside(isCanceledOnTouchOutside);

    if (0 == showPosition)
    {
      window.setGravity(Gravity.CENTER); /* The default display position of the dialog is centered */
    }
    else
    {
      window.setGravity(showPosition); /* Set a custom dialog position */
    }
  }

  /**
   * Get the View collection that needs to be monitored
   *
   * @return
   */
  public List<View> getViews() {
    return setOnClickListenerOnThisViews;
  }

  @Override
  public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {
  }

  public interface OnCustomDialogItemClickListener {
    void OnCustomDialogItemClick(CustomDialog customDialog, View view);
  }

  @Override
  public void onClick(View view) {
    if (customDialogItemClickListener != null)
    {
      customDialogItemClickListener.OnCustomDialogItemClick(this, view);
    }
  }
}
