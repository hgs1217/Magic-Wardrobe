package sjtu.edu.cn.magic_wardrobe.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import sjtu.edu.cn.magic_wardrobe.model.PostureParams;


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
    private PostureParams params;


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
            double heightScalar = (double) height / photoHeight;
            double widthScalar = (double) width / photoWidth;
            double offset = (viewWidth - width) / 2.0;

            int point1X = (int) (params.getX() * widthScalar + offset),
                point1Y = (int) (params.getY() * heightScalar),
                point2X = (int) ((params.getX() + params.getWidth()) * widthScalar + offset),
                point2Y = (int) (params.getY() * heightScalar),
                point3X = (int) (params.getX() * widthScalar + offset),
                point3Y = (int) ((params.getY() + params.getHeight()) * heightScalar),
                point4X = (int) ((params.getX() + params.getWidth()) * widthScalar + offset),
                point4Y = (int) ((params.getY() + params.getHeight()) * heightScalar);

            Paint p = new Paint();
            p.setColor(Color.RED);
            p.setStrokeWidth(6);
            canvas.drawLine(point1X, point1Y, point2X, point2Y, p);
            canvas.drawLine(point1X, point1Y, point3X, point3Y, p);
            canvas.drawLine(point2X, point2Y, point4X, point4Y, p);
            canvas.drawLine(point3X, point3Y, point4X, point4Y, p);
        }
    }

    public void setParams(Context context, int viewWidth, int width, int height, int photoWidth, int photoHeight, PostureParams params) {
        this.width = width;
        this.height = height;
        this.viewWidth = viewWidth;
        this.photoWidth = photoWidth;
        this.photoHeight = photoHeight;
        this.params = params;
        this.context = context;

        Log.i("TEST", String.valueOf(width)+" "+String.valueOf(height)+" "+String.valueOf(photoWidth)+" "+String.valueOf(photoHeight));

        invalidate();
    }
}
