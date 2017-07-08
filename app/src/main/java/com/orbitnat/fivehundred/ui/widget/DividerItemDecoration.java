package com.orbitnat.fivehundred.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private final Drawable mDivider;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;

    private boolean mFillBegin = false;
    private boolean mFillEnd = false;

    /**
     * Default divider will be used
     */
    public DividerItemDecoration(Context context) {
        final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
        mDivider = styledAttributes.getDrawable(0);
        styledAttributes.recycle();

        initialize();
    }

    /**
     * Custom divider will be used
     */
    public DividerItemDecoration(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);

        initialize();
    }

    private void initialize() {
        mPaddingBottom = 0;
        mPaddingLeft = 0;
        mPaddingRight = 0;
        mPaddingTop = 0;
        mFillBegin = false;
        mFillEnd = false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = getPaddingLeftFromParent(parent) + mPaddingLeft;
        int right = parent.getWidth() - getPaddingRightFromParent(parent) - mPaddingRight;

        int top;
        int bottom;
        View child;
        RecyclerView.LayoutParams params;

        int childCount = parent.getChildCount();

        int parentBottom = parent.getBottom() - parent.getPaddingBottom();
        int parentTop = parent.getTop() + parent.getPaddingTop();

        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);

            params = (RecyclerView.LayoutParams) child.getLayoutParams();

            top = child.getBottom() + params.bottomMargin + mPaddingTop;
            bottom = top + mDivider.getIntrinsicHeight() - mPaddingBottom;

            //skip if this decoration is outside parent padding
            if (bottom > parentBottom || bottom < parentTop) {
                continue;
            }

            if(mFillEnd && i == (childCount - 1)) {
                left = getPaddingLeftFromParent(parent);
                right = parent.getWidth() - getPaddingRightFromParent(parent);
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

            // Draw divider at begin position.
            if(mFillBegin && i == 0) {
                left = getPaddingLeftFromParent(parent);
                right = parent.getWidth() - getPaddingRightFromParent(parent);
                top = child.getTop() - params.topMargin - mPaddingTop;
                bottom = top + mDivider.getIntrinsicHeight() - mPaddingBottom;

                //skip if this decoration is outside parent padding
                if (top > parentBottom || top < parentTop) {
                    continue;
                }

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);

                left = getPaddingLeftFromParent(parent) + mPaddingLeft;
                right = parent.getWidth() - getPaddingRightFromParent(parent) - mPaddingRight;
            }
        }
    }

    protected Drawable getDivider() {
        return mDivider;
    }

    protected int getPaddingLeftFromParent(RecyclerView parent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return parent.getPaddingStart();
        }
        else {
            return parent.getPaddingLeft();
        }
    }

    protected int getPaddingRightFromParent(RecyclerView parent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return parent.getPaddingEnd();
        }
        else {
            return parent.getPaddingRight();
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public void setPaddingLeft(int padding) {
        mPaddingLeft = padding;
    }

    public void setPaddingTop(int padding) {
        mPaddingTop = padding;
    }

    public void setPaddingRight(int padding) {
        mPaddingRight = padding;
    }

    public void setPaddingBottom(int padding) {
        mPaddingBottom = padding;
    }

    public void setFillEnd(boolean fillEnd) {
        mFillEnd = fillEnd;
    }

    public boolean isFillEnd() {
        return mFillEnd;
    }

    public void setFillBegin(boolean fillBegin) {
        mFillBegin = fillBegin;
    }

    public boolean isFillBegin() {
        return mFillBegin;
    }
}