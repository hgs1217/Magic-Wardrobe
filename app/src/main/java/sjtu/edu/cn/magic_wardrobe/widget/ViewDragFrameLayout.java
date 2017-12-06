package sjtu.edu.cn.magic_wardrobe.widget;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import sjtu.edu.cn.magic_wardrobe.R;
import sjtu.edu.cn.magic_wardrobe.activity.SearchActivity;

/**
 * Created by HgS_1217_ on 2017/12/6.
 */

public class ViewDragFrameLayout extends FrameLayout {

    ViewDragHelper dragHelper;

    SearchActivity searchActivity;

    ImageView markPosture1;
    ImageView markPosture2;
    ImageView markPosture3;
    ImageView markPosture4;

    int topLoc;
    int leftLoc;

    public ViewDragFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                topLoc = top;
                if (top > getHeight() - child.getMeasuredHeight()) {
                    topLoc = getHeight() - child.getMeasuredHeight();
                } else if (top < 0) {
                    topLoc = 0;
                }
                return topLoc;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                leftLoc = left;
                if (left > getWidth() - child.getMeasuredWidth()) {
                    leftLoc = getWidth() - child.getMeasuredWidth();
                } else if (left < 0) {
                    leftLoc = 0;
                }
                return leftLoc;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                int markNum = getMarkNum(releasedChild);
                searchActivity.setPostureMark(markNum, topLoc, leftLoc);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        markPosture1 = (ImageView) findViewById(R.id.mark_posture_1);
        markPosture2 = (ImageView) findViewById(R.id.mark_posture_2);
        markPosture3 = (ImageView) findViewById(R.id.mark_posture_3);
        markPosture4 = (ImageView) findViewById(R.id.mark_posture_4);
    }

    private int getMarkNum(View child) {
        if (child == markPosture1) {
            return 1;
        } else if (child == markPosture2) {
            return 2;
        } else if (child == markPosture3) {
            return 3;
        } else if (child == markPosture4) {
            return 4;
        }
        return 0;
    }

    public void setSearchActivity(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }
}
