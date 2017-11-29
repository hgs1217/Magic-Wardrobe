package sjtu.edu.cn.magic_wardrobe.model;

/**
 * Created by HgS_1217_ on 2017/11/15.
 */

public class PostureParams {

    public static String TAG = "PictureParams";

    private int x;
    private int y;
    private int width;
    private int height;

    public PostureParams(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
