package sjtu.edu.cn.magic_wardrobe.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HgS_1217_ on 2017/11/21.
 */

public class ImageInfo {

    @SerializedName("imgUrl")
    private String imgUrl;
    @SerializedName("imgWidth")
    private Integer width;
    @SerializedName("imgHeight")
    private Integer height;

    public String getImgUrl() {
        return imgUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }
}
