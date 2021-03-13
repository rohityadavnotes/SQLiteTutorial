package com.sqlite.tutorial;

import androidx.annotation.NonNull;
import com.sqlite.tutorial.baseadapter.adapter.BaseSingleItemAdapter;
import com.sqlite.tutorial.baseadapter.adapter.BaseViewHolder;
import com.sqlite.tutorial.sqlite.model.Student;
import com.sqlite.tutorial.utilities.BitmapUtils;

public class RecyclerViewAdapter extends BaseSingleItemAdapter<Student, BaseViewHolder> {

    public RecyclerViewAdapter() {
        addChildClickViewIds(R.id.updateButtonTextView);
        addChildClickViewIds(R.id.deleteButtonTextView);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.recycler_view_row;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Student student, int position) {
        viewHolder.setImageBitmap(R.id.circleImageView, BitmapUtils.getBitmapFromByteArray(student.getPicture()));
        viewHolder.setText(R.id.firstNameTextView, student.getFirstName());
        viewHolder.setText(R.id.lastNameTextView, student.getLastName());
        viewHolder.setText(R.id.rollNumberTextView, student.getRollNumber());
    }
}
