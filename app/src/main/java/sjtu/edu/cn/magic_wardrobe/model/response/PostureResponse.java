package sjtu.edu.cn.magic_wardrobe.model.response;

import com.google.gson.annotations.SerializedName;

import sjtu.edu.cn.magic_wardrobe.model.PostureParams;

/**
 * Created by HgS_1217_ on 2017/11/28.
 */

public class PostureResponse extends HttpResponse {

    @SerializedName("x")
    private Integer x;
    @SerializedName("y")
    private Integer y;
    @SerializedName("width")
    private Integer width;
    @SerializedName("height")
    private Integer height;

    private PostureParams postureParams = null;

    public PostureParams getPostureParams() {
        if (postureParams == null) {
            postureParams = new PostureParams(x, y, width, height);
        }
        return postureParams;
    }
}
