package sjtu.edu.cn.magic_wardrobe.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HgS_1217_ on 2017/11/10.
 */

public class HttpResponse {

    @SerializedName("error")
    private int error;
    @SerializedName("msg")
    private String msg;

    public int getError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }
}
