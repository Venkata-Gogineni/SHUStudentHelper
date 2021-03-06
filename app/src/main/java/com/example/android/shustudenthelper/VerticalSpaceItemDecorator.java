package com.example.android.shustudenthelper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Surya Gogineni on 10/25/2016.
 */

public class VerticalSpaceItemDecorator extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecorator(int verticalSpaceHeight) {

        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect,
                               View view,
                               RecyclerView parent,
                               RecyclerView.State state) {

        // 1. Determine if we want to add a spacing decorator
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {

            // 2. Set the bottom offset to the specified height
            outRect.bottom = verticalSpaceHeight;
        }
    }
}
