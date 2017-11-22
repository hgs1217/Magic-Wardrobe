package sjtu.edu.cn.magic_wardrobe.model;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class ImageInfo {

    private String imgUrl;
    private int width;
    private int height;

    public ImageInfo(String url, int w, int h) {
        imgUrl = url;
        width = w;
        height = h;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
