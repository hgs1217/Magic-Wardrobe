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
import sjtu.edu.cn.magic_wardrobe.utils.ViewUtil;

/**
 * Created by HgS_1217_ on 2017/12/6.
 */

public class ViewDragFrameLayout extends FrameLayout {

    private static final int DRAG_BORDER = ViewUtil.dpToPx(SearchActivity.POSTURE_MARK_SIZE) / 2;


    ViewDragHelper dragHelper;

    SearchActivity searchActivity;

    ImageView markPosture1;
    ImageView markPosture2;
    ImageView markPosture3;
    ImageView markPosture4;

    private int topLoc;
    private int leftLoc;
    private int height;
    private int width;
    private int offset;

    public ViewDragFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        height = ViewUtil.dpToPx(SearchActivity.POSTURE_HEIGHT);
        width = ViewUtil.getScreenWidth() / 2;
        offset = ViewUtil.dpToPx(SearchActivity.POSTURE_MARK_SIZE) / 2;

        dragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top > height - offset - DRAG_BORDER) {
                    topLoc = height - offset - DRAG_BORDER;
                } else if (top < DRAG_BORDER - offset) {
                    topLoc = DRAG_BORDER - offset;
                } else {
                    topLoc = top;
                }
                return topLoc;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left > width - offset - DRAG_BORDER) {
                    leftLoc = width - offset - DRAG_BORDER;
                } else if (left < DRAG_BORDER - offset) {
                    leftLoc = DRAG_BORDER - offset;
                } else {
                    leftLoc = left;
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
