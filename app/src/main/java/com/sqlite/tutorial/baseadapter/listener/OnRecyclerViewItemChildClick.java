package com.sqlite.tutorial.baseadapter.listener;

import android.view.View;

public interface OnRecyclerViewItemChildClick<T> {
    void OnItemChildClick(View viewChild, T t, int position);
}
