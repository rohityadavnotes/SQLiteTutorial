package com.sqlite.tutorial.baseadapter.listener;

import android.view.View;

public interface OnRecyclerViewItemClick<T> {
    void OnItemClick(View itemView, T t, int position);
}
