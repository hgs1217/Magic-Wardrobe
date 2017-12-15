package sjtu.edu.cn.magic_wardrobe.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import sjtu.edu.cn.magic_wardrobe.model.OnceSearchParams;

/**
 * Created by HgS_1217_ on 2017/11/28.
 */

public class OnceSearchResponse extends HttpResponse {

    @SerializedName("h")
    private Double h;
    @SerializedName("s")
    private Double s;
    @SerializedName("v")
    private Double v;
    @SerializedName("attrs")
    private List<Integer> attrs;
    @SerializedName("images")
    private List<ImageInfo> images;

    private OnceSearchParams onceSearchParams = null;

    public OnceSearchParams getOnceSearchParams() {
        if (onceSearchParams == null) {
            onceSearchParams = new OnceSearchParams(h, s, v, attrs, images);
        }
        return onceSearchParams;
    }
}
