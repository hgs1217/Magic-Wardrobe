package sjtu.edu.cn.magic_wardrobe.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import sjtu.edu.cn.magic_wardrobe.activity.SearchActivity;
import sjtu.edu.cn.magic_wardrobe.model.PostureParams;
import sjtu.edu.cn.magic_wardrobe.utils.ViewUtil;


/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class PostureView extends View {


    private Context context;
    private int photoHeight;
    private int photoWidth;
    private int height;
    private int width;
    private int viewWidth;
    private int point1X;
    private int point1Y;
    private int point2X;
    private int point2Y;
    private int point3X;
    private int point3Y;
    private int point4X;
    private int point4Y;
    double heightScalar;
    double widthScalar;
    double offset;
    private PostureParams params;
    private int markOffset = ViewUtil.dpToPx(SearchActivity.POSTURE_MARK_SIZE) / 2;


    public PostureView(Context c) {
        super(c);
    }

    public PostureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (context != null) {
//            calculateParams();

//            Log.i("TEST", String.valueOf(ViewUtil.getScreenHeight())+", "+String.valueOf(ViewUtil.getScreenWidth()));
//
//            Log.i("TEST", String.valueOf(height)+", "+String.valueOf(photoHeight)+", "+
//                    String.valueOf(width)+", "+String.valueOf(photoWidth)+", "+
//                    String.valueOf(heightScalar)+", "+String.valueOf(widthScalar)+", "+
//                    String.valueOf(viewWidth)+", "+String.valueOf(offset));
//
//            Log.i("TEST", String.valueOf(point1X)+", "+String.valueOf(point1Y)+", "+
//                    String.valueOf(point2X)+", "+String.valueOf(point2Y)+", "+
//                    String.valueOf(point3X)+", "+String.valueOf(point3Y)+", "+
//                    String.valueOf(point4X)+", "+String.valueOf(point4Y));

            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStrokeWidth(6);
            canvas.drawLine(point1X, point1Y, point2X, point2Y, p);
            canvas.drawLine(point1X, point1Y, point3X, point3Y, p);
            canvas.drawLine(point2X, point2Y, point4X, point4Y, p);
            canvas.drawLine(point3X, point3Y, point4X, point4Y, p);
        }
    }

    private void calculateParams() {
        heightScalar = (double) height / photoHeight;
        widthScalar = (double) width / photoWidth;
        offset = (viewWidth - width) / 2.0;

        point1X = (int) (params.getX() * widthScalar + offset);
        point1Y = (int) (params.getY() * heightScalar);
        point2X = (int) ((params.getX() + params.getWidth()) * widthScalar + offset);
        point2Y = (int) (params.getY() * heightScalar);
        point3X = (int) (params.getX() * widthScalar + offset);
        point3Y = (int) ((params.getY() + params.getHeight()) * heightScalar);
        point4X = (int) ((params.getX() + params.getWidth()) * widthScalar + offset);
        point4Y = (int) ((params.getY() + params.getHeight()) * heightScalar);
    }

    public void setParams(Context context, int viewWidth, int width, int height, int photoWidth, int photoHeight, PostureParams params) {
        this.width = width;
        this.height = height;
        this.viewWidth = viewWidth;
        this.photoWidth = photoWidth;
        this.photoHeight = photoHeight;
        this.params = params;
        this.context = context;
        calculateParams();

        Log.i("TEST", String.valueOf(width)+" "+String.valueOf(height)+" "+String.valueOf(photoWidth)+" "+String.valueOf(photoHeight));

        invalidate();
    }

    public void setPostureLoc(PostureParams params) {
        this.params = params;
        calculateParams();

        invalidate();
    }

    public void setPostureMark(int markNum, int top, int left) {
        switch (markNum) {
            case 1:
                point1X = point3X = left + markOffset;
                point1Y = point2Y = top + markOffset;
                break;
            case 2:
                point2X = point4X = left + markOffset;
                point2Y = point1Y = top + markOffset;
                break;
            case 3:
                point3X = point1X = left + markOffset;
                point3Y = point4Y = top + markOffset;
                break;
            case 4:
                point4X = point2X = left + markOffset;
                point4Y = point3Y = top + markOffset;
                break;
        }
        invalidate();
    }

    public PostureParams getPostureParams() {
        int x = (int) Math.abs(((point1X - offset) / widthScalar));
        int y = (int) Math.abs((point1Y / heightScalar));
        int width = (int) Math.abs(((point2X - offset) / widthScalar - x));
        int height = (int) Math.abs((point3Y / heightScalar - y));

//        Log.i("TESTV", String.valueOf(x)+"\t"+String.valueOf(y)+"\t"+String.valueOf(width)+"\t"+String.valueOf(height));
        return new PostureParams(x, y, width, height);
    }

    public int getPoint1X() {
        return point1X;
    }

    public int getPoint1Y() {
        return point1Y;
    }

    public int getPoint2X() {
        return point2X;
    }

    public int getPoint2Y() {
        return point2Y;
    }

    public int getPoint3X() {
        return point3X;
    }

    public int getPoint3Y() {
        return point3Y;
    }

    public int getPoint4X() {
        return point4X;
    }

    public int getPoint4Y() {
        return point4Y;
    }
}
