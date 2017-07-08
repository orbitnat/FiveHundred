package com.orbitnat.fivehundred.ui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public abstract class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private View mChildViewDownAction;
    private float mOldX;
    private float mOldY;
    private static final float LIMIT_DISTANCE = 4;
    private View mLeafViewDownAction;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());

        if(childView != null) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_UP:
                    if(childView == mChildViewDownAction) {
                        int position = view.getChildLayoutPosition(childView);
                        long id = view.getAdapter().getItemId(position);

                        if(mLeafViewDownAction != null && isInBound(mLeafViewDownAction, e.getRawX(), e.getRawY())) {
                            onItemClick(view, childView, position, id, mLeafViewDownAction);
                        }
                        else {
                            onItemClick(view, childView, position, id, childView);
                        }
                    }

                    mChildViewDownAction = null;
                    mLeafViewDownAction = null;

                    break;

                case MotionEvent.ACTION_DOWN:
                    mChildViewDownAction = childView;
                    mLeafViewDownAction = findLeafView(childView, e);
                    mOldX = e.getX();
                    mOldY = e.getY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if(mChildViewDownAction != null) {
                        boolean isVertical = true;

                        if(view.getLayoutManager() instanceof LinearLayoutManager) {
                            isVertical = ((LinearLayoutManager) view.getLayoutManager()).getOrientation() == LinearLayoutManager.VERTICAL;
                        }
                        // TODO: Support other layout managers.

                        float diff;
                        if(isVertical) {
                            diff = Math.abs(e.getY() - mOldY) / view.getResources().getDisplayMetrics().density;
                        }
                        else {
                            diff = Math.abs(e.getX() - mOldX) / view.getResources().getDisplayMetrics().density;
                        }

                        // Consider if list is scrolled.
                        if(diff >= LIMIT_DISTANCE) {
                            mChildViewDownAction = null;
                            mLeafViewDownAction = null;
                        }
                    }
                    break;
            }
        }

        return false;
    }

    private View findLeafView(View view, MotionEvent e) {
        View leafView = null;

        if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            View childView;

            for(int i=0;i<childCount;i++) {
                childView = viewGroup.getChildAt(i);

                if(childView.getVisibility() == View.VISIBLE) {
                    leafView = findLeafView(childView, e);

                    if (leafView != null) {
                        break;
                    }
                }
            }
        }

        if(leafView == null) {
            if(view.getVisibility() == View.VISIBLE && view.isClickable() && isInBound(view, e.getRawX(), e.getRawY())) {
                leafView = view;
            }
        }

        return leafView;
    }

    private boolean isInBound(View view, float actionX, float actionY) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);

        return actionX >= locations[0] && actionX <= locations[0] + view.getWidth()
                && actionY >= locations[1] && actionY <= locations[1] + view.getHeight();
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public abstract void onItemClick(RecyclerView parent, View view, int position, long id, View targetView);
}