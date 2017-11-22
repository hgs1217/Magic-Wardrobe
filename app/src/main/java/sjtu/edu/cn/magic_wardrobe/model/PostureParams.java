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

    public PostureParams(String cmd) {
        String[] substrings = cmd.split(";");
        try {
            x = Integer.parseInt(substrings[0]);
            y = Integer.parseInt(substrings[1]);
            width = Integer.parseInt(substrings[2]);
            height = Integer.parseInt(substrings[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
